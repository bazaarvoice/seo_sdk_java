package com.bazaarvoice.seo.sdk.util;

/**
 * Common constants class used through out SEO SDK implementations.
 * @author Anandan Narayanaswamy
 *
 */
public final class BVConstant {

	public static final String BVREVEAL = "bvreveal";
	public static final String BVREVEAL_DEBUG = "bvreveal=debug";
	public static final String JS_DISPLAY_MSG = "JavaScript-only Display;";
	public static final String INCLUDE_PAGE_URI = "{INSERT_PAGE_URI}";
	
	/*
	 * Some of the default property values.
	 */
	public static final String STAGING_S3_HOSTNAME = "seo-stg.bazaarvoice.com"; 
	public static final String PRODUCTION_S3_HOSTNAME = "seo.bazaarvoice.com";
	public static final String EXECUTION_TIMEOUT = "3000";
	public static final String CRAWLER_AGENT_PATTERN = "msnbot|google|teoma|bingbot|yandexbot|yahoo";
	public static final String CONNECT_TIMEOUT = "1000";
	public static final String SOCKET_TIMEOUT = "1000";
	public static final String STAGING = "false";
	public static final String SEO_SDK_ENABLED = "true";
}
