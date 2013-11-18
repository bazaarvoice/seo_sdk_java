package com.bazaarvoice.seo.sdk;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVCoreConfig;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;

/**
 * Test class to simulate a connection time out scenario
 *
 * @author Anandan Narayanaswamy
 *
 */
public class BVManagedUIContent_ConnectionTimedout {

    private BVConfiguration bvConfiguration;

    /**
     * Test case to simulate connection timeout issue.
     */
    @Test
    public void testConnectionTimeOut() {
        bvConfiguration = new BVSDKConfigurationSimulator();

        BVParameters _bvParam = new BVParameters();
        _bvParam.setUserAgent("google");
        _bvParam.setBaseURI("/thispage.htm"); // this value is used to build pagination links
        _bvParam.setPageURI("http://localhost:8080/abcd" + "?" + "notSure=1&letSee=2"); //this value is used to needed BV URL parameters
        _bvParam.setContentType(ContentType.REVIEWS);
        _bvParam.setSubjectType(SubjectType.PRODUCT);
        _bvParam.setSubjectId("10204080000800000-I");

        BVUIContent bvUIContent = new BVManagedUIContent(bvConfiguration);
        String theContent = bvUIContent.getContent(_bvParam);
        System.out.println(theContent);
        Assert.assertEquals(theContent.contains("Connect to google.com:81 timed out"), true, "There should timed out message.");

    }

    /**
     * Mocked BVConfiguration class to test a sample invalid timout url by
     * supplying hostname as google.com:81
     *
     * @author Anandan Narayanaswamy
     *
     */
    private class BVSDKConfigurationSimulator implements BVConfiguration {

        private Map<String, String> propertyMap;

        public BVSDKConfigurationSimulator() {
            propertyMap = new HashMap<String, String>();
            propertyMap.put(BVCoreConfig.STAGING_S3_HOSTNAME.getPropertyName(), "google.com:81");
            propertyMap.put(BVCoreConfig.PRODUCTION_S3_HOSTNAME.getPropertyName(), "google.com:81");
            propertyMap.put(BVClientConfig.BOT_DETECTION.getPropertyName(), "true");
            propertyMap.put(BVClientConfig.BV_ROOT_FOLDER.getPropertyName(), "rootFolder");
            propertyMap.put(BVClientConfig.CLOUD_KEY.getPropertyName(), "cloudKey");
            propertyMap.put(BVClientConfig.CONNECT_TIMEOUT.getPropertyName(), "100");
            propertyMap.put(BVClientConfig.CRAWLER_AGENT_PATTERN.getPropertyName(), ".*(msnbot|google|teoma|bingbot|yandexbot|yahoo).*");
            propertyMap.put(BVClientConfig.INCLUDE_DISPLAY_INTEGRATION_CODE.getPropertyName(), "false");
            propertyMap.put(BVClientConfig.LOAD_SEO_FILES_LOCALLY.getPropertyName(), "false");
            propertyMap.put(BVClientConfig.LOCAL_SEO_FILE_ROOT.getPropertyName(), "/");
            propertyMap.put(BVClientConfig.SEO_SDK_ENABLED.getPropertyName(), "true");
            propertyMap.put(BVClientConfig.SOCKET_TIMEOUT.getPropertyName(), "1000");
            propertyMap.put(BVClientConfig.STAGING.getPropertyName(), "true");
            propertyMap.put(BVClientConfig.EXECUTION_TIMEOUT.getPropertyName(), "3000");
        }

        public BVConfiguration addProperty(BVClientConfig bvConfig,
                String propertyValue) {
            // TODO Auto-generated method stub
            return null;
        }

        public String getProperty(String propertyName) {
            return propertyMap.get(propertyName);
        }
    }
}
