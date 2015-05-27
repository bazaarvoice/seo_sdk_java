/*
 * ===========================================================================
 * Copyright 2014 Bazaarvoice, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ===========================================================================
 *
 */

package com.bazaarvoice.seo.sdk;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkUrl;
import com.bazaarvoice.seo.sdk.util.BVConstant;
import com.bazaarvoice.seo.sdk.util.BVMessageUtil;
import com.bazaarvoice.seo.sdk.util.BVThreadPool;
import com.bazaarvoice.seo.sdk.util.BVUtility;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.net.Proxy.Type;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.concurrent.*;
import java.util.regex.Pattern;

/**
 * Implementation class for {@link BVUIContentService}. This class is a self
 * executable of Callable and Future and returns StringBuilder upon execution.
 *
 * @author Anandan Narayanaswamy
 */
public class BVUIContentServiceProvider
  implements BVUIContentService, Callable<StringBuilder> {

  private final static Logger _logger = LoggerFactory.getLogger(
    BVUIContentServiceProvider.class
  );
  private BVConfiguration _bvConfiguration;
  private BVParameters _bvParameters;
  private StringBuilder _message;
  private StringBuilder _uiContent;
  private BVSeoSdkUrl _bvSeoSdkUrl;
  private Boolean sdkEnabled;

  public BVUIContentServiceProvider(BVConfiguration bvConfiguration) {
    _bvConfiguration = bvConfiguration;

    _message = new StringBuilder();
    _uiContent = new StringBuilder();
  }

  public StringBuilder call() throws Exception {

    URI seoContentUrl = null;

    try {
      // Includes integration script if one is enabled.
      includeIntegrationCode();
      seoContentUrl = _bvSeoSdkUrl.seoContentUri();
      String correctedBaseUri = _bvSeoSdkUrl.correctedBaseUri();
      getBvContent(_uiContent, seoContentUrl, correctedBaseUri);

    } catch (BVSdkException e) {
      _message.append(e.getMessage());
    }

    return _uiContent;
  }

  private void getBvContent(
    StringBuilder sb,
    URI seoContentUrl,
    String baseUri
  ) {
    if (isContentFromFile()) {
      sb.append(loadContentFromFile(seoContentUrl));
    } else {
      sb.append(loadContentFromHttp(seoContentUrl));
    }

    BVUtility.replaceString(
      sb,
      BVConstant.INCLUDE_PAGE_URI,
      baseUri + (baseUri.contains("?") ? "&" : "?")
    );
  }

  private boolean isContentFromFile() {
    boolean loadFromFile = Boolean.parseBoolean(_bvConfiguration.getProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY.getPropertyName()
    ));
    return loadFromFile;
  }

  private String loadContentFromHttp(URI path) {
    int connectionTimeout = Integer.parseInt(_bvConfiguration.getProperty(
      BVClientConfig.CONNECT_TIMEOUT.getPropertyName()
    ));
    int socketTimeout = Integer.parseInt(_bvConfiguration.getProperty(
      BVClientConfig.SOCKET_TIMEOUT.getPropertyName()
    ));
    String proxyHost = _bvConfiguration.getProperty(
      BVClientConfig.PROXY_HOST.getPropertyName()
    );
    String charsetConfig = _bvConfiguration.getProperty(
      BVClientConfig.CHARSET.getPropertyName()
    );
    Charset charset = null;
    try {
      charset = charsetConfig == null ?
        Charset.defaultCharset() : Charset.forName(charsetConfig);
    } catch (Exception e) {
      _logger.error(BVMessageUtil.getMessage("ERR0024"));
      charset = Charset.defaultCharset();
    }

    String content = null;
    InputStream is = null;
    HttpURLConnection httpUrlConnection = null;
    try {
      URL url = path.toURL();

      if (
        !StringUtils.isBlank(proxyHost) &&
        !"none".equalsIgnoreCase(proxyHost)
      ) {
        int proxyPort = Integer.parseInt(_bvConfiguration.getProperty(
          BVClientConfig.PROXY_PORT.getPropertyName()
        ));
        SocketAddress socketAddress = new InetSocketAddress(
          proxyHost,
          proxyPort
        );
        Proxy proxy = new Proxy(Type.HTTP, socketAddress);
        httpUrlConnection = (HttpURLConnection) url.openConnection(proxy);
      } else {
        httpUrlConnection = (HttpURLConnection) url.openConnection();
      }

      httpUrlConnection.setConnectTimeout(connectionTimeout);
      httpUrlConnection.setReadTimeout(socketTimeout);

      is = httpUrlConnection.getInputStream();

      byte[] byteArray = IOUtils.toByteArray(is);
      is.close();

      if (byteArray == null) {
        throw new BVSdkException("ERR0025");
      }

      content = new String(byteArray, charset.name());
    } catch (MalformedURLException e) {
      // TODO: handle this.
    } catch (IOException e) {
      // Error stream needs to be read fully to keep the connection persistent on exceptions
      handleErrorStream(httpUrlConnection);
      if (e instanceof SocketTimeoutException) {
        throw new BVSdkException(e.getMessage());
      } else {
        throw new BVSdkException("ERR0012");
      }
    } catch (BVSdkException bve) {
      throw bve;
    } finally {
      IOUtils.closeQuietly(is);
    }

    boolean isValidContent = BVUtility.validateBVContent(content);
    if (!isValidContent) {
      throw new BVSdkException("ERR0025");
    }

    return content;
  }

  /**
   * Handle errorStream on httpConnection to help with Keep-Alive.
   * In order to help with Keep-Alive, we need to call getErrorStream()
   * and get the response body. This method needs to be called when IOException occurs.
   * http://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html
   *
   * @param httpUrlConnection the connection to get the error stream
   */
  private void handleErrorStream(final HttpURLConnection httpUrlConnection) {
    InputStream es = null;
    try {
      es = httpUrlConnection.getErrorStream();
      if (es != null) {
        IOUtils.toByteArray(es);
      }
    } catch(IOException ioe) {
      // Swallow any errors.
    } finally {
      IOUtils.closeQuietly(es);
    }
  }

  private String loadContentFromFile(URI path) {
    String content = null;
    try {
      File file = new File(path);
      content = FileUtils.readFileToString(
        file,
        _bvConfiguration.getProperty(BVClientConfig.CHARSET.getPropertyName())
      );
    } catch (IOException e) {
      throw new BVSdkException("ERR0012");
    }

    return content;
  }

  private void includeIntegrationCode() {
    String includeScriptStr = _bvConfiguration.getProperty(
      BVClientConfig.INCLUDE_DISPLAY_INTEGRATION_CODE.getPropertyName()
    );
    boolean includeIntegrationScript = Boolean.parseBoolean(includeScriptStr);

    if (!includeIntegrationScript) {
      return;
    }

    Object[] params = {
      _bvParameters.getSubjectType().uriValue(),
      _bvParameters.getSubjectId()
    };
    String integrationScriptValue = _bvConfiguration.getProperty(
      _bvParameters.getContentType().getIntegrationScriptProperty()
    );
    String integrationScript = MessageFormat.format(
      integrationScriptValue,
      params
    );

    _uiContent.append(integrationScript);
  }

  public boolean showUserAgentSEOContent() {
    if (_bvParameters == null || _bvParameters.getUserAgent() == null) {
      return false;
    }

    String crawlerAgentPattern = _bvConfiguration.getProperty(
      BVClientConfig.CRAWLER_AGENT_PATTERN.getPropertyName()
    );
    if (!StringUtils.isBlank(crawlerAgentPattern)) {
      crawlerAgentPattern = ".*(" + crawlerAgentPattern + ").*";
    }
    Pattern pattern = Pattern.compile(
      crawlerAgentPattern,
      Pattern.CASE_INSENSITIVE
    );
    _logger.debug("userAgent is : " + _bvParameters.getUserAgent());

    return (
      pattern.matcher(_bvParameters.getUserAgent()).matches() ||
      _bvParameters.getUserAgent().toLowerCase().contains("google")
    );
  }

  public void setBVParameters(BVParameters bvParameters) {
    _bvParameters = bvParameters;
  }

  public void setBVSeoSdkUrl(BVSeoSdkUrl bvSeoSdkUrl) {
    _bvSeoSdkUrl = bvSeoSdkUrl;
  }

  public boolean isSdkEnabled() {
    if (sdkEnabled == null) {
      sdkEnabled = Boolean.parseBoolean(_bvConfiguration.getProperty(
        BVClientConfig.SEO_SDK_ENABLED.getPropertyName()
      ));
      sdkEnabled = sdkEnabled || _bvSeoSdkUrl.queryString().contains(
        BVConstant.BVREVEAL
      );
    }

    return sdkEnabled;
  }

  /**
   * Self executioner method.
   *
   * @param reload
   * @return
   */
  public StringBuilder executeCall(boolean reload) {
    if (reload) {
      return new StringBuilder(_uiContent);
    }

    boolean isSearchBot = showUserAgentSEOContent();
    long executionTimeout = isSearchBot ? Long.parseLong(
      _bvConfiguration.getProperty(
        BVClientConfig.EXECUTION_TIMEOUT_BOT.getPropertyName()
      )
    ) : Long.parseLong(_bvConfiguration.getProperty(
      BVClientConfig.EXECUTION_TIMEOUT.getPropertyName()
    ));

    if (!isSearchBot && executionTimeout == 0) {
      _message.append(BVMessageUtil.getMessage("MSG0004"));
      return null;
    }

    if (isSearchBot && executionTimeout < 100) {
      executionTimeout = 100;
      _message.append(BVMessageUtil.getMessage("MSG0005"));
    }

    ExecutorService executorService = BVThreadPool.getExecutorService();
    Future<StringBuilder> future = executorService.submit(this);

    try {
      _uiContent = future.get(executionTimeout, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      // TODO: handle this.
    } catch (ExecutionException e) {
      if (e.getCause() instanceof BVSdkException) {
        throw new BVSdkException(e.getCause().getMessage());
      }
    } catch (TimeoutException e) {
      String err = isSearchBot ? "ERR0026" : "ERR0018";
      _message.append(MessageFormat.format(
        BVMessageUtil.getMessage(err),
        new Object[]{executionTimeout}
      ));
    }

    return new StringBuilder(_uiContent);
  }

  public StringBuilder getMessage() {
    return _message;
  }
}
