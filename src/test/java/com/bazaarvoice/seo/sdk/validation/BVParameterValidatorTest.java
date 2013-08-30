package com.bazaarvoice.seo.sdk.validation;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;
import com.bazaarvoice.seo.sdk.util.BVMessageUtil;

/**
 * Test case implementation for BVParameterValidator.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public class BVParameterValidatorTest {
	
	/**
	 * test case for validate method in BVParameterValidator.
	 * This test case starts from failure scenario to till success scenario.
	 */
	@Test
	public void testValidation() {
		String errorMessage = null;
		BVParameters bvParams = null;
		
		BVValidator bvValidator = new BVParameterValidator();
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVMessageUtil.getMessage("ERR0011"), "Error Messages are different.");
		
		errorMessage = null;
		bvParams = new BVParameters();
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVMessageUtil.getMessage("ERR0017"), "Error Messages are different.");
		
		bvParams.setUserAgent("google");
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVMessageUtil.getMessage("ERR0014"), "Error Messages are different.");
		
		bvParams.setSubjectId("ProductA");
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVMessageUtil.getMessage("ERR0016"), "Error Messages are different.");
		
		bvParams.setSubjectType(SubjectType.PRODUCT);
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVMessageUtil.getMessage("ERR0015"), "Error Messages are different.");
		
		bvParams.setContentType(ContentType.REVIEWS);
		/*errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0012"), "Error Messages are different.");*/
		
		/*bvParams.setBaseURI("acdd");
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0012"), "Error Messages are different.");*/
		
		/*bvParams.setBaseURI(null);
		errorMessage = null;
		try {
			bvValidator.validate(bvParams);
			fail("It should have thrown exception by now.");
		} catch (BVSdkException e) {
			errorMessage = e.getMessage();
		}
		assertSame(errorMessage, BVSdkMessageUtil.getMessage("ERR0012"), "Error Messages are different.");*/
		
	}
	
}
