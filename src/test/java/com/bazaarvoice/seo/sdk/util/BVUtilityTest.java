package com.bazaarvoice.seo.sdk.util;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for BVUtility.
 * @author Anandan Narayanaswamy
 *
 */
public class BVUtilityTest {

	/**
	 * Test case for testing BV Pattern
	 */
	@Test
	public void testBVPattern() {
		/*
		 * white space match with BV
		 */
		String content = "This is a valid content which has BV and let us see";
		boolean validContent = BVUtilty.validateBVContent(content);
		Assert.assertTrue(validContent);
		
		content = "This content has bV in the content";
		validContent = BVUtilty.validateBVContent(content);
		Assert.assertTrue(validContent);
		
		content = "This content has Bv in the content";
		validContent = BVUtilty.validateBVContent(content);
		Assert.assertTrue(validContent);
		
		content = "Thsi content has bv in the content";
		validContent = BVUtilty.validateBVContent(content);
		Assert.assertTrue(validContent);
		
		content = "thisisstrangeBvcontent";
		validContent = BVUtilty.validateBVContent(content);
		Assert.assertTrue(validContent);
		
		content = "corrupted";
		validContent = BVUtilty.validateBVContent(content);
		Assert.assertFalse(validContent);
		
		content = "";
		validContent = BVUtilty.validateBVContent(content);
		Assert.assertFalse(validContent);
	}
	
}
