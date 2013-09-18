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
 * Test class for testing Execution timeout settings
 * @author Anandan Narayanaswamy
 *
 */
public class BVManagedUIContent_ExecutionTimeoutTest {

	@Test
	public void testExecutionTimeout() {
		BVConfiguration bvConfig = new BVSdkConfiguration();
		bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");
		bvConfig.addProperty(BVClientConfig.CLOUD_KEY, "myshco-359c29d8a8cbe3822bc0d7c58cb9f9ca");
		bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "9344seob");
		bvConfig.addProperty(BVClientConfig.EXECUTION_TIMEOUT, "2");
		
		BVUIContent uiContent = new BVManagedUIContent(bvConfig);
		
		BVParameters bvParameters = new BVParameters();
		bvParameters.setUserAgent("google");
		bvParameters.setContentType(ContentType.REVIEWS);
		bvParameters.setSubjectType(SubjectType.PRODUCT);
		bvParameters.setSubjectId("3000001");
		
		String theUIContent = uiContent.getContent(bvParameters);
		System.out.println(theUIContent);
		assertEquals(theUIContent.contains("bvseo-msg: Execution timed out, exceeded"), true, "there should be execution timeout message");
	}
}
