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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

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
		bvParameters.setContentType(ContentType.REVIEWS);
		
		BVFooter bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
		String displayFooter = bvFooter.displayFooter("getContent");
		
		StringBuilder expectedFooterPattern = new StringBuilder();
		expectedFooterPattern.append("\\Q<ul id=\"BVSEOSDK_meta\" style=\"display:"
				+ "none !important\">\\E")
		.append("\\s\\Q<li data-bvseo=\"sdk\">bvseo_sdk, java_sdk, bvseo-\\E\\d.\\d.\\d.\\d"
				+ "\\Q</li>\\E")
		.append("\\s\\Q<li data-bvseo=\"sp_mt\">getContent, method:CLOUD, 0ms</li>\\E")
		.append("\\s\\Q<li data-bvseo=\"ct_st\">REVIEWS, PRODUCT</li>\\E")
		.append("\\Q</ul>\\E");
		displayFooter = displayFooter.replaceAll("\\n", "");
//		System.out.println(displayFooter);
//		System.out.println(expectedFooterPattern.toString());
		assertTrue(displayFooter.matches(expectedFooterPattern.toString()), 
				"The contents are changed either fix expectedFooterPattern or "
				+ "displayFooter");
		
	}
	
	/**
	 * Test case to test display footer method for reveal=debug.
	 */
	@Test
	public void testDisplayFooter_debug() {
		
		BVConfiguration bvConfiguration = new BVSdkConfiguration();
		bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "true");
		
		BVParameters bvParameters = new BVParameters();
		bvParameters.setContentType(ContentType.REVIEWS);
		bvParameters.setPageURI("?bvreveal=debug");
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		bvParameters.setSubjectId("12345");
		
		BVSeoSdkUrl _bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParameters);
		
		BVFooter bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
		bvFooter.setBvSeoSdkUrl(_bvSeoSdkUrl);
		
		String displayFooter = bvFooter.displayFooter("getContent");
//		System.out.println(displayFooter);
		String expectedFooterPattern = getDbgFtrPtrn();
		
		displayFooter = displayFooter.replaceAll("\\n", "");
//		System.out.println(displayFooter);
//		System.out.println(expectedFooterPattern);
		assertTrue(displayFooter.matches(expectedFooterPattern), 
				"The contents are changed either fix expectedFooterPattern or "
				+ "displayFooter");
		
	}
	
	/**
	 * Test case to test display footer method for reveal=debug with URL.
	 * Rules : 
	 * display URLs only when HTTP method is invoked and not for local files.
	 */
	@Test
	public void testDisplayFooter_URL_debug() {
		
		BVConfiguration bvConfiguration = new BVSdkConfiguration();
		bvConfiguration.addProperty(BVClientConfig.CLOUD_KEY, "cloud123");
		bvConfiguration.addProperty(BVClientConfig.BV_ROOT_FOLDER, "bvRootFolder");
		bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");
		
		BVParameters bvParameters = new BVParameters();
		bvParameters.setPageURI("?bvreveal=debug");
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		bvParameters.setContentType(ContentType.REVIEWS);
		bvParameters.setSubjectId("product1");
		
		BVSeoSdkUrl _bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParameters);
		
		BVFooter bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
		bvFooter.setBvSeoSdkUrl(_bvSeoSdkUrl);
		
		String displayFooter = bvFooter.displayFooter("getContent");
//		System.out.println(displayFooter);
		displayFooter = displayFooter.replaceAll("\n", "");
		
		
		
		String expectedUrlPattern = ".*(\\s\\Q\\E<li data-bvseo=\"contentURL\">htt"
				+ "p://seo.bazaarvoice.com/cloud123/bvRootFolder/reviews/produc"
				+ "t/1/product1.htm</li>\\Q\\E).*";
		String expectedContentTagPtrn = ".*(data-bvseo=\"contentURL\").*";
		
		assertTrue(displayFooter.matches(expectedUrlPattern), 
				"The contents are changed either fix expectedUrlPattern or "
				+ "displayFooter");
		assertTrue(displayFooter.matches(expectedContentTagPtrn), 
				"The contents are changed either fix expectedContentTagPtrn or "
				+ "displayFooter");
		
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
		displayFooter = displayFooter.replaceAll("\n", "");
		
//		System.out.println(displayFooter);
		
		expectedContentTagPtrn = ".*(data-bvseo=\"contentURL\").*";
		
		assertFalse(displayFooter.matches(expectedContentTagPtrn), 
				"The contents are changed either fix expectedContentTagPtrn or "
				+ "displayFooter");
		
	}
	
	private String getDbgFtrPtrn() {
		
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("\\Q<ul id=\"BVSEOSDK_meta\" style=\"display:none !important\">\\E")
                .append("\\s\\Q<li data-bvseo=\"sdk\">bvseo_sdk, java_sdk, bvseo-\\E\\d.\\d.\\d.\\d\\Q</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"sp_mt\">getContent, method:LOCAL, 0ms</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"ct_st\">REVIEWS, PRODUCT</li>\\E")
                .append("\\Q</ul>\\E")
                .append("\\Q<ul id=\"BVSEOSDK_meta_debug\" style=\"display:none !important\">\\E")
                .append("\\s\\Q<li data-bvseo=\"seo.sdk.enabled\">true</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"cloudKey\">${config.value}</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"loadSEOFilesLocally\">true</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"staging\">false</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"includeDisplayIntegrationCode\">${config.value}</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"seo.sdk.ssl.enabled\">${config.value}</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"stagingS3Hostname\">seo-stg.bazaarvoice.com</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"seo.sdk.execution.timeout\">500</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"crawlerAgentPattern\">msnbot|google|teoma|bingbot|yandexbot|yahoo</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"seo.sdk.charset\">${config.value}</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"bv.root.folder\">${config.value}</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"localSEOFileRoot\">${config.value}</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"seo.sdk.http.proxy.host\">none</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"connectTimeout\">2000</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"seo.sdk.http.proxy.port\">0</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"socketTimeout\">2000</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"seo.sdk.execution.timeout.bot\">2000</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"productionS3Hostname\">seo.bazaarvoice.com</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"userAgent\">${_bvParameters.userAgent}</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"baseURI\">${_bvParameters.baseURI}</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"pageURI\">?bvreveal=debug</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"subjectId\">12345</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"contentType\">REVIEWS</li>\\E")
                .append("\\s\\Q<li data-bvseo=\"subjectType\">PRODUCT</li>\\E")
                .append("\\s\\Q</ul>\\E");
		
		return sBuilder.toString();
		
	}
	
}
