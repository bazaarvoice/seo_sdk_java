package com.bazaarvoice.seo.sdk;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkUrl;
import com.bazaarvoice.seo.sdk.util.BVConstant;
import com.bazaarvoice.seo.sdk.util.BVMessageUtil;
import com.bazaarvoice.seo.sdk.util.BVUtilty;

/**
 * Implementation class for {@link BVUIContentService}. This class is a self
 * executable of Callable and Future and returns StringBuilder upon execution.
 *
 * @author Anandan Narayanaswamy
 *
 */
public class BVUIContentServiceProvider implements BVUIContentService, Callable<StringBuilder> {

    private final static Logger _logger = LoggerFactory.getLogger(BVUIContentServiceProvider.class);
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
        String displayJSOnly = null;
        URI seoContentUrl = null;
        try {
            //includes integration script if one is enabled.
            includeIntegrationCode();

            boolean isBotDetection = Boolean.parseBoolean(_bvConfiguration.getProperty(BVClientConfig.BOT_DETECTION.getPropertyName()));

            /*
             * Hit only when botDetection is disabled or if the queryString is appended with bvreveal or if it matches any 
             * crawler pattern that is configured at the client configuration. 
             */
            if (!isBotDetection || _bvSeoSdkUrl.queryString().contains(BVConstant.BVREVEAL) || showUserAgentSEOContent()) {
                seoContentUrl = _bvSeoSdkUrl.seoContentUri();
                String correctedBaseUri = _bvSeoSdkUrl.correctedBaseUri();
                getBvContent(_uiContent, seoContentUrl, correctedBaseUri);
            } else {
                displayJSOnly = BVConstant.JS_DISPLAY_MSG;
            }
        } catch (BVSdkException e) {
            _message.append(e.getMessage());
        }

        if (displayJSOnly != null) {
            _message.append(displayJSOnly);
        }
        return _uiContent;
    }

    private void getBvContent(StringBuilder sb, URI seoContentUrl, String baseUri) {
        if (isContentFromFile()) {
            sb.append(loadContentFromFile(seoContentUrl));
        } else {
            sb.append(loadContentFromHttp(seoContentUrl));
        }

        BVUtilty.replaceString(sb, BVConstant.INCLUDE_PAGE_URI, baseUri + (baseUri.contains("?") ? "&" : "?"));
    }

    private boolean isContentFromFile() {
        boolean loadFromFile = Boolean.parseBoolean(_bvConfiguration.
                getProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY.getPropertyName()));
        return loadFromFile;
    }

    private String loadContentFromHttp(URI path) {
        int connectionTimeout = Integer.parseInt(_bvConfiguration.getProperty(BVClientConfig.CONNECT_TIMEOUT.getPropertyName()));
        int socketTimeout = Integer.parseInt(_bvConfiguration.getProperty(BVClientConfig.SOCKET_TIMEOUT.getPropertyName()));
        int proxyPort = Integer.parseInt(_bvConfiguration.getProperty(BVClientConfig.PROXY_PORT.getPropertyName()));
        String proxyHost = _bvConfiguration.getProperty(BVClientConfig.PROXY_HOST.getPropertyName());
        String content = null;
        
        try {
        	
        	Request httpRequest = Request.Get(path).connectTimeout(connectionTimeout).
                    socketTimeout(socketTimeout); 
        	
        	if (!StringUtils.isBlank(proxyHost) && !"none".equalsIgnoreCase(proxyHost)) {
                HttpHost proxy = new HttpHost(proxyHost, proxyPort);
                httpRequest.viaProxy(proxy);
            } 
           
        	content = new String(httpRequest.execute().returnContent().asBytes());
        	
        } catch (ClientProtocolException e) {
            throw new BVSdkException("ERR0012");
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                throw new BVSdkException("ERR0019");
            } else {
                throw new BVSdkException(e.getMessage());
            }
        }

        return content;
    }

    private String loadContentFromFile(URI path) {

        String content = null;
        try {
        	File file = new File(path);
            content = FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            throw new BVSdkException("ERR0012");
        }

        return content;
    }

    private void includeIntegrationCode() {
        String includeScriptStr = _bvConfiguration.getProperty(BVClientConfig.INCLUDE_DISPLAY_INTEGRATION_CODE.getPropertyName());
        boolean includeIntegrationScript = Boolean.parseBoolean(includeScriptStr);

        if (!includeIntegrationScript) {
            return;
        }

        Object[] params = {_bvParameters.getSubjectType().uriValue(), _bvParameters.getSubjectId()};
        String integrationScriptValue = _bvConfiguration.getProperty(_bvParameters.getContentType().getIntegrationScriptProperty());
        String integrationScript = MessageFormat.format(integrationScriptValue, params);

        _uiContent.append(integrationScript);
    }

    public boolean showUserAgentSEOContent() {
        if (_bvParameters == null || _bvParameters.getUserAgent() == null) {
            return false;
        }

        String crawlerAgentPattern = _bvConfiguration.getProperty(BVClientConfig.CRAWLER_AGENT_PATTERN.getPropertyName());
        if (!StringUtils.isBlank(crawlerAgentPattern)) {
            crawlerAgentPattern = ".*(" + crawlerAgentPattern + ").*";
        }
        Pattern pattern = Pattern.compile(crawlerAgentPattern, Pattern.CASE_INSENSITIVE);
        _logger.debug("userAgent is : " + _bvParameters.getUserAgent());

        return (pattern.matcher(_bvParameters.getUserAgent()).matches() || _bvParameters.getUserAgent().toLowerCase().contains("google"));
    }

    public void setBVParameters(BVParameters bvParameters) {
        _bvParameters = bvParameters;
    }

    public void setBVSeoSdkUrl(BVSeoSdkUrl bvSeoSdkUrl) {
        _bvSeoSdkUrl = bvSeoSdkUrl;
    }

    public boolean isSdkEnabled() {
        if (sdkEnabled == null) {
            sdkEnabled = Boolean.parseBoolean(_bvConfiguration.getProperty(BVClientConfig.SEO_SDK_ENABLED.getPropertyName()));
            sdkEnabled = sdkEnabled || _bvSeoSdkUrl.queryString().contains(BVConstant.BVREVEAL);
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

        long executionTimeout = Long.parseLong(_bvConfiguration.getProperty(BVClientConfig.EXECUTION_TIMEOUT.getPropertyName()));
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<StringBuilder> future = executorService.submit(this);

        try {
            _uiContent = future.get(executionTimeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            if (e.getCause() instanceof BVSdkException) {
                throw new BVSdkException(e.getCause().getMessage());
            }
        } catch (TimeoutException e) {
            _message.append(MessageFormat.format(BVMessageUtil.getMessage("ERR0018"), new Object[]{executionTimeout}));
        }

        return new StringBuilder(_uiContent);
    }

    public StringBuilder getMessage() {
        return _message;
    }
}
