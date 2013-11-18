package com.bazaarvoice.seo.sdk.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.util.BVConstant;

/**
 * Default implementation of configuration settings. This loads the Bazaarvoice
 * specific configuration and also user specific setting.
 *
 * @author Anandan Narayanaswamy
 *
 */
public class BVSdkConfiguration implements BVConfiguration {

    private static Logger _logger = LoggerFactory.getLogger(BVSdkConfiguration.class);
    private Map<String, String> _instanceConfiguration;

    /**
     * Default constructor. If configuration should be overwritten or if you
     * need to load properties/configuration every time please use the add
     * method.
     */
    public BVSdkConfiguration() {
        _instanceConfiguration = new HashMap<String, String>();

        /*
         * Adding bvcore properties.
         */
        _instanceConfiguration.put(BVCoreConfig.PRODUCTION_S3_HOSTNAME.getPropertyName(), BVConstant.PRODUCTION_S3_HOSTNAME);
        _instanceConfiguration.put(BVCoreConfig.STAGING_S3_HOSTNAME.getPropertyName(), BVConstant.STAGING_S3_HOSTNAME);

        addProperty(BVClientConfig.EXECUTION_TIMEOUT, BVConstant.EXECUTION_TIMEOUT);
        addProperty(BVClientConfig.CRAWLER_AGENT_PATTERN, BVConstant.CRAWLER_AGENT_PATTERN);
        addProperty(BVClientConfig.CONNECT_TIMEOUT, BVConstant.CONNECT_TIMEOUT);
        addProperty(BVClientConfig.SOCKET_TIMEOUT, BVConstant.SOCKET_TIMEOUT);
        addProperty(BVClientConfig.STAGING, BVConstant.STAGING);
        addProperty(BVClientConfig.SEO_SDK_ENABLED, BVConstant.SEO_SDK_ENABLED);
        addProperty(BVClientConfig.PROXY_HOST, BVConstant.PROXY_HOST);
        addProperty(BVClientConfig.PROXY_PORT, BVConstant.PROXY_PORT);
        _logger.debug("Completed default properties in BVSdkConfiguration.");
    }

    public BVConfiguration addProperty(BVClientConfig bvConfig, String propertyValue) {
        if (StringUtils.isBlank(propertyValue)) {
            throw new BVSdkException("ERR0006");
        }

        this._instanceConfiguration.put(bvConfig.getPropertyName(), propertyValue);
        return this;
    }

    public String getProperty(String propertyName) {
        return this._instanceConfiguration.get(propertyName);
    }
}
