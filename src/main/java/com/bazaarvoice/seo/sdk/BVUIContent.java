package com.bazaarvoice.seo.sdk;

import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;

/**
 * 
 * @author Anandan Narayanaswamy
 *
 */
public interface BVUIContent {

	String getContent(BVParameters bvQueryParams);
	
	String getContent(BVConfiguration bvConfig, BVParameters bvQueryParams);
	
}
