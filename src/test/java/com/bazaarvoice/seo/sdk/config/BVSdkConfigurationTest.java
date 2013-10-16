package com.bazaarvoice.seo.sdk.config;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.util.BVMessageUtil;

/**
 * Test class for BVSdkConfiguration implementation class.
 * Check individual docs on each test methods provided in this test class
 * for detailed information.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public class BVSdkConfigurationTest {

	/**
	 * Test case to check if bvconfig.properties loads properly.
	 * Assertions are made randomly to cross verify on the configurations that are provided by default.
	 * Any change in the property may lead to failure in the test case
	 * which should be corrected.
	 * 
	 * There are no possible error scenarios arising since the property file loads from the 
	 * resource bundle that is already provided.
	 * 
	 */
	@Test
	public void testBVConfigLoading() {
		BVConfiguration bvConfiguration = new BVSdkConfiguration();
		String stagingS3Hostname = bvConfiguration.getProperty("stagingS3Hostname");
		assertEquals(stagingS3Hostname, "seo-stg.bazaarvoice.com", "stagingS3Hostname are different.");
		
		String productionS3Hostname = bvConfiguration.getProperty("productionS3Hostname");
		assertEquals(productionS3Hostname, "seo.bazaarvoice.com", "productionS3Hostname are different.");
	}
	
	/**
	 * Test case for both add and getProperty methods.
	 * Contains both positive scenario and negative scenario.
	 * Add and getProperty are interchangeably used hence
	 * this test case will server the purpose for both add and getProperty methods.
	 * 
	 */
	@Test
	public void testAdd_getProperty() {
		
		BVConfiguration bvConfiguration = new BVSdkConfiguration();
		String actualErrorMessage = null;
		String depZoneId = null;
		String actualDepZoneId = null;
		
		/*
		 * Negative scenario. trying to add null value to existing value.
		 */
		try {
			bvConfiguration.addProperty(BVClientConfig.BV_ROOT_FOLDER, depZoneId);
		} catch (BVSdkException e) {
			actualErrorMessage = e.getMessage();
		}
		assertEquals(actualErrorMessage, BVMessageUtil.getMessage("ERR0006"), "Error message does not match.");
		actualDepZoneId = bvConfiguration.getProperty(BVClientConfig.BV_ROOT_FOLDER.getPropertyName());
		assertEquals(actualDepZoneId, null, "actualDepZoneId does not match.");
		
		/*
		 * Negative scenario. trying to add empty value.
		 */
		actualErrorMessage = null;
		actualDepZoneId = null;
		depZoneId = "";
		try {
			bvConfiguration.addProperty(BVClientConfig.BV_ROOT_FOLDER, depZoneId);
		} catch (BVSdkException e) {
			actualErrorMessage = e.getMessage();
		}
		assertEquals(actualErrorMessage, BVMessageUtil.getMessage("ERR0006"), "Error message does not match.");
		actualDepZoneId = bvConfiguration.getProperty(BVClientConfig.BV_ROOT_FOLDER.getPropertyName());
		assertEquals(actualDepZoneId, null, "actualDepZoneId does not match.");
		
		/*
		 * positive scenario. when adding valid value.
		 */
		actualErrorMessage = null;
		actualDepZoneId = null;
		depZoneId = "DEP_ZONE_ID";
		try {
			bvConfiguration.addProperty(BVClientConfig.BV_ROOT_FOLDER, depZoneId);
		} catch (BVSdkException e) {
			actualErrorMessage = e.getMessage();
			fail("There was an exception please fix the code so that the exception will not occur.");
		}
		assertNull(actualErrorMessage, "There should not be any error message.");
		actualDepZoneId = bvConfiguration.getProperty(BVClientConfig.BV_ROOT_FOLDER.getPropertyName());
		assertEquals(depZoneId, actualDepZoneId, "deployment zone id does not match");
		
		/*
		 * positive scenario. value overwrite.
		 */
		actualErrorMessage = null;
		actualDepZoneId = null;
		depZoneId = "DEP_ZONE_ID_OVERWRITE";
		try {
			bvConfiguration.addProperty(BVClientConfig.BV_ROOT_FOLDER, depZoneId);
		} catch (BVSdkException e) {
			actualErrorMessage = e.getMessage();
			fail("There was an exception please fix the code so that the exception will not occur.");
		}
		assertNull(actualErrorMessage, "There should not be any error message.");
		actualDepZoneId = bvConfiguration.getProperty(BVClientConfig.BV_ROOT_FOLDER.getPropertyName());
		assertEquals(depZoneId, actualDepZoneId, "deployment zone id does not match");
		
		/*
		 * positive scenario : when trying to get non-existence property.
		 */
		String nonExistingProperty = bvConfiguration.getProperty("NON_EXISTING_PROPERTY");
		assertNull(nonExistingProperty, "the nonExistingProperty should be null.");
		
	}
	
	/**
	 * Test case to test multiple instance support for BVSDKConfiguration.
	 */
	@Test
	public void testMultipleConfigurations() {
		BVConfiguration configurationInstance_1 = new BVSdkConfiguration();
		configurationInstance_1.addProperty(BVClientConfig.BV_ROOT_FOLDER, "DEP_INST_1");
		
		BVConfiguration configurationInstance_2 = new BVSdkConfiguration();
		configurationInstance_2.addProperty(BVClientConfig.BV_ROOT_FOLDER, "DEP_INST_2");
		
		String deploymentInstance_1 = configurationInstance_1.getProperty(BVClientConfig.BV_ROOT_FOLDER.getPropertyName());
		String deploymentInstance_2 = configurationInstance_2.getProperty(BVClientConfig.BV_ROOT_FOLDER.getPropertyName());
		assertEquals(deploymentInstance_1.equalsIgnoreCase(deploymentInstance_2), false, "Instance configuration should not be same");
	}

	/**
	 * Test case to test if selected BVCore contents from bvconfig.properties are not changed
	 */
	@Test
	public void testBVCoreConfig() {
		BVConfiguration configuration = new BVSdkConfiguration();
		String hostName = configuration.getProperty(BVCoreConfig.PRODUCTION_S3_HOSTNAME.getPropertyName());
		assertEquals(hostName, "seo.bazaarvoice.com",  "production hostname has been changed please fix.");
		
		hostName = configuration.getProperty(BVCoreConfig.STAGING_S3_HOSTNAME.getPropertyName());
		assertEquals(hostName, "seo-stg.bazaarvoice.com",  "staging hostname has been changed please fix.");
	}
}
