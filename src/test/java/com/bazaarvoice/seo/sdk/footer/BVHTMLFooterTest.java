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

package com.bazaarvoice.seo.sdk.footer;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkURLBuilder;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkUrl;
import org.testng.annotations.Test;

import java.util.ResourceBundle;

import static org.testng.Assert.*;

/**
 * Test class for BVHTMLFooter.
 *
 * @author Anandan Narayanaswamy
 *
 */
public class BVHTMLFooterTest {

  private final static String VERSION = ResourceBundle.getBundle("sdk").getString("version");
  private final static String LINE_SEPARATOR = System.lineSeparator();

  private static StringBuilder buildString(String[] lines){
    StringBuilder stringBuilder = new StringBuilder();
    for(String line: lines){
      stringBuilder.append(line)
              .append(LINE_SEPARATOR);
    }
    return stringBuilder;
  }

  @Test
  public void testDisplayFooter() {
    BVConfiguration bvConfiguration = new BVSdkConfiguration();

    BVParameters bvParameters = new BVParameters();
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setContentType(ContentType.REVIEWS);

    BVFooter bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
    String displayFooter = bvFooter.displayFooter("getContent");

    StringBuilder expectedFooter = buildString(new String[] {
            "<ul id=\"BVSEOSDK_meta\" style=\"display:none !important\">",
            "  <li data-bvseo=\"sdk\">bvseo_sdk, java_sdk, bvseo-" + VERSION + "</li>",
            "  <li data-bvseo=\"sp_mt\">CLOUD, getContent, 0ms</li>",
            "  <li data-bvseo=\"ct_st\">REVIEWS, PRODUCT</li>",
            "</ul>"
    });
    assertEquals(displayFooter, expectedFooter.toString(), "Footer did not match the expected value.");
  }

  /**
   * Test case to test display footer method for reveal=debug.
   */
  @Test
  public void testDisplayFooter_debug() {

    BVConfiguration bvConfiguration = new BVSdkConfiguration();
    bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "true");

    BVParameters bvParameters = new BVParameters();
    bvParameters.setContentType(ContentType.REVIEWS);
    bvParameters.setPageURI("?bvreveal=debug");
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setSubjectId("12345");

    BVSeoSdkUrl _bvSeoSdkUrl = new BVSeoSdkURLBuilder(
      bvConfiguration,
      bvParameters
    );

    BVFooter bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
    bvFooter.setBvSeoSdkUrl(_bvSeoSdkUrl);

    String displayFooter = bvFooter.displayFooter("getContent");
    String expectedFooter = getDbgFtrPtrn(bvParameters.getPageURI());

