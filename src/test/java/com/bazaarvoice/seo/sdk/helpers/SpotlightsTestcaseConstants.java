package com.bazaarvoice.seo.sdk.helpers;

/**
 * Class for having test case related constants for spotlights.
 *
 * This class will be used to define all constants that are meant to be used by
 * the test cases in seo_sdk_java related to spotlights.
 *
 * This class also simplifies by providing flexibility to any change in S3
 * buckets.
 *
 * Use this class to get staging configurations.
 *
 * @author anandan.narayanaswamy
 */
public class SpotlightsTestcaseConstants {

  private static final String testCloudKey = "spotlight-five-311f5a3337b8d5e0d817adb7af279b0a";
  private static final String testRootFolder = "Main_Site-en_US";
  private static final String testProduct_1 = "category-3";

  /**
   * Returns the test client's cloud key that is defined in
   * SpotlightsTestcaseConstants.testCoudKey. Currently this is set to
   * spotlight-five-311f5a3337b8d5e0d817adb7af279b0a.
   *
   * @return testCloudKey
   */
  public static String getTestCloudKey() {
    return testCloudKey;
  }

  /**
   * Returns the test client's root folder that is defined in
   * SpotlightsTestcaseConstants.testRootFolder. Currently this is set to
   * CategoryPilot-en_US.
   *
   * @return testRootFolder
   */
  public static String getTestRootFolder() {
    return testRootFolder;
  }

  /**
   * Returns the test client's test product as defined in
   * SpotlightsTestcaseConstants.testProduct_1. Currently this value is
   * category-3.
   *
   * @return testProduct_1
   */
  public static String getTestProduct_1() {
    return testProduct_1;
  }

}
