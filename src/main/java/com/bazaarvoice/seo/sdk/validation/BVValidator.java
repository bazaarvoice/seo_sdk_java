package com.bazaarvoice.seo.sdk.validation;

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
	 * Method to validate bvParameters.
	 * 
	 * @param bvParams			the bvParameter object.
	 * @throws BVSdkException	if invalid attribute is found, throws BVsdkException.
	 */
	void validate(BVParameters bvParams) throws BVSdkException;
	
}
