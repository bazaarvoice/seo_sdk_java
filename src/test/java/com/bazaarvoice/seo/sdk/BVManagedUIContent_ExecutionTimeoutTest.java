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

import static org.testng.Assert.assertEquals;

import com.bazaarvoice.seo.sdk.servlet.DefaultRequestContext;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;

/**
 * Test class for testing Execution timeout settings.
 *
 * @author Anandan Narayanaswamy
 */
public class BVManagedUIContent_ExecutionTimeoutTest {

  /**
   * Test case for user execution timeout implementation.
   */
  @Test
  public void testExecutionTimeout() {
    DefaultRequestContext.initialize();
    BVConfiguration bvConfig = new BVSdkConfiguration();
    bvConfig.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "false"
    );
    bvConfig.addProperty(
      BVClientConfig.STAGING,
      "true"
    );
    bvConfig.addProperty(
      BVClientConfig.CLOUD_KEY,
      "agileville-78B2EF7DE83644CAB5F8C72F2D8C8491"
    );
    bvConfig.addProperty(
      BVClientConfig.BV_ROOT_FOLDER,
      "Main_Site-en_US"
    );
    bvConfig.addProperty(
      BVClientConfig.EXECUTION_TIMEOUT,
      "2"
    );

    BVUIContent uiContent = new BVManagedUIContent(bvConfig);

    BVParameters bvParameters = new BVParameters();
    bvParameters.setUserAgent("NORMAL_USER OR Browser userAgent");
    bvParameters.setContentType(ContentType.REVIEWS);
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setSubjectId("data-gen-7k694zcnd6gbnpv2v4e6mmd22");

    String theUIContent = uiContent.getContent(bvParameters);
    assertEquals(
      theUIContent.contains("getContent"),
      true,
      "there should be getContent word/message"
    );
    assertEquals(
      theUIContent.contains("bvseo-msg: Execution timed out, exceeded"),
      true,
      "there should be execution timeout message"
    );

    uiContent = new BVManagedUIContent(bvConfig);
    theUIContent = uiContent.getReviews(bvParameters);
    assertEquals(
      theUIContent.contains("getReviews"),
      true,
      "there should be getReviews word/message"
    );
    assertEquals(
      theUIContent.contains("bvseo-msg: Execution timed out, exceeded"),
      true,
      "there should be execution timeout message"
    );

