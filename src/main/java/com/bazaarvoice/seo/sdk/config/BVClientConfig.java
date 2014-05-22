package com.bazaarvoice.seo.sdk.config;

public enum BVClientConfig {

    BV_ROOT_FOLDER("bv.root.folder"),
    CLOUD_KEY("cloudKey"),
    LOAD_SEO_FILES_LOCALLY("loadSEOFilesLocally"),
    LOCAL_SEO_FILE_ROOT("localSEOFileRoot"),
    CONNECT_TIMEOUT("connectTimeout"),
    SOCKET_TIMEOUT("socketTimeout"),
    INCLUDE_DISPLAY_INTEGRATION_CODE("includeDisplayIntegrationCode"),
    BOT_DETECTION("botDetection"),
    CRAWLER_AGENT_PATTERN("crawlerAgentPattern"),
    SEO_SDK_ENABLED("seo.sdk.enabled"),
    STAGING("staging"),
    EXECUTION_TIMEOUT("seo.sdk.execution.timeout"),
    PROXY_HOST("seo.sdk.execution.proxy.host"),
    PROXY_PORT("seo.sdk.execution.proxy.port"),
    CHARSET("seo.sdk.charset"),
    SSL_ENABLED("seo.sdk.ssl.enabled");
    
    private String propertyName;

    private BVClientConfig(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
