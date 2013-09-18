package com.bazaarvoice.seo.sdk.validation;

import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.model.BVParameters;

/**
 * Validation interface to validate the attributes in BVParameters.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public interface BVValidator {

	/**
	 * Method to validate bvConfiguration & bvParameters.
	 * 
	 * @param bvConfigration	the bvConfiguration object.
	 * @param bvParams			the bvParameter object.
	 * @return String errors if invalid attribute is found.
	 */
	String validate(BVConfiguration bvConfiguration, BVParameters bvParams) throws BVSdkException;
	
}
