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

package com.bazaarvoice.seo.sdk;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;

/**
 * Test class for BVManagedUIContent - Interactive Archive page.
 * Contains test cases most of them http driven i.e. not to load from local files.
 * The test case also ensures that it points to staging rather than production.
 *  
 * Read through each test case to find more detail.
 *  
 * @author Anandan Narayanaswamy
 *
 */
public class BVManagedUIContent2_Test {

	private BVUIContent _bvUIContent; 
	
	private static final String CLOUD_KEY = "myshco-126b543c32d9079f120a575ece25bad6";
	private static final String DISPLAY_CODE = "9344ia";
	
	@BeforeMethod
	public void init() {
		BVConfiguration _bvConfiguration = new BVSdkConfiguration();
		_bvConfiguration.addProperty(BVClientConfig.BV_ROOT_FOLDER, DISPLAY_CODE);
		_bvConfiguration.addProperty(BVClientConfig.BOT_DETECTION, "true");
		_bvConfiguration.addProperty(BVClientConfig.CLOUD_KEY, CLOUD_KEY);
		_bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");
		_bvConfiguration.addProperty(BVClientConfig.SEO_SDK_ENABLED, "true");
		_bvConfiguration.addProperty(BVClientConfig.STAGING, "true");
		
		_bvUIContent = new BVManagedUIContent(_bvConfiguration);
	}
	
	/**
	 * Test case to get interactive archive page review content.
	 */
	@Test
	public void test_InteractivePage_Review() {
		BVParameters bvParameters = new BVParameters();
		bvParameters.setUserAgent("google");
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		bvParameters.setSubjectId("review1");
		bvParameters.setContentType(ContentType.REVIEWS);
		bvParameters.setBaseURI("http://www.example.com/store/products/reviews");
		bvParameters.setPageURI("http://www.example.com/store/products/data-gen-696yl2lg1kurmqxn88fqif5y2/?utm_campaign=bazaarvoice&utm_medium=SearchVoice&utm_source=RatingsAndReviews&utm_content=Default&bvrrp=12325/reviews/product/2/product1.htm");
		String erroMessage = null;
		String content = null;
		try {
			content = _bvUIContent.getContent(bvParameters);
		} catch (BVSdkException e) {
			erroMessage = e.getMessage();
		}
		assertNull(erroMessage, "There should not be any errorMessage");
		assertNotNull(content, "There should be content to proceed further assertion!!");
		assertFalse(content.contains("HTTP 403 Forbidden"), "There should be valid content");
		System.out.println(content);
	}
	
	@Test
	public void test_InteractivePage_Question() {
		BVParameters bvParameters = new BVParameters();
		bvParameters.setUserAgent("google");
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		bvParameters.setSubjectId("product1");
		bvParameters.setContentType(ContentType.QUESTIONS);
		bvParameters.setBaseURI("http://www.example.com/store/products/question");
		bvParameters.setPageURI("http://www.example.com/store/products/data-gen-696yl2lg1kurmqxn88fqif5y2/?utm_campaign=bazaarvoice&utm_medium=SearchVoice&utm_source=RatingsAndReviews&utm_content=Default&bvrrp=12325/questions/product/2/product1.htm");
		String erroMessage = null;
		String content = null;
		try {
			content = _bvUIContent.getContent(bvParameters);
		} catch (BVSdkException e) {
			erroMessage = e.getMessage();
		}
		assertNull(erroMessage, "There should not be any errorMessage");
		assertNotNull(content, "There should be content to proceed further assertion!!");
		assertFalse(content.contains("HTTP 403 Forbidden"), "There should be valid content");
		System.out.println(content);
	}
	
	@Test
	public void test_InteractivePage_Stories() {
		BVParameters bvParameters = new BVParameters();
		bvParameters.setUserAgent("google");
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		bvParameters.setSubjectId("product1");
		bvParameters.setContentType(ContentType.STORIES);
		bvParameters.setBaseURI("http://www.example.com/store/products/story");
		bvParameters.setPageURI("http://www.example.com/store/products/data-gen-696yl2lg1kurmqxn88fqif5y2/?utm_campaign=bazaarvoice&utm_medium=SearchVoice&utm_source=RatingsAndReviews&utm_content=Default&bvrrp=12325/story/product/2/product1.htm");
		String erroMessage = null;
		String content = null;
		try {
			content = _bvUIContent.getContent(bvParameters);
		} catch (BVSdkException e) {
			erroMessage = e.getMessage();
		}
		assertNull(erroMessage, "There should not be any errorMessage");
		assertNotNull(content, "There should be content to proceed further assertion!!");
		assertFalse(content.contains("HTTP 403 Forbidden"), "There should be valid content");
		System.out.println(content);
	}
	
}
