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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.bazaarvoice.seo.sdk.servlet.DefaultRequestContext;
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
 * Test class for BVManagedUIContent - Inline page.
 *
 * Contains test cases most of them http driven i.e. not to load from local
 * files. The test case also ensures that it points to staging rather than
 * production.
 *
 * Read through each test case to find more detail.
 *
 * @author Anandan Narayanaswamy
 *
 */
public class BVManagedUIContent4_Test {

  private BVUIContent _bvUIContent;

  private static final String CLOUD_KEY = "myshco-3e3001e88d9c32d19a17cafacb81bec7";
  private static final String DISPLAY_CODE = "9344";

  @BeforeMethod
  public void init() {
    BVConfiguration _bvConfiguration = new BVSdkConfiguration();
    _bvConfiguration.addProperty(BVClientConfig.BV_ROOT_FOLDER, DISPLAY_CODE);
    _bvConfiguration.addProperty(BVClientConfig.CLOUD_KEY, CLOUD_KEY);
    _bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");
    _bvConfiguration.addProperty(BVClientConfig.SEO_SDK_ENABLED, "true");
    _bvConfiguration.addProperty(BVClientConfig.STAGING, "true");

    _bvUIContent = new BVManagedUIContent(_bvConfiguration);

  }

  /**
   * Test case to get Inline page review content.
   */
  @Test
  public void test_InteractivePage_Review() {
    DefaultRequestContext.initialize();
    BVParameters bvParameters = new BVParameters();
    bvParameters.setUserAgent("google");
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setSubjectId("review1");
    bvParameters.setContentType(ContentType.REVIEWS);
    bvParameters.setBaseURI("http://www.example.com/store/products/reviews");
    bvParameters.setPageURI("http://www.example.com/store/products/data-gen-696yl2lg1kurmqxn88fqif5y2/?utm_campaign=bazaarvoice&utm_medium=SearchVoice&utm_source=RatingsAndReviews&utm_content=Default&bvrrp=12325/reviews/product/2/product1.htm");
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
      "There should be valid content."
    );
  }

  @Test
  public void test_InlinePage_Question() {
    DefaultRequestContext.initialize();
    BVParameters bvParameters = new BVParameters();
    bvParameters.setUserAgent("google");
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setSubjectId("product1");
    bvParameters.setContentType(ContentType.QUESTIONS);
    bvParameters.setBaseURI("http://www.example.com/store/products/question");
    bvParameters.setPageURI("http://www.example.com/store/products/data-gen-696yl2lg1kurmqxn88fqif5y2/?utm_campaign=bazaarvoice&utm_medium=SearchVoice&utm_source=RatingsAndReviews&utm_content=Default&bvrrp=12325/questions/product/2/product1.htm");
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
      "There should be valid content."
    );
  }

  @Test
  public void test_InlinePage_Stories() {
    DefaultRequestContext.initialize();
    BVParameters bvParameters = new BVParameters();
    bvParameters.setUserAgent("google");
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setSubjectId("product1");
    bvParameters.setContentType(ContentType.STORIES);
    bvParameters.setBaseURI("http://www.example.com/store/products/story");
    bvParameters.setPageURI("http://www.example.com/store/products/data-gen-696yl2lg1kurmqxn88fqif5y2/?utm_campaign=bazaarvoice&utm_medium=SearchVoice&utm_source=RatingsAndReviews&utm_content=Default&bvrrp=12325/story/product/2/product1.htm");
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
      "There should be valid content."
    );
  }

  /**
   * HTTPS test case.
   *
   * @throws NoSuchAlgorithmException
   * @throws KeyManagementException
   * @throws IOException
   * @throws FileNotFoundException
   * @throws CertificateException
   * @throws KeyStoreException
   * @throws UnrecoverableKeyException
   * @throws NoSuchProviderException
   */
  @Test
  public void testSEOContentFrom_Using_HTTPS() throws NoSuchAlgorithmException, KeyManagementException, CertificateException, FileNotFoundException, IOException, KeyStoreException, UnrecoverableKeyException, NoSuchProviderException {
    DefaultRequestContext.initialize();
    BVConfiguration bvConfig = new BVSdkConfiguration();
    bvConfig.addProperty(
      BVClientConfig.SSL_ENABLED,
      "true"
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
      "60000"
    );

    BVUIContent uiContent = new BVManagedUIContent(bvConfig);

    BVParameters bvParameters = new BVParameters();
    bvParameters.setUserAgent("google");
    bvParameters.setContentType(ContentType.REVIEWS);
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setSubjectId("data-gen-5zkafmln4wymhcfbp5u6hmv5q");
    bvParameters.setPageURI("http://localhost:8080/sample/xyz.jsp");

    String theUIContent = uiContent.getContent(bvParameters);
    assertEquals(
      theUIContent.contains("<!--begin-bvseo-reviews-->"),
      true,
      "there should be BvRRSourceID in the content"
    );
  }

}
