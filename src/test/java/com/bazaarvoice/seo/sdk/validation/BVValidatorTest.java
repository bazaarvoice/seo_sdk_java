package com.bazaarvoice.seo.sdk.validation;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;

/**
 * Test case implementation for BVParameterValidator.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public class BVValidatorTest {
	
	/**
	 * test case for validate method in BVParameterValidator.
	 * This test case starts from failure scenario to till success scenario.
	 */
	@Test
	public void testValidation() {
		String errorMessage = null;
		BVConfiguration bvConfig = null;
		BVParameters bvParams = null;
		
		BVValidator bvValidator = new BVDefaultValidator();
		errorMessage = bvValidator.validate(bvConfig, bvParams);
		assertEquals(errorMessage.contains("BVConfiguration is null, please set a valid BVConfiguration.;"), true, "Error Messages are different.");
		
		errorMessage = null;
		bvConfig = new BVSdkConfiguration();
		bvParams = new BVParameters();
		bvValidator = new BVDefaultValidator();
		errorMessage = bvValidator.validate(bvConfig, bvParams);
		System.out.println(errorMessage);
		assertEquals(errorMessage.contains("userAgent in BVParameters is null.;SubjectId cannot be null or empty.;subjectType is null in BVParameters.;"), 
				true, "Error Messages are different.");
		
	}
	
}
