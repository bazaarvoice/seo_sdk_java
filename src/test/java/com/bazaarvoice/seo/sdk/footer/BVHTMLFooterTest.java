package com.bazaarvoice.seo.sdk.footer;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.SubjectType;

/**
 * Test class for BVHTMLFooter.
 * @author Anandan Narayanaswamy
 *
 */
public class BVHTMLFooterTest {

	/**
	 * Test case to test display footer method plain tags. 
	 */
	@Test
	public void testDisplayFooter() {
		BVConfiguration bvConfiguration = new BVSdkConfiguration();
		BVParameters bvParameters = new BVParameters();
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		BVFooter bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
		
		String displayFooter = bvFooter.displayFooter("getContent");
		assertEquals(displayFooter.contains("<li id=\"mt\">bvseo-CLOUD</li>"), true, "the content string should match.");
		assertEquals(displayFooter.contains("<li id=\"ct\">bvseo-${_bvParameters.contentType}</li>"), true, "the content string should match.");
		assertEquals(displayFooter.contains("<li id=\"st\">bvseo-PRODUCT</li>"), true, "the content string should match.");
		assertEquals(displayFooter.contains("<li id=seo.sdk.execution.timeout>"), false, "the content string should not match.");
	}
	
	/**
	 * Test case to test display footer method for reveal=debug.
	 */
	@Test
	public void testDisplayFooter_debug() {
		BVConfiguration bvConfiguration = new BVSdkConfiguration();
		bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "true");
		
		BVParameters bvParameters = new BVParameters();
		bvParameters.setPageURI("?bvreveal=debug");
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		
		BVFooter bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
		String displayFooter = bvFooter.displayFooter("getContent");
		System.out.println(displayFooter);
		assertEquals(displayFooter.contains("<li id=\"mt\">bvseo-LOCAL</li>"), true, "the content string should match.");
		assertEquals(displayFooter.contains("<li id=\"ct\">bvseo-${_bvParameters.contentType}</li>"), true, "the content string should match.");
		assertEquals(displayFooter.contains("<li id=\"st\">bvseo-PRODUCT</li>"), true, "the content string should match.");
		assertEquals(displayFooter.contains("<li id=loadSEOFilesLocally>true</li>"), true, "the content string should match.");
	}
	
}
