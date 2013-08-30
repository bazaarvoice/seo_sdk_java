package com.bazaarvoice.seo.sdk.config;

/**
 * Enums that should be used only for configuring bazaarvoice server configuration.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public enum BVCoreConfig {

	STAGING_S3_HOSTNAME("stagingS3Hostname"),
	PRODUCTION_S3_HOSTNAME("productionS3Hostname"), 
	VERSION("version");

	private String propertyName;
	
	private BVCoreConfig(String propertyName) {
		this.propertyName = propertyName; 
	}
	
	public String getPropertyName() {
		return this.propertyName;
	}
	
}
