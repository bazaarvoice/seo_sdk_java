/*
 * ===========================================================================
 * Copyright 2014 Bazaarvoice, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ===========================================================================
 * 
 */

package com.bazaarvoice.seo.sdk.footer;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkURLBuilder;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkUrl;

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
		
		BVSeoSdkUrl _bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParameters);
		
		BVFooter bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
		bvFooter.setBvSeoSdkUrl(_bvSeoSdkUrl);
		
		
		String displayFooter = bvFooter.displayFooter("getContent");
		System.out.println(displayFooter);
		assertEquals(displayFooter.contains("<li id=\"mt\">bvseo-LOCAL</li>"), true, "the content string should match.");
		assertEquals(displayFooter.contains("<li id=\"ct\">bvseo-${_bvParameters.contentType}</li>"), true, "the content string should match.");
		assertEquals(displayFooter.contains("<li id=\"st\">bvseo-PRODUCT</li>"), true, "the content string should match.");
		assertEquals(displayFooter.contains("<li id=loadSEOFilesLocally>true</li>"), true, "the content string should match.");
	}
	
	/**
	 * Test case to test display footer method for reveal=debug along with URL which was a request.
	 * Rules : 
	 * display urls only when HTTP method is invoked and not for files.
	 */
	@Test
	public void testDisplayFooter_URL_debug() {
		BVConfiguration bvConfiguration = new BVSdkConfiguration();
		bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");
		
		BVParameters bvParameters = new BVParameters();
		bvParameters.setPageURI("?bvreveal=debug");
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		bvParameters.setContentType(ContentType.REVIEWS);
		
		BVSeoSdkUrl _bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParameters);
		
		BVFooter bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
		bvFooter.setBvSeoSdkUrl(_bvSeoSdkUrl);
		
		
		String displayFooter = bvFooter.displayFooter("getContent");
		System.out.println(displayFooter);
		assertEquals(displayFooter.contains("<li id=\"mt\">bvseo-CLOUD</li>"), true, "the content string should match.");
		assertEquals(displayFooter.contains("<li id=\"st\">bvseo-PRODUCT</li>"), true, "the content string should match.");
		assertEquals(displayFooter.contains("<li id=\"contentURL\">http://seo.bazaarvoice.com"), true, "the content string should match.");
		
		/*
		 * When loading from files it should not display URL.
		 */
		bvConfiguration = new BVSdkConfiguration();
		bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "true");
		
		bvParameters = new BVParameters();
		bvParameters.setPageURI("?bvreveal=debug");
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		bvParameters.setContentType(ContentType.REVIEWS);
		
		_bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParameters);
		
		bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
		bvFooter.setBvSeoSdkUrl(_bvSeoSdkUrl);
		
		
		displayFooter = bvFooter.displayFooter("getContent");
		System.out.println(displayFooter);
		assertEquals(displayFooter.contains("<li id=\"mt\">bvseo-LOCAL</li>"), true, "the content string should match.");
		assertEquals(displayFooter.contains("<li id=\"st\">bvseo-PRODUCT</li>"), true, "the content string should match.");
		assertEquals(displayFooter.contains("<li id=\"contentURL\">http://seo.bazaarvoice.com"), false, "there should not be any url pattern.");
	}
}
