package com.bazaarvoice.seo.sdk;

import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;

/**
 * Test class to test Stories.
 * @author Anandan Narayanaswam
 *
 */
public class BVManagedUIContent_StoriesTest {

	@Test
	public void storyTest() {
		BVConfiguration bvConfiguration = new BVSdkConfiguration();
		bvConfiguration.addProperty(BVClientConfig.BOT_DETECTION, "true");
		bvConfiguration.addProperty(BVClientConfig.BV_ROOT_FOLDER, "9632");
		bvConfiguration.addProperty(BVClientConfig.CLOUD_KEY, "hartford-35ea8ab0dae7395fb183b14c0e6f3473");
		
		BVParameters bvParameters = new BVParameters();
		bvParameters.setContentType(ContentType.STORIES);
		bvParameters.setSubjectId("LongTermDisability");
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		bvParameters.setUserAgent("google");
		bvParameters.setPageURI("?bvreveal=debug");
		
		BVUIContent bvUIContent = new BVManagedUIContent(bvConfiguration);
		String theContent = bvUIContent.getContent(bvParameters);
		System.out.println(theContent);
	}
	
	
}