    uiContent = new BVManagedUIContent(bvConfig);
    theUIContent = uiContent.getAggregateRating(bvParameters);
    assertEquals(
      theUIContent.contains("getAggregateRating"),
      true,
      "there should be getAggregateRating word/message"
    );
    assertEquals(
      theUIContent.contains("bvseo-msg: Execution timed out, exceeded"),
      true,
      "there should be execution timeout message"
    );
  }

  /**
   * Test case for user execution timeout when set to 0
   */
  @Test
  public void testExecutionTimeout_ZeroTest() {
    DefaultRequestContext.initialize();
    BVConfiguration bvConfig = new BVSdkConfiguration();
    bvConfig.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "false"
    );
    bvConfig.addProperty(
      BVClientConfig.STAGING,
      "true"
    );
    bvConfig.addProperty(
      BVClientConfig.CLOUD_KEY,
      "agileville-78B2EF7DE83644CAB5F8C72F2D8C8491"
    );
    bvConfig.addProperty(
      BVClientConfig.BV_ROOT_FOLDER,
      "Main_Site-en_US"
    );
    bvConfig.addProperty(
      BVClientConfig.EXECUTION_TIMEOUT,
      "0"
    );

    BVUIContent uiContent = new BVManagedUIContent(bvConfig);

    BVParameters bvParameters = new BVParameters();
    bvParameters.setUserAgent("NORMAL_USER OR Browser userAgent");
    bvParameters.setContentType(ContentType.REVIEWS);
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setSubjectId("data-gen-7k694zcnd6gbnpv2v4e6mmd22");

    String theUIContent = uiContent.getContent(bvParameters);
    assertEquals(
      theUIContent.contains("getContent"),
      true,
      "there should be getContent word/message"
    );
    assertEquals(
      theUIContent.contains("bvseo-msg: EXECUTION_TIMEOUT is set to 0 ms; JavaScript-only Display."),
      true,
      "there should be execution timeout message"
    );

    uiContent = new BVManagedUIContent(bvConfig);
    theUIContent = uiContent.getReviews(bvParameters);
    assertEquals(
      theUIContent.contains("getReviews"),
      true,
      "there should be getReviews word/message"
    );
    assertEquals(
      theUIContent.contains("bvseo-msg: EXECUTION_TIMEOUT is set to 0 ms; JavaScript-only Display."),
      true,
      "there should be execution timeout message"
    );

    uiContent = new BVManagedUIContent(bvConfig);
    theUIContent = uiContent.getAggregateRating(bvParameters);
    assertEquals(
      theUIContent.contains("getAggregateRating"),
      true,
      "there should be getAggregateRating word/message"
    );
    assertEquals(
      theUIContent.contains("bvseo-msg: EXECUTION_TIMEOUT is set to 0 ms; JavaScript-only Display."),
      true,
      "there should be execution timeout message"
    );
  }

  /**
   * Test case for bot execution timeout implementation.
   */
  @Test
  public void testExecutionTimeoutBot() {
    DefaultRequestContext.initialize();
    BVConfiguration bvConfig = new BVSdkConfiguration();
    bvConfig.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "false"
    );
    bvConfig.addProperty(
      BVClientConfig.STAGING,
      "true"
    );
    bvConfig.addProperty(
      BVClientConfig.CLOUD_KEY,
      "agileville-78B2EF7DE83644CAB5F8C72F2D8C8491"
    );
    bvConfig.addProperty(
      BVClientConfig.BV_ROOT_FOLDER,
      "Main_Site-en_US"
    );
    bvConfig.addProperty(
      BVClientConfig.EXECUTION_TIMEOUT_BOT,
      "2"
    );

    BVUIContent uiContent = new BVManagedUIContent(bvConfig);

    BVParameters bvParameters = new BVParameters();
    bvParameters.setUserAgent("google");
    bvParameters.setContentType(ContentType.REVIEWS);
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setSubjectId("data-gen-7k694zcnd6gbnpv2v4e6mmd22");

    String theUIContent = uiContent.getContent(bvParameters);
    assertEquals(
      theUIContent.contains("getContent"),
      true,
      "there should be getContent word/message"
    );

    uiContent = new BVManagedUIContent(bvConfig);
    theUIContent = uiContent.getReviews(bvParameters);
    assertEquals(
      theUIContent.contains("bvseo-msg: EXECUTION_TIMEOUT_BOT is less than the minimum value allowed. Minimum value of 100ms used.;"),
      true,
      "there should be execution timeout message"
    );

    uiContent = new BVManagedUIContent(bvConfig);

    theUIContent = uiContent.getAggregateRating(bvParameters);
    assertEquals(
      theUIContent.contains("bvseo-msg: EXECUTION_TIMEOUT_BOT is less than the minimum value allowed. Minimum value of 100ms used.;"),
      true,
      "there should be execution timeout message"
    );
  }

  /**
   * Test case to simulate number of threads created.
   *
   * Notice in the debug option of the eclipse that there should be thread pool
   * created and re-used. watch for number of threads created.
   */
  @Test
  public void testThreadCreation() {
    DefaultRequestContext.initialize();
    //http://seo.bazaarvoice.com.s3.amazonaws.com/myshco-126b543c32d9079f120a575ece25bad6/9344ia/reviews/product/1/3000001.htm
    for (int i = 0; i < 1000; i++) {
      BVConfiguration bvConfig = new BVSdkConfiguration();
      bvConfig.addProperty(
        BVClientConfig.LOAD_SEO_FILES_LOCALLY,
        "false"
      );
      bvConfig.addProperty(
        BVClientConfig.CLOUD_KEY,
        "myshco-359c29d8a8cbe3822bc0d7c58cb9f9ca"
      );
      bvConfig.addProperty(
        BVClientConfig.BV_ROOT_FOLDER,
        "9344seob"
      );

      BVUIContent uiContent = new BVManagedUIContent(bvConfig);

      BVParameters bvParameters = new BVParameters();
      bvParameters.setUserAgent("google");
      bvParameters.setContentType(ContentType.REVIEWS);
      bvParameters.setSubjectType(SubjectType.PRODUCT);
      bvParameters.setSubjectId("3000001");

      String theUIContent = uiContent.getContent(bvParameters);
      Assert.assertNotNull(theUIContent, "There should be contents.");
    }

  }
}
