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

import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;

/**
 * Test class to test pagination.
 * @author Anandan Narayanaswam
 *
 */
public class BVManagedUIContent_PaginationTest {

	@Test
	public void testPagination() {
		//Establish a new BVConfiguration.  Properties within this configuration are typically set in bvconfig.properties.
		//addProperty can be used to override configurations set in bvconfig.properties.
		BVConfiguration _bvConfig = new BVSdkConfiguration();
				_bvConfig.addProperty(BVClientConfig.SEO_SDK_ENABLED, "true");  // use this as a kill switch
				_bvConfig.addProperty(BVClientConfig.BOT_DETECTION, "true"); // set to true if user agent/bot detection is desired
				
				//this SDK supports retrieval of SEO contents from the cloud or local file system
				_bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false"); // set to false if using cloud-based content
				_bvConfig.addProperty(BVClientConfig.LOCAL_SEO_FILE_ROOT, "/");
				//if LOAD_SEO_FILES_LOCALLY = false, configure CLOUD_KEY and STAGING
				_bvConfig.addProperty(BVClientConfig.CLOUD_KEY,
		"adobe-55d020998d7b4776fb0f9df49278083c"); // get this value from BV
				_bvConfig.addProperty(BVClientConfig.STAGING, "false");  // set to true for staging environment data
				_bvConfig.addProperty(BVClientConfig.CRAWLER_AGENT_PATTERN, "yandex");
				
				//insert root folder with the value provided.
				//if multiple deployment zones/display codes are used for this implementation, use conditional logic to set the appropriate BV_ROOT_FOLDER
				_bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "8814"); //get this value from BV
				

		//Create BVParameters for each injection.  If the page contains multiple injections, for example //reviews and questions, set unique parameters for each injection.
		BVParameters _bvParam = new BVParameters();
				_bvParam.setUserAgent("yandex");
				_bvParam.setBaseURI("Example-Adobe.jsp"); // this value is pagination links
				_bvParam.setPageURI("http://localhost:8080/Sample/Example-Adobe.jsp?bvrrp=8814/reviews/product/4/PR6.htm&bvreveal=debug"); //this should be the current page, full URL
				_bvParam.setContentType(ContentType.REVIEWS);
				_bvParam.setSubjectType(SubjectType.PRODUCT);
				_bvParam.setSubjectId("PR6");

		BVUIContent _bvOutput = new BVManagedUIContent(_bvConfig);
				
		String sBvOutputReviews = _bvOutput.getContent(_bvParam); //String sBvOutputSummary = _bvOutput.getAggregateRating(_bvParam);
		System.out.println(sBvOutputReviews);
		//String sBvOutputReviews = _bvOutput.getReviews(_bvParam);

	}
}
