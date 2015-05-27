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
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;

/**
 * Test class for BVManagedUIContent implementation class.
 *
 * This test case focuses more from an implementation point of view. There may
 * be a few test cases which can be considered as use case scenario.
 *
 * @author Anandan Narayanaswamy
 */
public class BVManagedUIContentTest {

  /**
   * Test case to check if null query param is supplied
   * to BVManagedUIContent.
   */
  @Test
  public void testSearchContentNullBVQueryParams() {
    BVUIContent bvUIContent = new BVManagedUIContent();
    String bvContent = null;

    bvContent = bvUIContent.getContent(null);
    assertEquals(
      bvContent.contains("<li data-bvseo=\"ms\">bvseo-msg: BVParameters is null.;</li>"),
      true,
      "Message are not same please verify."
    );
  }

  /**
   * Test case with various possible BaseURI and PageURI that can be supplied
   * inside bvParameters.
   *
   * These are valid Base and PageURI and the code implementation should handle
   * all scenarios and should not throw any error or behave improperly. Also
   * note that base and pageUri can be null and we assume that user has entered
   * empty string for the same
   */
  @Test
  public void testBase_PageUri() {

    /*
     * When base uri and page uri are null.
     */
    BVConfiguration _bvConfig = new BVSdkConfiguration();
    _bvConfig.addProperty(
      BVClientConfig.SEO_SDK_ENABLED,
      "true"
    );
    _bvConfig.addProperty(
      BVClientConfig.STAGING,
      "false"
    );
    _bvConfig.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "false"
    );
    _bvConfig.addProperty(
      BVClientConfig.CLOUD_KEY,
      "godaddy-a4501eb5be8bf8efda68f3f4ff7b3cf4"
    );
    _bvConfig.addProperty(
      BVClientConfig.LOCAL_SEO_FILE_ROOT,
      "/"
    );
    _bvConfig.addProperty(
      BVClientConfig.BV_ROOT_FOLDER,
      "6574-en_us"
    );

    BVParameters _bvParam = new BVParameters();
    _bvParam.setUserAgent("google");
    _bvParam.setBaseURI(null);
    _bvParam.setPageURI(null);
    _bvParam.setContentType(ContentType.REVIEWS);
    _bvParam.setSubjectType(SubjectType.PRODUCT);
    _bvParam.setSubjectId("ssl-certificates");

    BVUIContent _bvOutput = new BVManagedUIContent(_bvConfig);
    String sBvOutput = _bvOutput.getContent(_bvParam);
    assertNotNull(sBvOutput, "sBvOutput should not be null");

    /*
     * When base uri and page uri are empty.
     */
    _bvParam.setBaseURI("");
    _bvParam.setPageURI("");

    _bvOutput = new BVManagedUIContent(_bvConfig);
    sBvOutput = _bvOutput.getContent(_bvParam);
    assertNotNull(sBvOutput, "sBvOutput should not be null");

    /*
     * When base uri and page uri are complete urls.
     */
    _bvParam.setBaseURI("http://localhost:8080/Sample/Example-1.jsp");
    _bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvrrp=abcd");

    _bvOutput = new BVManagedUIContent(_bvConfig);
    sBvOutput = _bvOutput.getContent(_bvParam);
    assertNotNull(sBvOutput, "sBvOutput should not be null");

    /*
     * When base uri and page uri has bvrrp parameters.
     */
    _bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/3/ssl-certificates.htm");
    _bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/3/ssl-certificates.htm");
    _bvOutput = new BVManagedUIContent(_bvConfig);
    sBvOutput = _bvOutput.getContent(_bvParam);
    assertNotNull(sBvOutput, "sBvOutput should not be null");

    /*
     * When base uri and page uri has bvrrp parameters.
     */
    _bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/2/ssl-certificates.htm");
    _bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/2/ssl-certificates.htm");
    _bvOutput = new BVManagedUIContent(_bvConfig);
    sBvOutput = _bvOutput.getContent(_bvParam);
    assertNotNull(sBvOutput, "sBvOutput should not be null");
  }

}
