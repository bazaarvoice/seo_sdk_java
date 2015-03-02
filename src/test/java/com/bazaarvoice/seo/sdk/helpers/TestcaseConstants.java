package com.bazaarvoice.seo.sdk.helpers;


/**
 * Class for having test case related constants.
 * This class will be used to define all constants
 * that are meant to be used by the test cases in seo_sdk_java.
 * 
 * This class also simplifies by providing flexibility to any change in
 * S3 buckets.
 * 
 * Use this class to get staging configurations.
 * 
 * @author anandan.narayanaswamy
 *
 */
public class TestcaseConstants {

	private static String testCloudKey = "myshco-126b543c32d9079f120a575ece25bad6";
	private static String testRootFolder = "9344ia";
	private static String testReviewProduct_1 = "5000001";
	
	/**
	 * Returns the test client that is defined in TestcaseConstants.testClient
	 * Currently this is set to myshco-126b543c32d9079f120a575ece25bad6
	 * 
	 * @return testClient
	 */
	public static String getTestCloudKey() {
		return testCloudKey;
	}
	
	/**
	 * Returns the test client that is defined in TestcaseConstants.testRootFolder
	 * Currently this is set to 9344ia
	 * 
	 * @return testRootFolder
	 */
	public static String getTestRootFolder() {
		return testRootFolder;
	}
	
	/**
	 * Returns the test client that is defined in 
	 *  TestcaseConstants.testReviewProduct_1.
	 *  Currently this value is 5000001 and has ~7 pages.
	 *  
	 * @return testReviewProduct_1
	 */
	public static String getTestReviewProduct_1() {
		return testReviewProduct_1;
	}
	
}
