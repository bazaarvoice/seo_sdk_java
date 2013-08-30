package com.bazaarvoice.seo.sdk.config;

/**
 * Configuration Interface to get or set configurations.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public interface BVConfiguration {

	/**
	 * Add configurations to existing configuration(s).
	 * Make sure all the configurations mentioned are added from BVConfig.
	 * Any value that is already added to the configuration will be over written.
	 * 
	 * @param bvConfig
	 * @param propertyValue
	 * @return BVConfiguration
	 */
	BVConfiguration addProperty (BVClientConfig bvConfig, String propertyValue);
	
	/**
	 * Retrieves the value of the property.
	 * This could be retrieved from anywhere.
	 * It could be the property from properties or from hashmap or
	 * from a custom implementation.
	 * 
	 * @param propertyName
	 * @return property from the configuration.
	 */
	String getProperty (String propertyName);
	
}
