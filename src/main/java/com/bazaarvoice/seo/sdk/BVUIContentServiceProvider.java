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
import com.bazaarvoice.seo.sdk.servlet.RequestContext;
import com.bazaarvoice.seo.sdk.servlet.RequestFilter;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkUrl;
import com.bazaarvoice.seo.sdk.util.BVMessageUtil;
import com.bazaarvoice.seo.sdk.util.BVThreadPool;
import com.bazaarvoice.seo.sdk.util.BVUtility;
import com.bazaarvoice.seo.sdk.util.Environment;
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
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Implementation class for {@link BVUIContentService}. This class is a self
 * executable of Callable and Future and returns StringBuilder upon execution.
 *
 * @author Anandan Narayanaswamy
 */
public class BVUIContentServiceProvider
  implements BVUIContentService, Callable<StringBuilder> {

  private final static Logger LOGGER = LoggerFactory.getLogger(
    BVUIContentServiceProvider.class
  );
  private final static String HTTP_HEADER_USER_AGENT = "User-Agent";
  private final static String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
  private final static String HTTP_HEADER_ACCEPT_ENCODING_GZIP = "gzip";

  private BVConfiguration bvConfiguration;
  private BVParameters bvParameters;
  private StringBuilder message;
  private StringBuilder uiContent;
  private BVSeoSdkUrl bvSeoSdkUrl;
  private Boolean sdkEnabled;

  public BVUIContentServiceProvider(BVConfiguration bvConfiguration) {
    this.bvConfiguration = bvConfiguration;

    message = new StringBuilder();
    uiContent = new StringBuilder();
  }

  public StringBuilder call() throws Exception {

    try {
      // Includes integration script if one is enabled.
      includeIntegrationCode();
      URI seoContentUrl = bvSeoSdkUrl.seoContentUri();
      String correctedBaseUri = bvSeoSdkUrl.correctedBaseUri();
      getBvContent(uiContent, seoContentUrl, correctedBaseUri);

    } catch (BVSdkException e) {
      message.append(e.getMessage());
    }

    return uiContent;
  }

  /**
   * Format for UserAgent Header values used in outgoing requests.
   * Includes Operating System, JRE Version, and Package version.
   */
  private static final String USER_AGENT_FORMAT = String.format("JRE/%s;bv_java_sdk/%s;",
    Environment.getJreVersion() != null ? Environment.getJreVersion() : "unknown",
    Environment.getPackageSpecificationVersion()
  ) + "%s";

  /**
   * UserAgent Header value to use when making requests.
   */
  private String getUserAgent()
  {
    String requestUserAgent = getRequestUserAgent();
    String userAgent = requestUserAgent != null ? requestUserAgent : getParameterUserAgent();
    if (userAgent == null || userAgent.isEmpty())
    {
      LOGGER.debug(BVMessageUtil.getMessage("MSG0007"));
    }
    return String.format(USER_AGENT_FORMAT, userAgent);
  }

  /**
   * The UserAgent as specified by bvParameters
   */
  private String getParameterUserAgent()
  {
    String paramUserAgent = bvParameters.getUserAgent();
    return StringUtils.isBlank(paramUserAgent) ? null : paramUserAgent;
  }

  /**
   * The user agent as specified on the incoming request.
   */
  private String getRequestUserAgent() {
    String userAgent = RequestContext.getHeader(HTTP_HEADER_USER_AGENT);
    if (StringUtils.isNotBlank(userAgent)) {
      return userAgent;
    }
    return null;
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

    BVUtility.replacePageURIFromContent(
      sb,
      baseUri
    );
  }

  private boolean isContentFromFile() {
    return Boolean.parseBoolean(bvConfiguration.getProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY.getPropertyName()
    ));
  }

  private String loadContentFromHttp(URI path) {
    int connectionTimeout = Integer.parseInt(bvConfiguration.getProperty(
      BVClientConfig.CONNECT_TIMEOUT.getPropertyName()
    ));
    int socketTimeout = Integer.parseInt(bvConfiguration.getProperty(
      BVClientConfig.SOCKET_TIMEOUT.getPropertyName()
    ));
    String proxyHost = bvConfiguration.getProperty(
      BVClientConfig.PROXY_HOST.getPropertyName()
    );
    String charsetConfig = bvConfiguration.getProperty(
      BVClientConfig.CHARSET.getPropertyName()
    );
    Charset charset = null;
    try {
      charset = charsetConfig == null ?
        Charset.defaultCharset() : Charset.forName(charsetConfig);
    } catch (Exception e) {
      LOGGER.error(BVMessageUtil.getMessage("ERR0024"));
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
        int proxyPort = Integer.parseInt(bvConfiguration.getProperty(
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
      httpUrlConnection.setRequestProperty(HTTP_HEADER_USER_AGENT, getUserAgent());
      httpUrlConnection.setRequestProperty(HTTP_HEADER_ACCEPT_ENCODING, HTTP_HEADER_ACCEPT_ENCODING_GZIP);

      is = httpUrlConnection.getInputStream();
      if(httpUrlConnection.getContentEncoding().equalsIgnoreCase(HTTP_HEADER_ACCEPT_ENCODING_GZIP)){
        is = new GZIPInputStream(is);
      }

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
    } catch (IOException ioe) {
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
        bvConfiguration.getProperty(BVClientConfig.CHARSET.getPropertyName())
      );
    } catch (IOException e) {
      throw new BVSdkException("ERR0012");
    }

    return content;
  }

  private void includeIntegrationCode() {
    String includeScriptStr = bvConfiguration.getProperty(
      BVClientConfig.INCLUDE_DISPLAY_INTEGRATION_CODE.getPropertyName()
    );
    boolean includeIntegrationScript = Boolean.parseBoolean(includeScriptStr);

    if (!includeIntegrationScript) {
      return;
    }

    Object[] params = {
      bvParameters.getSubjectType().uriValue(),
      bvParameters.getSubjectId()
    };
    String integrationScriptValue = bvConfiguration.getProperty(
      bvParameters.getContentType().getIntegrationScriptProperty()
    );
    String integrationScript = MessageFormat.format(
      integrationScriptValue,
      params
    );

    uiContent.append(integrationScript);
  }

  public boolean showUserAgentSEOContent() {
    if (bvParameters == null || bvParameters.getUserAgent() == null) {
      return false;
    }

    String crawlerAgentPattern = bvConfiguration.getProperty(
      BVClientConfig.CRAWLER_AGENT_PATTERN.getPropertyName()
    );
    if (!StringUtils.isBlank(crawlerAgentPattern)) {
      crawlerAgentPattern = ".*(" + crawlerAgentPattern + ").*";
    }
    Pattern pattern = Pattern.compile(
      crawlerAgentPattern,
      Pattern.CASE_INSENSITIVE
    );
    LOGGER.debug("userAgent is : " + bvParameters.getUserAgent());

    return (
      pattern.matcher(bvParameters.getUserAgent()).matches() ||
        bvParameters.getUserAgent().toLowerCase().contains("google")
    );
  }

  public void setBVParameters(BVParameters bvParameters) {
    this.bvParameters = bvParameters;
  }

  public void setBVSeoSdkUrl(BVSeoSdkUrl bvSeoSdkUrl) {
    this.bvSeoSdkUrl = bvSeoSdkUrl;
  }

  public boolean isSdkEnabled() {
    if (sdkEnabled == null) {
      sdkEnabled = Boolean.parseBoolean(bvConfiguration.getProperty(
        BVClientConfig.SEO_SDK_ENABLED.getPropertyName()
      ));
      sdkEnabled = sdkEnabled || BVUtility.isRevealDebugEnabled(bvParameters);
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
      return new StringBuilder(uiContent);
    }

    /**
     * StringBuilder depends on Length to reference the position of the next character;
     * We can effectively clear the StringBuilder by resetting it's length to '0'.
     */
    uiContent.setLength(0);

    boolean isSearchBot = showUserAgentSEOContent();
    long executionTimeout = isSearchBot ? Long.parseLong(
      bvConfiguration.getProperty(
        BVClientConfig.EXECUTION_TIMEOUT_BOT.getPropertyName()
      )
    ) : Long.parseLong(bvConfiguration.getProperty(
      BVClientConfig.EXECUTION_TIMEOUT.getPropertyName()
    ));

    if (!isSearchBot && executionTimeout == 0) {
      message.append(BVMessageUtil.getMessage("MSG0004"));
      return null;
    }

    if (isSearchBot && executionTimeout < 100) {
      executionTimeout = 100;
      message.append(BVMessageUtil.getMessage("MSG0005"));
    }

    ExecutorService executorService = BVThreadPool.getExecutorService();
    Future<StringBuilder> future = executorService.submit(this);

    try {
      uiContent = future.get(executionTimeout, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      // TODO: handle this.
    } catch (ExecutionException e) {
      if (e.getCause() instanceof BVSdkException) {
        throw new BVSdkException(e.getCause().getMessage());
      }
    } catch (TimeoutException e) {
      String err = isSearchBot ? "ERR0026" : "ERR0018";
      message.append(MessageFormat.format(
        BVMessageUtil.getMessage(err),
        new Object[]{executionTimeout}
      ));
    }

    return new StringBuilder(uiContent);
  }

  public StringBuilder getMessage() {
    return message;
  }
}