    assertEquals(displayFooter, expectedFooter, "Footer did not match the expected value.");

  }

  /**
   * Test case to test display footer method for reveal=debug for Seller Ratings Display.
   */
  @Test
  public void testDisplayFooter_debug_seller() {

    BVConfiguration bvConfiguration = new BVSdkConfiguration();
    bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "true");

    BVParameters bvParameters = new BVParameters();
    bvParameters.setContentType(ContentType.REVIEWS);
    bvParameters.setPageURI("?bvreveal=debug");
    bvParameters.setSubjectType(SubjectType.SELLER);
    bvParameters.setSubjectId("12345");

    BVSeoSdkUrl _bvSeoSdkUrl = new BVSeoSdkURLBuilder(
      bvConfiguration,
      bvParameters
    );

    BVFooter bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
    bvFooter.setBvSeoSdkUrl(_bvSeoSdkUrl);

    String displayFooter = bvFooter.displayFooter("getContent");

    displayFooter = displayFooter.replaceAll("\\n", "");

    assertTrue(
      !displayFooter.contains("productionS3Hostname"),
      "Contents should not include productionS3Hostname"
    );

  }

  /**
   * Test case to test display footer method for bvstate keyvalue pair reveal:debug.
   */
  @Test
  public void testDisplayFooter_debug_bvstate() {

    BVConfiguration bvConfiguration = new BVSdkConfiguration();
    bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "true");

    BVParameters bvParameters = new BVParameters();
    bvParameters.setContentType(ContentType.REVIEWS);
    bvParameters.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvstate=ct:r/reveal:debug");
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setSubjectId("12345");

    BVSeoSdkUrl _bvSeoSdkUrl = new BVSeoSdkURLBuilder(
      bvConfiguration,
      bvParameters
    );

    BVFooter bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
    bvFooter.setBvSeoSdkUrl(_bvSeoSdkUrl);

    String displayFooter = bvFooter.displayFooter("getContent");
    String expectedFooter = getDbgFtrPtrn(bvParameters.getPageURI());

    assertEquals(displayFooter, expectedFooter, "Footer did not match the expected value.");

  }

  /**
   * Test case to test display footer method for reveal=debug with URL.
   * Rules :
   * display URLs only when HTTP method is invoked and not for local files.
   */
  @Test
  public void testDisplayFooter_URL_debug() {

    BVConfiguration bvConfiguration = new BVSdkConfiguration();
    bvConfiguration.addProperty(BVClientConfig.CLOUD_KEY, "cloud123");
    bvConfiguration.addProperty(BVClientConfig.BV_ROOT_FOLDER, "bvRootFolder");
    bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");

    BVParameters bvParameters = new BVParameters();
    bvParameters.setPageURI("?bvreveal=debug");
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setContentType(ContentType.REVIEWS);
    bvParameters.setSubjectId("product1");

    BVSeoSdkUrl _bvSeoSdkUrl = new BVSeoSdkURLBuilder(
      bvConfiguration,
      bvParameters
    );

    BVFooter bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
    bvFooter.setBvSeoSdkUrl(_bvSeoSdkUrl);

    String displayFooter = bvFooter.displayFooter("getContent");
    displayFooter = displayFooter.replaceAll("\n", "");

    String expectedUrlPattern = ".*(\\s\\s\\Q\\E<li data-bvseo=\"contentURL\">http://seo.bazaarvoice.com/cloud123/bvRootFolder/reviews/product/1/product1.htm</li>\\Q\\E).*";
    String expectedContentTagPtrn = ".*(data-bvseo=\"contentURL\").*";

    assertTrue(
      displayFooter.matches(expectedUrlPattern),
      "The contents are changed either fix expectedUrlPattern or displayFooter"
    );
    assertTrue(
      displayFooter.matches(expectedContentTagPtrn),
      "The contents are changed either fix expectedContentTagPtrn or displayFooter"
    );

    /*
     * When loading from files it should not display URL.
     */
    bvConfiguration = new BVSdkConfiguration();
    bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "true");

    bvParameters = new BVParameters();
    bvParameters.setPageURI("?bvreveal=debug");
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setContentType(ContentType.REVIEWS);

    _bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParameters);

    bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
    bvFooter.setBvSeoSdkUrl(_bvSeoSdkUrl);

    displayFooter = bvFooter.displayFooter("getContent");
    displayFooter = displayFooter.replaceAll("\n", "");

    expectedContentTagPtrn = ".*(data-bvseo=\"contentURL\").*";

    assertFalse(
      displayFooter.matches(expectedContentTagPtrn),
      "The contents are changed either fix expectedContentTagPtrn or displayFooter"
    );
  }

  /**
   * Test case to test display footer method for reveal:debug bvstate key-value.
   * Rules :
   * display URLs only when HTTP method is invoked and not for local files.
   */
  @Test
  public void testDisplayFooter_URL_debug_bvstate() {

    BVConfiguration bvConfiguration = new BVSdkConfiguration();
    bvConfiguration.addProperty(BVClientConfig.CLOUD_KEY, "cloud123");
    bvConfiguration.addProperty(BVClientConfig.BV_ROOT_FOLDER, "bvRootFolder");
    bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");

    BVParameters bvParameters = new BVParameters();
    bvParameters.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvstate=ct:r/reveal:debug");
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setContentType(ContentType.REVIEWS);
    bvParameters.setSubjectId("product1");

    BVSeoSdkUrl _bvSeoSdkUrl = new BVSeoSdkURLBuilder(
      bvConfiguration,
      bvParameters
    );

    BVFooter bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
    bvFooter.setBvSeoSdkUrl(_bvSeoSdkUrl);

    String displayFooter = bvFooter.displayFooter("getContent");
    displayFooter = displayFooter.replaceAll("\n", "");

    String expectedUrlPattern = ".*(\\s\\s\\Q\\E<li data-bvseo=\"contentURL\">http://seo.bazaarvoice.com/cloud123/bvRootFolder/reviews/product/1/product1.htm</li>\\Q\\E).*";
    String expectedContentTagPtrn = ".*(data-bvseo=\"contentURL\").*";

    assertTrue(
      displayFooter.matches(expectedUrlPattern),
      "The contents are changed either fix expectedUrlPattern or displayFooter"
    );
    assertTrue(
      displayFooter.matches(expectedContentTagPtrn),
      "The contents are changed either fix expectedContentTagPtrn or displayFooter"
    );

    /*
     * When loading from files it should not display URL.
     */
    bvConfiguration = new BVSdkConfiguration();
    bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "true");

    bvParameters = new BVParameters();
    bvParameters.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvstate=ct:r/reveal:debug");
    bvParameters.setSubjectType(SubjectType.PRODUCT);
    bvParameters.setContentType(ContentType.REVIEWS);

    _bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParameters);

    bvFooter = new BVHTMLFooter(bvConfiguration, bvParameters);
    bvFooter.setBvSeoSdkUrl(_bvSeoSdkUrl);

    displayFooter = bvFooter.displayFooter("getContent");
    displayFooter = displayFooter.replaceAll("\n", "");

    expectedContentTagPtrn = ".*(data-bvseo=\"contentURL\").*";

    assertFalse(
      displayFooter.matches(expectedContentTagPtrn),
      "The contents are changed either fix expectedContentTagPtrn or displayFooter"
    );
  }

  private String getDbgFtrPtrn(String pageURI) {
    StringBuilder sBuilder = buildString(new String[] {
            "<ul id=\"BVSEOSDK_meta\" style=\"display:none !important\">",
            "  <li data-bvseo=\"sdk\">bvseo_sdk, java_sdk, bvseo-" + VERSION + "</li>",
            "  <li data-bvseo=\"sp_mt\">LOCAL, getContent, 0ms</li>",
            "  <li data-bvseo=\"ct_st\">REVIEWS, PRODUCT</li>",
            "</ul>",
            "",
            "<ul id=\"BVSEOSDK_meta_debug\" style=\"display:none !important\">",
            "  <li data-bvseo=\"testingStagingS3Hostname\">seo-qa-stg.bazaarvoice.com</li>",
            "  <li data-bvseo=\"testingProductionS3Hostname\">seo-qa.bazaarvoice.com</li>",
            "  <li data-bvseo=\"stagingS3Hostname\">seo-stg.bazaarvoice.com</li>",
            "  <li data-bvseo=\"productionS3Hostname\">seo.bazaarvoice.com</li>",
            "  <li data-bvseo=\"botDetection\"></li>",
            "  <li data-bvseo=\"bv.root.folder\"></li>",
            "  <li data-bvseo=\"cloudKey\"></li>",
            "  <li data-bvseo=\"loadSEOFilesLocally\">true</li>",
            "  <li data-bvseo=\"localSEOFileRoot\"></li>",
            "  <li data-bvseo=\"connectTimeout\">2000</li>",
            "  <li data-bvseo=\"socketTimeout\">2000</li>",
            "  <li data-bvseo=\"includeDisplayIntegrationCode\"></li>",
            "  <li data-bvseo=\"crawlerAgentPattern\">msnbot|google|teoma|bingbot|yandexbot|yahoo</li>",
            "  <li data-bvseo=\"seo.sdk.enabled\">true</li>",
            "  <li data-bvseo=\"staging\">false</li>",
            "  <li data-bvseo=\"testing\">false</li>",
            "  <li data-bvseo=\"seo.sdk.execution.timeout\">500</li>",
            "  <li data-bvseo=\"seo.sdk.execution.timeout.bot\">2000</li>",
            "  <li data-bvseo=\"seo.sdk.http.proxy.host\">none</li>",
            "  <li data-bvseo=\"seo.sdk.http.proxy.port\">0</li>",
            "  <li data-bvseo=\"seo.sdk.charset\"></li>",
            "  <li data-bvseo=\"seo.sdk.ssl.enabled\"></li>",
            "  <li data-bvseo=\"userAgent\"></li>",
            "  <li data-bvseo=\"baseURI\"></li>",
            "  <li data-bvseo=\"pageURI\">"+pageURI+"</li>",
            "  <li data-bvseo=\"subjectId\">12345</li>",
            "  <li data-bvseo=\"contentType\">REVIEWS</li>",
            "  <li data-bvseo=\"subjectType\">PRODUCT</li>",
            "</ul>"
    });

    return sBuilder.toString();
  }
}
