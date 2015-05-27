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

package com.bazaarvoice.seo.sdk.url;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentSubType;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;

import static org.testng.Assert.assertEquals;

/**
 * Test class for BVSeoSdkURLBuilder.
 *
 * Ref. to each individual test cases for detail description / use case
 * scenarios.
 *
 * @author Anandan Narayanaswamy
 */
public class BVSeoSdkURLBuilderTest {

  private BVSeoSdkUrl bvSeoSdkUrl;
  private BVConfiguration bvConfiguration;

  @BeforeMethod
  public void setup() {
    bvConfiguration = new BVSdkConfiguration();

    bvConfiguration.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "false"
    );
    bvConfiguration.addProperty(
      BVClientConfig.CLOUD_KEY,
      "myshco-126b543c32d9079f120a575ece25bad6"
    );
    bvConfiguration.addProperty(
      BVClientConfig.LOCAL_SEO_FILE_ROOT,
      "/filePath"
    );
    bvConfiguration.addProperty(
      BVClientConfig.BV_ROOT_FOLDER,
      "6574-en_us"
    );
  }

  /**
   * PRR http url test cases.
   * Test case with various possible BaseURI and PageURI
   * that can be supplied inside bvParameters.
   * These are valid Base and PageURI and the code implementation
   * should handle all scenarios and should not throw any error or behave improperly.
   * Also note that base and pageUri can be null and we assume that user has entered
   * empty string for the same.
   */
  @Test
  public void testBase_PageUri_For_PRR_HTTP() {
    BVParameters bvParam = new BVParameters();
    bvParam.setBaseURI(null);
    bvParam.setPageURI(null);
    bvParam.setContentType(ContentType.REVIEWS);
    bvParam.setSubjectType(SubjectType.PRODUCT);
    bvParam.setSubjectId("ssl-certificates");

    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    String expectedBaseUri = "";
    String expectedQueryString = "";
    String expectedSeoContentUri = "http://seo.bazaarvoice.com/myshco-126b543c32d9079f120a575ece25bad6/6574-en_us/reviews/product/1/ssl-certificates.htm";

    String actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    String actualQueryString = bvSeoSdkUrl.queryString();
    String actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );

    /*
     * When base uri and page uri are empty.
     */
    bvParam.setBaseURI("");
    bvParam.setPageURI("");
    bvParam.setPageNumber(null);
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    expectedBaseUri = "";
    expectedQueryString = "";
    expectedSeoContentUri = "http://seo.bazaarvoice.com/myshco-126b543c32d9079f120a575ece25bad6/6574-en_us/reviews/product/1/ssl-certificates.htm";

    actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    actualQueryString = bvSeoSdkUrl.queryString();
    actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
      );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );

    /*
     * When base uri and page uri are complete urls.
     */
    bvParam.setBaseURI("http://localhost:8080/Sample/Example-1.jsp");
    bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvrrp=abcd");
    bvParam.setPageNumber(null);
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    expectedBaseUri = "http://localhost:8080/Sample/Example-1.jsp";
    expectedQueryString = "bvrrp=abcd";
    expectedSeoContentUri = "http://seo.bazaarvoice.com/myshco-126b543c32d9079f120a575ece25bad6/6574-en_us/reviews/product/1/ssl-certificates.htm";

    actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    actualQueryString = bvSeoSdkUrl.queryString();
    actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same");
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same");
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );

    /*
     * When base uri and page uri has bvrrp parameters.
     */
    bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/3/ssl-certificates.htm");
    bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/3/ssl-certificates.htm");
    bvParam.setPageNumber(null);
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    expectedBaseUri = "/sample_seo_sdk_web/scenario-2.jsp?null";
    expectedQueryString = "null&bvrrp=6574-en_us/reviews/product/3/ssl-certificates.htm";
    expectedSeoContentUri = "http://seo.bazaarvoice.com/myshco-126b543c32d9079f120a575ece25bad6/6574-en_us/reviews/product/3/ssl-certificates.htm";

    actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    actualQueryString = bvSeoSdkUrl.queryString();
    actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );

    /*
     * When base uri and page uri has bvrrp parameters.
     */
    bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/2/ssl-certificates.htm");
    bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/2/ssl-certificates.htm");
    bvParam.setPageNumber(null);
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    expectedBaseUri = "/sample_seo_sdk_web/scenario-2.jsp?null";
    expectedQueryString = "null&bvrrp=6574-en_us/reviews/product/2/ssl-certificates.htm";
    expectedSeoContentUri = "http://seo.bazaarvoice.com/myshco-126b543c32d9079f120a575ece25bad6/6574-en_us/reviews/product/2/ssl-certificates.htm";

    actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    actualQueryString = bvSeoSdkUrl.queryString();
    actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
    assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
    assertEquals(actualSeoContentUri, expectedSeoContentUri, "actual and expected seo content uri should be same");
  }

  @Test
  public void testPRR_HTTP_In_QA() {
    bvConfiguration.addProperty(BVClientConfig.STAGING, "false");
    bvConfiguration.addProperty(BVClientConfig.TESTING, "true");

    BVParameters bvParam = new BVParameters();
    bvParam.setBaseURI(null);
    bvParam.setPageURI(null);
    bvParam.setContentType(ContentType.REVIEWS);
    bvParam.setSubjectType(SubjectType.PRODUCT);
    bvParam.setSubjectId("ssl-certificates");

    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    String expectedBaseUri = "";
    String expectedQueryString = "";
    String expectedSeoContentUri = "http://seo-qa.bazaarvoice.com/myshco-126b543c32d9079f120a575ece25bad6/6574-en_us/reviews/product/1/ssl-certificates.htm";

    String actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    String actualQueryString = bvSeoSdkUrl.queryString();
    String actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
    assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
    assertEquals(actualSeoContentUri, expectedSeoContentUri, "actual and expected seo content uri should be same");
  }

  @Test
  public void testPRR_HTTP_In_QA_Staging() {
    bvConfiguration.addProperty(BVClientConfig.STAGING, "true");
    bvConfiguration.addProperty(BVClientConfig.TESTING, "true");

    BVParameters bvParam = new BVParameters();
    bvParam.setBaseURI(null);
    bvParam.setPageURI(null);
    bvParam.setContentType(ContentType.REVIEWS);
    bvParam.setSubjectType(SubjectType.PRODUCT);
    bvParam.setSubjectId("ssl-certificates");

    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    String expectedBaseUri = "";
    String expectedQueryString = "";
    String expectedSeoContentUri = "http://seo-qa-stg.bazaarvoice.com/myshco-126b543c32d9079f120a575ece25bad6/6574-en_us/reviews/product/1/ssl-certificates.htm";

    String actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    String actualQueryString = bvSeoSdkUrl.queryString();
    String actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
    assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
    assertEquals(actualSeoContentUri, expectedSeoContentUri, "actual and expected seo content uri should be same");
  }

  /**
   * PRR file url test cases.
   */
  @Test
  public void testBase_PageUri_For_PRR_File() {
    bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "true");

    BVParameters bvParam = new BVParameters();
    bvParam.setBaseURI(null);
    bvParam.setPageURI(null);
    bvParam.setContentType(ContentType.REVIEWS);
    bvParam.setSubjectType(SubjectType.PRODUCT);
    bvParam.setSubjectId("ssl-certificates");

    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    String expectedBaseUri = "";
    String expectedQueryString = "";
    String expectedSeoContentUri = "/filePath/6574-en_us/reviews/product/1/ssl-certificates.htm";

    String actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    String actualQueryString = bvSeoSdkUrl.queryString();
    String actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      new File(expectedSeoContentUri).toURI().toString(),
      "actual and expected seo content uri should be same"
    );

    /*
     * When base uri and page uri are empty.
     */
    bvParam.setBaseURI("");
    bvParam.setPageURI("");

    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    expectedBaseUri = "";
    expectedQueryString = "";
    expectedSeoContentUri = "/filePath/6574-en_us/reviews/product/1/ssl-certificates.htm";

    actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    actualQueryString = bvSeoSdkUrl.queryString();
    actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      new File(expectedSeoContentUri).toURI().toString(),
      "actual and expected seo content uri should be same"
    );

    /*
     * When base uri and page uri are complete urls.
     */
    bvParam.setBaseURI("http://localhost:8080/Sample/Example-1.jsp");
    bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvrrp=abcd");

    bvParam.setPageNumber(null);
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    expectedBaseUri = "http://localhost:8080/Sample/Example-1.jsp";
    expectedQueryString = "bvrrp=abcd";
    expectedSeoContentUri = "/filePath/6574-en_us/reviews/product/1/ssl-certificates.htm";

    actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    actualQueryString = bvSeoSdkUrl.queryString();
    actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      new File(expectedSeoContentUri).toURI().toString(),
      "actual and expected seo content uri should be same"
    );

    /*
     * When base uri and page uri has bvrrp parameters.
     */
    bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/3/ssl-certificates.htm");
    bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/3/ssl-certificates.htm");

    bvParam.setPageNumber(null);
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    expectedBaseUri = "/sample_seo_sdk_web/scenario-2.jsp?null";
    expectedQueryString = "null&bvrrp=6574-en_us/reviews/product/3/ssl-certificates.htm";
    expectedSeoContentUri = "/filePath/6574-en_us/reviews/product/3/ssl-certificates.htm";

    actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    actualQueryString = bvSeoSdkUrl.queryString();
    actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      new File(expectedSeoContentUri).toURI().toString(),
      "actual and expected seo content uri should be same"
    );

    /*
     * When base uri and page uri has bvrrp parameters.
     */
    bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/2/ssl-certificates.htm");
    bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/2/ssl-certificates.htm");
    bvParam.setPageNumber(null);
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    expectedBaseUri = "/sample_seo_sdk_web/scenario-2.jsp?null";
    expectedQueryString = "null&bvrrp=6574-en_us/reviews/product/2/ssl-certificates.htm";
    expectedSeoContentUri = "/filePath/6574-en_us/reviews/product/2/ssl-certificates.htm";

    actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    actualQueryString = bvSeoSdkUrl.queryString();
    actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      new File(expectedSeoContentUri).toURI().toString(),
      "actual and expected seo content uri should be same"
    );
  }

  /**
   * C2013 http url test cases.
   *
   * If pageUri contains bvpage param then it is a valid C2013 implementation
   * anything apart from that is PRR implementation.
   *
   * @throws UnsupportedEncodingException
   */
  @Test
  public void testUri_For_C2013_HTTP() throws UnsupportedEncodingException {
    bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");

    BVParameters bvParam = new BVParameters();
    bvParam.setBaseURI("http://localhost:8080/Sample/Example-1.jsp");
    bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvpage=pg2/ctrp/stp");
    bvParam.setContentType(ContentType.REVIEWS);
    bvParam.setSubjectType(SubjectType.PRODUCT);
    bvParam.setSubjectId("ssl-certificates");

    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    String expectedBaseUri = "http://localhost:8080/Sample/Example-1.jsp";
    String expectedQueryString = "bvpage=pg2/ctrp/stp";
    String expectedSeoContentUri = "http://seo.bazaarvoice.com/myshco-126b543c32d9079f120a575ece25bad6/6574-en_us/reviewspage/product/2/ssl-certificates.htm";

    String actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    String actualQueryString = bvSeoSdkUrl.queryString();
    String actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );

    /*
     * When base uri and page uri has bvpage parameters and the subjectid is
     * part of bvpage. Also base uri has different page number and subjectid.
     * Also the ct is reviews.
     */
    bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvpage=pg2/ctrp/stp/iddogfood");
    bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvpage=pg3/ctre/stp/idcatfood");
    bvParam.setPageNumber(null);
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    expectedBaseUri = "/sample_seo_sdk_web/scenario-2.jsp?null";
    expectedQueryString = "null&bvpage=pg3/ctre/stp/idcatfood";
    expectedSeoContentUri = "http://seo.bazaarvoice.com/myshco-126b543c32d9079f120a575ece25bad6/6574-en_us/reviews/product/3/catfood.htm";

    actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    actualQueryString = bvSeoSdkUrl.queryString();
    actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );

    /*
     * When base uri and page uri has bvpage parameters but not a fully qualified url.
     * also the subjectId is part of bvParam and not in the bvpage.
     */
    bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvpage=pg3/ctre/stp/idcatfood");
    bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvpage=pg4/ctrp/stp");
    bvParam.setSubjectId("p5543");
    bvParam.setPageNumber(null);
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    expectedBaseUri = "/sample_seo_sdk_web/scenario-2.jsp?null";
    expectedQueryString = "null&bvpage=pg4/ctrp/stp";
    expectedSeoContentUri = "http://seo.bazaarvoice.com/myshco-126b543c32d9079f120a575ece25bad6/6574-en_us/reviewspage/product/4/p5543.htm";

    actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    actualQueryString = bvSeoSdkUrl.queryString();
    actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );

    /*
     * reviews category roll-up (future product), page 2, subject ID = c8765
     */
    bvParam.setBaseURI(null);
    bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvpage=pg2/ctre/stc");
    bvParam.setSubjectId("c8765");
    bvParam.setPageNumber(null);
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    expectedBaseUri = "";
    expectedQueryString = "null&bvpage=pg2/ctre/stc";
    expectedSeoContentUri = "http://seo.bazaarvoice.com/myshco-126b543c32d9079f120a575ece25bad6/6574-en_us/reviews/category/2/c8765.htm";

    actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    actualQueryString = bvSeoSdkUrl.queryString();
    actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );

    /*
     * questions/answers detail page, question ID = q45677
     */
    bvParam.setBaseURI(null);
    bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvpage=ctqa/std/id45677");
    bvParam.setSubjectId("c8765");
    bvParam.setPageNumber(null);
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    expectedBaseUri = "";
    expectedQueryString = "null&bvpage=ctqa/std/id45677";
    expectedSeoContentUri = "http://seo.bazaarvoice.com/myshco-126b543c32d9079f120a575ece25bad6/6574-en_us/questions/detail/1/45677.htm";

    actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    actualQueryString = bvSeoSdkUrl.queryString();
    actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );

    /*
     * entry/landing page (future product), id = myshirtspage
     */
    bvParam.setBaseURI(null);
    bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvpage=ctun/ste/idmyshirtspage");
    bvParam.setSubjectId(null);
    bvParam.setPageNumber(null);
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    expectedBaseUri = "";
    expectedQueryString = "null&bvpage=ctun/ste/idmyshirtspage";
    expectedSeoContentUri = "http://seo.bazaarvoice.com/myshco-126b543c32d9079f120a575ece25bad6/6574-en_us/universal/entry/1/myshirtspage.htm";

    actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    actualQueryString = bvSeoSdkUrl.queryString();
    actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );

  }

  /**
   * C2013 file url test cases.
   * Test case to only check if the file url is pointing correctly.
   */
  @Test
  public void testUri_For_C2013_File() {
    bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "true");

    BVParameters bvParam = new BVParameters();
    bvParam.setBaseURI("http://localhost:8080/Sample/Example-1.jsp");
    bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvpage=pg2/ctrp/stp");
    bvParam.setContentType(ContentType.REVIEWS);
    bvParam.setSubjectType(SubjectType.PRODUCT);
    bvParam.setSubjectId("ssl-certificates");

    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    String expectedBaseUri = "http://localhost:8080/Sample/Example-1.jsp";
    String expectedQueryString = "bvpage=pg2/ctrp/stp";
    String expectedSeoContentUri = new File("/filePath/6574-en_us/reviewspage/product/2/ssl-certificates.htm").toURI().toString();

    String actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    String actualQueryString = bvSeoSdkUrl.queryString();
    String actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );
  }

  @Test
  public void storyTest_default() {
    bvConfiguration.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "true"
    );

    BVParameters bvParam = new BVParameters();
    bvParam.setBaseURI("http://localhost:8080/Sample/Example-1.jsp");
    bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp");
    bvParam.setContentType(ContentType.STORIES);
    bvParam.setSubjectType(SubjectType.PRODUCT);

    bvParam.setSubjectId("ssl-certificates");

    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    String expectedBaseUri = "http://localhost:8080/Sample/Example-1.jsp";
    String expectedQueryString = null;
    String expectedSeoContentUri = new File(
        "/filePath/6574-en_us/stories/product/1/ssl-certificates.htm"
    ).toURI().toString();

    String actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    String actualQueryString = bvSeoSdkUrl.queryString();
    String actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );
  }

  @Test
  public void storyTest_default_none() {
    bvConfiguration.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "true"
    );

    BVParameters bvParam = new BVParameters();
    bvParam.setBaseURI("http://localhost:8080/Sample/Example-1.jsp");
    bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp");
    bvParam.setContentType(ContentType.STORIES);
    bvParam.setSubjectType(SubjectType.PRODUCT);
    bvParam.setContentSubType(ContentSubType.NONE);
    bvParam.setSubjectId("ssl-certificates");

    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    String expectedBaseUri = "http://localhost:8080/Sample/Example-1.jsp";
    String expectedQueryString = null;
    String expectedSeoContentUri = new File(
      "/filePath/6574-en_us/stories/product/1/ssl-certificates.htm"
    ).toURI().toString();

    String actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    String actualQueryString = bvSeoSdkUrl.queryString();
    String actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );
  }

  @Test
  public void storyTest_stories() {
    bvConfiguration.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "true"
    );

    BVParameters bvParam = new BVParameters();
    bvParam.setBaseURI("http://localhost:8080/Sample/Example-1.jsp");
    bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp");
    bvParam.setContentType(ContentType.STORIES);
    bvParam.setSubjectType(SubjectType.PRODUCT);
    bvParam.setContentSubType(ContentSubType.STORIES_LIST);
    bvParam.setSubjectId("ssl-certificates");

    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    String expectedBaseUri = "http://localhost:8080/Sample/Example-1.jsp";
    String expectedQueryString = null;
    String expectedSeoContentUri = new File(
      "/filePath/6574-en_us/stories/product/1/stories/ssl-certificates.htm"
    ).toURI().toString();

    String actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    String actualQueryString = bvSeoSdkUrl.queryString();
    String actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );
  }

  @Test
  public void storyTest_storiesGrid() {
    bvConfiguration.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "true"
    );

    BVParameters bvParam = new BVParameters();
    bvParam.setBaseURI("http://localhost:8080/Sample/Example-1.jsp");
    bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp");
    bvParam.setContentType(ContentType.STORIES);
    bvParam.setSubjectType(SubjectType.PRODUCT);
    bvParam.setContentSubType(ContentSubType.STORIES_GRID);
    bvParam.setSubjectId("ssl-certificates");

    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    String expectedBaseUri = "http://localhost:8080/Sample/Example-1.jsp";
    String expectedQueryString = null;
    String expectedSeoContentUri = new File(
      "/filePath/6574-en_us/stories/product/1/storiesgrid/ssl-certificates.htm"
    ).toURI().toString();

    String actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    String actualQueryString = bvSeoSdkUrl.queryString();
    String actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );
  }

  @Test
  public void storyTest_stories_pagenumber() {
    /*
     * Test case - 1
     */
    bvConfiguration.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "true"
    );

    BVParameters bvParam = new BVParameters();
    bvParam.setContentType(ContentType.STORIES);
    bvParam.setSubjectType(SubjectType.PRODUCT);
    bvParam.setSubjectId("ssl-certificates");

    bvParam.setContentSubType(ContentSubType.STORIES_GRID);
    bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvsyp=6574-en_us/stories/product/2/storiesgrid/ssl-certificates.htm");
    bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvsyp=6574-en_us/stories/product/2/storiesgrid/ssl-certificates.htm");
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    String expectedBaseUri = "/sample_seo_sdk_web/scenario-2.jsp?null";
    String expectedQueryString = "null&bvsyp=6574-en_us/stories/product/2/storiesgrid/ssl-certificates.htm";
    String expectedSeoContentUri = new File(
      "/filePath/6574-en_us/stories/product/2/storiesgrid/ssl-certificates.htm"
    ).toURI().toString();

    String actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    String actualQueryString = bvSeoSdkUrl.queryString();
    String actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );

    /*
     * Test case - 2
     */
    bvParam.setContentSubType(ContentSubType.STORIES_LIST);
    bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvsyp=6574-en_us/stories/product/2/stories/ssl-certificates.htm");
    bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvsyp=6574-en_us/stories/product/2/stories/ssl-certificates.htm");
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    expectedBaseUri = "/sample_seo_sdk_web/scenario-2.jsp?null";
    expectedQueryString = "null&bvsyp=6574-en_us/stories/product/2/stories/ssl-certificates.htm";
    expectedSeoContentUri = new File(
      "/filePath/6574-en_us/stories/product/2/stories/ssl-certificates.htm"
    ).toURI().toString();

    actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    actualQueryString = bvSeoSdkUrl.queryString();
    actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );

    /*
     * Test case - 3
     */
    bvParam.setContentSubType(null);
    bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvsyp=6574-en_us/stories/product/2/ssl-certificates.htm");
    bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvsyp=6574-en_us/stories/product/2/ssl-certificates.htm");
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);

    expectedBaseUri = "/sample_seo_sdk_web/scenario-2.jsp?null";
    expectedQueryString = "null&bvsyp=6574-en_us/stories/product/2/ssl-certificates.htm";
    expectedSeoContentUri = new File(
      "/filePath/6574-en_us/stories/product/2/ssl-certificates.htm"
    ).toURI().toString();

    actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
    actualQueryString = bvSeoSdkUrl.queryString();
    actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();

    assertEquals(
      actualBaseUri,
      expectedBaseUri,
      "actual and expected base uri should be same"
    );
    assertEquals(
      actualQueryString,
      expectedQueryString,
      "actual and expected query string should be same"
    );
    assertEquals(
      actualSeoContentUri,
      expectedSeoContentUri,
      "actual and expected seo content uri should be same"
    );
  }
}
