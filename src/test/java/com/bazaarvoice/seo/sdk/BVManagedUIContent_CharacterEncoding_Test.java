/*
 * ===========================================================================
 * Copyright 2014 Bazaarvoice, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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

import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.helpers.SeoTestcaseConstants;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;

/**
 * Test class for BVManagedUIContent - Inline page.
 *
 * Contains test cases most of them http driven i.e. not to load from local
 * files. The test case also ensures that it points to staging rather than
 * production.
 *
 * Read through each test case to find more detail.
 *
 * @author Anandan Narayanaswamy
 */
public class BVManagedUIContent_CharacterEncoding_Test {

  private static final String CLOUD_KEY_JAPANESE = "watanabe-3b69d1e993a2361cea6f695b062279fa";
  private static final String DISPLAY_CODE_JAPANESE = "Main_Site-ja_JP";

  /**
   * Test UTF encoding default value.
   */
  @Test
  public void testDefaultUTF_8_Review() {
    BVUIContent _bvUIContent = getUTF8Configuration();

    BVParameters bvParameters = new BVParameters();
    bvParameters.setUserAgent("google");
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setSubjectId("0101707101");
    bvParameters.setContentType(ContentType.REVIEWS);
    bvParameters.setBaseURI("http://www.example.com/store/products/reviews");
    bvParameters.setPageURI("http://www.example.com/store/products/data-gen-696yl2lg1kurmqxn88fqif5y2/?utm_campaign=bazaarvoice&utm_medium=SearchVoice&utm_source=RatingsAndReviews&utm_content=Default&bvrrp=Main_Site-en_GB/reviews/product/1/0101707101.htm&bvreveal=debug");
    String errorMessage = null;
    String content = null;

    try {
      content = _bvUIContent.getContent(bvParameters);
    } catch (BVSdkException e) {
      errorMessage = e.getMessage();
    }

    assertNull(errorMessage, "There should not be any errorMessage");
    assertNotNull(content, "There should be content to proceed further.");
    assertFalse(
      content.contains("HTTP 403 Forbidden"),
      "There should be valid content"
    );
  }

  /**
   * Test UTF encoding default value.
   */
  @Test
  public void testDefault_Shift_JIS_Review() {

    BVUIContent _bvUIContent = getJIS_EcodingConfiguration();

    BVParameters bvParameters = new BVParameters();
    bvParameters.setUserAgent("google");
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setSubjectId("13989");
    bvParameters.setContentType(ContentType.REVIEWS);
    bvParameters.setBaseURI("http://www.example.com/store/products/reviews");
    String errorMessage = null;
    String content = null;

    try {
      content = _bvUIContent.getContent(bvParameters);
    } catch (BVSdkException e) {
      errorMessage = e.getMessage();
    }

    assertNull(errorMessage, "There should not be any errorMessage");
    assertNotNull(
      content,
      "There should be content to proceed further."
    );
    assertFalse(
      content.contains("HTTP 403 Forbidden"),
      "There should be valid content"
    );
  }

  private BVUIContent getUTF8Configuration() {
    BVConfiguration _bvConfiguration = new BVSdkConfiguration();
    _bvConfiguration.addProperty(
      BVClientConfig.BV_ROOT_FOLDER,
      SeoTestcaseConstants.getTestRootFolder()
    );
    _bvConfiguration.addProperty(
      BVClientConfig.CLOUD_KEY,
      SeoTestcaseConstants.getTestCloudKey()
    );
    _bvConfiguration.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "false"
    );
    _bvConfiguration.addProperty(
      BVClientConfig.SEO_SDK_ENABLED,
      "true"
    );
    _bvConfiguration.addProperty(
      BVClientConfig.STAGING,
      "true"
    );

    return new BVManagedUIContent(_bvConfiguration);
  }

  private BVUIContent getJIS_EcodingConfiguration() {
    BVConfiguration _bvConfiguration = new BVSdkConfiguration();
    _bvConfiguration.addProperty(
      BVClientConfig.BV_ROOT_FOLDER,
      DISPLAY_CODE_JAPANESE
    );
    _bvConfiguration.addProperty(
      BVClientConfig.CLOUD_KEY,
      CLOUD_KEY_JAPANESE
    );
    _bvConfiguration.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "false"
    );
    _bvConfiguration.addProperty(
      BVClientConfig.SEO_SDK_ENABLED,
      "true"
    );
    _bvConfiguration.addProperty(
      BVClientConfig.STAGING,
      "true"
    );
    _bvConfiguration.addProperty(
      BVClientConfig.EXECUTION_TIMEOUT,
      "60000"
    );
    _bvConfiguration.addProperty(
      BVClientConfig.CHARSET,
      "Shift-JIS"
    );

    return new BVManagedUIContent(_bvConfiguration);
  }

}
