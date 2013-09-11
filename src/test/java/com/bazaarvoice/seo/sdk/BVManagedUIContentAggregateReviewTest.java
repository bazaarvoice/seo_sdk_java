package com.bazaarvoice.seo.sdk;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;
import com.bazaarvoice.seo.sdk.util.BVMessageUtil;

/**
 * Test class for displaying only the aggregate reviews from the review contents
 * @author Anandan Narayanaswamy
 *
 */
public class BVManagedUIContentAggregateReviewTest {

	/**
	 * Aggregate reivew test
	 */
	@Test
	public void testSEOContentFromHTTP_SinglePagePRR_AggregateRating() {
		BVConfiguration bvConfig = new BVSdkConfiguration();
		bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");
		bvConfig.addProperty(BVClientConfig.CLOUD_KEY, "myshco-359c29d8a8cbe3822bc0d7c58cb9f9ca");
		bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "9344seob");
		
		BVUIContent uiContent = new BVManagedUIContent(bvConfig);
		
		BVParameters bvParameters = new BVParameters();
		bvParameters.setUserAgent("google");
		bvParameters.setContentType(ContentType.REVIEWS);
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		bvParameters.setSubjectId("3000001");
		
		String theUIContent = uiContent.getAggregateRating(bvParameters);
		System.out.println(theUIContent);
		assertEquals(theUIContent.contains("<span itemprop=\"aggregateRating\" itemscope itemtype=\"http://schema.org/AggregateRating\">"), 
				true, "there should be BvRRSourceID in the content");
		assertEquals(!theUIContent.contains("itemprop=\"review\" itemscope itemtype=\"http://schema.org/Review\">"), true, 
				"there should not be reviews section in the content");
	}
	
	/**
	 * reivew content test
	 */
	@Test
	public void testSEOContentFromHTTP_SinglePagePRR_Reviews() {
		BVConfiguration bvConfig = new BVSdkConfiguration();
		bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");
		bvConfig.addProperty(BVClientConfig.CLOUD_KEY, "myshco-359c29d8a8cbe3822bc0d7c58cb9f9ca");
		bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "9344seob");
		
		BVUIContent uiContent = new BVManagedUIContent(bvConfig);
		
		BVParameters bvParameters = new BVParameters();
		bvParameters.setUserAgent("google");
		bvParameters.setContentType(ContentType.REVIEWS);
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		bvParameters.setSubjectId("3000001");
		
		String theUIContent = uiContent.getReviews(bvParameters);
		System.out.println(theUIContent);
		assertEquals(theUIContent.contains("itemprop=\"review\" itemscope itemtype=\"http://schema.org/Review\">"), true, 
				"there should not be reviews section in the content");
		assertEquals(!theUIContent.contains("<span itemprop=\"aggregateRating\" itemscope itemtype=\"http://schema.org/AggregateRating\">"), 
				true, "there should be BvRRSourceID in the content");
		
	}
	
	/**
	 * Bot detection is enabled and user agent is not a search crawler pattern
	 */
	@Test
	public void testSEOContent_SinglePageHTTP_aggregateRating_BotDetectionEnabled() {
		//Establish a new BVConfiguration.  Properties within this configuration are typically set in bvconfig.properties.
		//addProperty can be used to override configurations set in bvconfig.properties.
		BVConfiguration _bvConfig = new BVSdkConfiguration();
				_bvConfig.addProperty(BVClientConfig.SEO_SDK_ENABLED, "true");  // use this as a kill switch
				_bvConfig.addProperty(BVClientConfig.BOT_DETECTION, "true"); // set to true if user agent/bot detection is desired
				
				//this SDK supports retrieval of SEO contents from the cloud or local file system
				_bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false"); // set to false if using cloud-based content
				//if LOAD_SEO_FILES_LOCALLY = false, configure CLOUD_KEY and STAGING
				_bvConfig.addProperty(BVClientConfig.CLOUD_KEY,
		"adobe-55d020998d7b4776fb0f9df49278083c"); // get this value from BV
				_bvConfig.addProperty(BVClientConfig.STAGING, "false");  // set to true for staging environment data
				
				//insert root folder with the value provided.
				//if multiple deployment zones/display codes are used for this implementation, use conditional logic to set the appropriate BV_ROOT_FOLDER
				_bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "8814"); //get this value from BV				

		//Create BVParameters for each injection.  If the page contains multiple injections, for example //reviews and questions, set unique parameters for each injection.
		BVParameters _bvParam = new BVParameters();
				_bvParam.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_3) AppleWebKit/536.29.13 (KHTML, like Gecko) Version/6.0.4 Safari/536.29.13");
				_bvParam.setBaseURI("/thispage.htm"); // this value is used to build pagination links
				_bvParam.setPageURI("http://localhost:8080/abcd" + "?" + "notSure=1&letSee=2"); //this value is used to needed BV URL parameters
				_bvParam.setContentType(ContentType.REVIEWS);
				_bvParam.setSubjectType(SubjectType.PRODUCT);
				_bvParam.setSubjectId("PR6");

		BVUIContent _bvOutput = new BVManagedUIContent(_bvConfig);
				

		String sBvOutputSummary = _bvOutput.getAggregateRating(_bvParam);
		System.out.println(sBvOutputSummary);
		assertEquals(!sBvOutputSummary.contains("<span itemprop=\"aggregateRating\" itemscope itemtype=\"http://schema.org/AggregateRating\">"), 
				true, "there should be BvRRSourceID in the content");
		assertEquals(!sBvOutputSummary.contains("itemprop=\"review\" itemscope itemtype=\"http://schema.org/Review\">"), true, 
				"there should not be reviews section in the content");
		assertEquals(sBvOutputSummary.contains("JavaScript-only Display"), 
				true, "There should be JavaScript display element");
		
		String sBvOutputReviews = _bvOutput.getReviews(_bvParam);
		assertEquals(!sBvOutputReviews.contains("itemprop=\"review\" itemscope itemtype=\"http://schema.org/Review\">"), true, 
				"there should not be reviews section in the content");
		assertEquals(!sBvOutputReviews.contains("<span itemprop=\"aggregateRating\" itemscope itemtype=\"http://schema.org/AggregateRating\">"), 
				true, "there should be BvRRSourceID in the content");
		assertEquals(sBvOutputReviews.contains("JavaScript-only Display"), 
				true, "There should be JavaScript display element");

	}
	
	/**
	 * Bot detection is disabled and user agent is google but aggretate rating is not present for this client.
	 */
	@Test
	public void testSEOContent_SinglePageHTTP_aggregateRating_IfNotPresent() {
		//Establish a new BVConfiguration.  Properties within this configuration are typically set in bvconfig.properties.
		//addProperty can be used to override configurations set in bvconfig.properties.
		BVConfiguration _bvConfig = new BVSdkConfiguration();
				_bvConfig.addProperty(BVClientConfig.SEO_SDK_ENABLED, "true");  // use this as a kill switch
				_bvConfig.addProperty(BVClientConfig.BOT_DETECTION, "false"); // set to true if user agent/bot detection is desired
				
				//this SDK supports retrieval of SEO contents from the cloud or local file system
				_bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false"); // set to false if using cloud-based content
				//if LOAD_SEO_FILES_LOCALLY = false, configure CLOUD_KEY and STAGING
				_bvConfig.addProperty(BVClientConfig.CLOUD_KEY,
		"behr-947068ea1b6a311404df9025894a438b"); // get this value from BV
				_bvConfig.addProperty(BVClientConfig.STAGING, "false");  // set to true for staging environment data
				
				//insert root folder with the value provided.
				//if multiple deployment zones/display codes are used for this implementation, use conditional logic to set the appropriate BV_ROOT_FOLDER
				_bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "6794redes-en_us"); //get this value from BV				

		//Create BVParameters for each injection.  If the page contains multiple injections, for example //reviews and questions, set unique parameters for each injection.
		BVParameters _bvParam = new BVParameters();
				_bvParam.setUserAgent("google");
				_bvParam.setBaseURI("/thispage.htm"); // this value is used to build pagination links
				_bvParam.setPageURI("http://localhost:8080/abcd" + "?" + "notSure=1&letSee=2"); //this value is used to needed BV URL parameters
				_bvParam.setContentType(ContentType.REVIEWS);
				_bvParam.setSubjectType(SubjectType.PRODUCT);
				_bvParam.setSubjectId("10204080000800000-I");

		BVUIContent _bvOutput = new BVManagedUIContent(_bvConfig);
				

		String sBvOutputSummary = _bvOutput.getAggregateRating(_bvParam);
		System.out.println(sBvOutputSummary);
		assertEquals(!sBvOutputSummary.contains("<span itemprop=\"aggregateRating\" itemscope itemtype=\"http://schema.org/AggregateRating\">"), 
				true, "there should be BvRRSourceID in the content");
		assertEquals(!sBvOutputSummary.contains("itemprop=\"review\" itemscope itemtype=\"http://schema.org/Review\">"), true, 
				"there should not be reviews section in the content");
		String expectedMessage = BVMessageUtil.getMessage("ERR0003");
		assertEquals(sBvOutputSummary.contains(expectedMessage), 
				true, "Message does not contain expected message please test");
		
	}
	
	/**
	 * Aggregate reviews without the pagination link.
	 */
	@Test
	public void testAggregate_WithoutPagination() {
		//Establish a new BVConfiguration.  Properties within this configuration are typically set in bvconfig.properties.
		//addProperty can be used to override configurations set in bvconfig.properties.
		BVConfiguration _bvConfig = new BVSdkConfiguration();
				_bvConfig.addProperty(BVClientConfig.SEO_SDK_ENABLED, "true");  // use this as a kill switch
				_bvConfig.addProperty(BVClientConfig.BOT_DETECTION, "true"); // set to true if user agent/bot detection is desired
				
				//this SDK supports retrieval of SEO contents from the cloud or local file system
				_bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false"); // set to false if using cloud-based content
				//if LOAD_SEO_FILES_LOCALLY = false, configure CLOUD_KEY and STAGING
				_bvConfig.addProperty(BVClientConfig.CLOUD_KEY,
		"hartz-2605f8e4ef6790962627644cc195acf2"); // get this value from BV
				_bvConfig.addProperty(BVClientConfig.STAGING, "false");  // set to true for staging environment data
				
				//insert root folder with the value provided.
				//if multiple deployment zones/display codes are used for this implementation, use conditional logic to set the appropriate BV_ROOT_FOLDER
				_bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "11568-en_US"); //get this value from BV				

				//Create BVParameters for each injection.  If the page contains multiple injections, for example //reviews and questions, set unique parameters for each injection.
				BVParameters _bvParam = new BVParameters();
						_bvParam.setUserAgent("google");
						_bvParam.setBaseURI("/thispage.htm"); // this value is used to build pagination links
						_bvParam.setPageURI("http://localhost:8080/abcd" + "?" + "notSure=1&letSee=2"); //this value is used to needed BV URL parameters
						_bvParam.setContentType(ContentType.REVIEWS);
						_bvParam.setSubjectType(SubjectType.PRODUCT);
						_bvParam.setSubjectId("1577");

				BVUIContent _bvOutput = new BVManagedUIContent(_bvConfig);
						

				String sBvOutputSummary = _bvOutput.getAggregateRating(_bvParam);
				System.out.println(sBvOutputSummary);
				assertEquals(sBvOutputSummary.contains("<!--begin-aggregate-rating-->"), 
						true, "there should be aggregateRating in the content");
				assertEquals(!sBvOutputSummary.contains("itemprop=\"review\" itemscope itemtype=\"http://schema.org/Review\">"), true, 
						"there should not be reviews section in the content");
	}
	
	/**
	 * test case for get content when seo_sdk is disabled.
	 */
	@Test
	public void testGetContent_SDK_Disabled() {
		//Establish a new BVConfiguration.  Properties within this configuration are typically set in bvconfig.properties.
		//addProperty can be used to override configurations set in bvconfig.properties.
		BVConfiguration _bvConfig = new BVSdkConfiguration();
				_bvConfig.addProperty(BVClientConfig.SEO_SDK_ENABLED, "false");  // use this as a kill switch
				_bvConfig.addProperty(BVClientConfig.BOT_DETECTION, "true"); // set to true if user agent/bot detection is desired
				
				//this SDK supports retrieval of SEO contents from the cloud or local file system
				_bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false"); // set to false if using cloud-based content
				//if LOAD_SEO_FILES_LOCALLY = false, configure CLOUD_KEY and STAGING
				_bvConfig.addProperty(BVClientConfig.CLOUD_KEY,
		"hartz-2605f8e4ef6790962627644cc195acf2"); // get this value from BV
				_bvConfig.addProperty(BVClientConfig.STAGING, "false");  // set to true for staging environment data
				
				//insert root folder with the value provided.
				//if multiple deployment zones/display codes are used for this implementation, use conditional logic to set the appropriate BV_ROOT_FOLDER
				_bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "11568-en_US"); //get this value from BV				

				//Create BVParameters for each injection.  If the page contains multiple injections, for example //reviews and questions, set unique parameters for each injection.
				BVParameters _bvParam = new BVParameters();
						_bvParam.setUserAgent("google");
						_bvParam.setBaseURI("/thispage.htm"); // this value is used to build pagination links
						_bvParam.setPageURI("http://localhost:8080/abcd" + "?" + "notSure=1&letSee=2"); //this value is used to needed BV URL parameters
						_bvParam.setContentType(ContentType.REVIEWS);
						_bvParam.setSubjectType(SubjectType.PRODUCT);
						_bvParam.setSubjectId("1577");

				BVUIContent _bvOutput = new BVManagedUIContent(_bvConfig);
						

				String sBvOutputSummary = _bvOutput.getContent(_bvParam); 
				assertEquals(sBvOutputSummary, "", "There should be empty string");
	}
	
	/**
	 * Test case for getting Aggregate when seo_sdk is disabled.
	 */
	@Test
	public void testGetAggregateReview_SDK_Disabled() {
		//Establish a new BVConfiguration.  Properties within this configuration are typically set in bvconfig.properties.
		//addProperty can be used to override configurations set in bvconfig.properties.
		BVConfiguration _bvConfig = new BVSdkConfiguration();
				_bvConfig.addProperty(BVClientConfig.SEO_SDK_ENABLED, "false");  // use this as a kill switch
				_bvConfig.addProperty(BVClientConfig.BOT_DETECTION, "true"); // set to true if user agent/bot detection is desired
				
				//this SDK supports retrieval of SEO contents from the cloud or local file system
				_bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false"); // set to false if using cloud-based content
				//if LOAD_SEO_FILES_LOCALLY = false, configure CLOUD_KEY and STAGING
				_bvConfig.addProperty(BVClientConfig.CLOUD_KEY,
		"hartz-2605f8e4ef6790962627644cc195acf2"); // get this value from BV
				_bvConfig.addProperty(BVClientConfig.STAGING, "false");  // set to true for staging environment data
				
				//insert root folder with the value provided.
				//if multiple deployment zones/display codes are used for this implementation, use conditional logic to set the appropriate BV_ROOT_FOLDER
				_bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "11568-en_US"); //get this value from BV				

				//Create BVParameters for each injection.  If the page contains multiple injections, for example //reviews and questions, set unique parameters for each injection.
				BVParameters _bvParam = new BVParameters();
						_bvParam.setUserAgent("google");
						_bvParam.setBaseURI("/thispage.htm"); // this value is used to build pagination links
						_bvParam.setPageURI("http://localhost:8080/abcd" + "?" + "notSure=1&letSee=2"); //this value is used to needed BV URL parameters
						_bvParam.setContentType(ContentType.REVIEWS);
						_bvParam.setSubjectType(SubjectType.PRODUCT);
						_bvParam.setSubjectId("1577");

				BVUIContent _bvOutput = new BVManagedUIContent(_bvConfig);
						

				String sBvOutputSummary = _bvOutput.getAggregateRating(_bvParam); 
				assertEquals(sBvOutputSummary, "", "There should be empty string");
	}
	
	/**
	 * test case to test get reviews when seo_sdk is disabled.
	 */
	@Test
	public void testGetReview_SDK_Disabled() {
		//Establish a new BVConfiguration.  Properties within this configuration are typically set in bvconfig.properties.
		//addProperty can be used to override configurations set in bvconfig.properties.
		BVConfiguration _bvConfig = new BVSdkConfiguration();
				_bvConfig.addProperty(BVClientConfig.SEO_SDK_ENABLED, "false");  // use this as a kill switch
				_bvConfig.addProperty(BVClientConfig.BOT_DETECTION, "true"); // set to true if user agent/bot detection is desired
				
				//this SDK supports retrieval of SEO contents from the cloud or local file system
				_bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false"); // set to false if using cloud-based content
				//if LOAD_SEO_FILES_LOCALLY = false, configure CLOUD_KEY and STAGING
				_bvConfig.addProperty(BVClientConfig.CLOUD_KEY,
		"hartz-2605f8e4ef6790962627644cc195acf2"); // get this value from BV
				_bvConfig.addProperty(BVClientConfig.STAGING, "false");  // set to true for staging environment data
				
				//insert root folder with the value provided.
				//if multiple deployment zones/display codes are used for this implementation, use conditional logic to set the appropriate BV_ROOT_FOLDER
				_bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "11568-en_US"); //get this value from BV				

				//Create BVParameters for each injection.  If the page contains multiple injections, for example //reviews and questions, set unique parameters for each injection.
				BVParameters _bvParam = new BVParameters();
						_bvParam.setUserAgent("google");
						_bvParam.setBaseURI("/thispage.htm"); // this value is used to build pagination links
						_bvParam.setPageURI("http://localhost:8080/abcd" + "?" + "notSure=1&letSee=2"); //this value is used to needed BV URL parameters
						_bvParam.setContentType(ContentType.REVIEWS);
						_bvParam.setSubjectType(SubjectType.PRODUCT);
						_bvParam.setSubjectId("1577");

				BVUIContent _bvOutput = new BVManagedUIContent(_bvConfig);
						

				String sBvOutputSummary = _bvOutput.getReviews(_bvParam); 
				assertEquals(sBvOutputSummary, "", "There should be empty string");
	}
	
	/**
	 * Crawler patter change as user will enter any pattern and will be straight
	 * string. if they are using separator,  they will be using '|' to separte words.
	 */
	@Test
	public void testSEOContentFromHTTP_SinglePagePRR_CrawlerOverride() {
		BVConfiguration bvConfig = new BVSdkConfiguration();
		bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");
		bvConfig.addProperty(BVClientConfig.CLOUD_KEY, "myshco-359c29d8a8cbe3822bc0d7c58cb9f9ca");
		bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "9344seob");
		bvConfig.addProperty(BVClientConfig.CRAWLER_AGENT_PATTERN, "mysearchbot");
		
		BVUIContent uiContent = new BVManagedUIContent(bvConfig);
		
		BVParameters bvParameters = new BVParameters();
		bvParameters.setUserAgent("111mysearchbot122");
		bvParameters.setContentType(ContentType.REVIEWS);
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		bvParameters.setSubjectId("3000001");
		
		String theUIContent = uiContent.getAggregateRating(bvParameters);
		System.out.println(theUIContent);
		assertEquals(theUIContent.contains("<span itemprop=\"aggregateRating\" itemscope itemtype=\"http://schema.org/AggregateRating\">"), 
				true, "there should be BvRRSourceID in the content");
		assertEquals(!theUIContent.contains("itemprop=\"review\" itemscope itemtype=\"http://schema.org/Review\">"), true, 
				"there should not be reviews section in the content");
		
		/*
		 * Scenario for multiple crawler patterh
		 */
		bvConfig = new BVSdkConfiguration();
		bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");
		bvConfig.addProperty(BVClientConfig.CLOUD_KEY, "myshco-359c29d8a8cbe3822bc0d7c58cb9f9ca");
		bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "9344seob");
		bvConfig.addProperty(BVClientConfig.CRAWLER_AGENT_PATTERN, "mysearchbot|anotherbot");
		
		uiContent = new BVManagedUIContent(bvConfig);
		
		bvParameters = new BVParameters();
		bvParameters.setUserAgent("111mysearchbot122");
		bvParameters.setContentType(ContentType.REVIEWS);
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		bvParameters.setSubjectId("3000001");
		
		theUIContent = uiContent.getAggregateRating(bvParameters);
		System.out.println(theUIContent);
		assertEquals(theUIContent.contains("<span itemprop=\"aggregateRating\" itemscope itemtype=\"http://schema.org/AggregateRating\">"), 
				true, "there should be BvRRSourceID in the content");
		assertEquals(!theUIContent.contains("itemprop=\"review\" itemscope itemtype=\"http://schema.org/Review\">"), true, 
				"there should not be reviews section in the content");
		
		bvParameters = new BVParameters();
		bvParameters.setUserAgent("111anotherbot122");
		bvParameters.setContentType(ContentType.REVIEWS);
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		bvParameters.setSubjectId("3000001");
		
		theUIContent = uiContent.getAggregateRating(bvParameters);
		System.out.println(theUIContent);
		assertEquals(theUIContent.contains("<span itemprop=\"aggregateRating\" itemscope itemtype=\"http://schema.org/AggregateRating\">"), 
				true, "there should be BvRRSourceID in the content");
		assertEquals(!theUIContent.contains("itemprop=\"review\" itemscope itemtype=\"http://schema.org/Review\">"), true, 
				"there should not be reviews section in the content");
	}
}
