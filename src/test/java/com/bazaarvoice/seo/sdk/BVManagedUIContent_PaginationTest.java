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

import com.bazaarvoice.seo.sdk.servlet.DefaultRequestContext;
import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;

/**
 * Test class to test pagination.
 *
 * @author Anandan Narayanaswamy
 */
public class BVManagedUIContent_PaginationTest {

  @Test
  public void testPagination() {
    DefaultRequestContext.initialize();
    BVConfiguration _bvConfig = new BVSdkConfiguration();
    _bvConfig.addProperty(
      BVClientConfig.SEO_SDK_ENABLED,
      "true"
    );
    _bvConfig.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "false"
    );
    _bvConfig.addProperty(
      BVClientConfig.LOCAL_SEO_FILE_ROOT,
      "/"
    );
    _bvConfig.addProperty(
      BVClientConfig.CLOUD_KEY,
      "adobe-55d020998d7b4776fb0f9df49278083c"
    );
    _bvConfig.addProperty(
      BVClientConfig.STAGING,
      "false"
    );
    _bvConfig.addProperty(
      BVClientConfig.CRAWLER_AGENT_PATTERN,
      "yandex"
    );
    _bvConfig.addProperty(
      BVClientConfig.BV_ROOT_FOLDER,
      "8814"
    );

    BVParameters _bvParam = new BVParameters();
    _bvParam.setUserAgent("yandex");
    // This value is pagination links.
    _bvParam.setBaseURI("Example-Adobe.jsp");
    _bvParam.setPageURI("http://localhost:8080/Sample/Example-Adobe.jsp?bvrrp=8814/reviews/product/4/PR6.htm&bvreveal=debug"); //this should be the current page, full URL
    _bvParam.setContentType(ContentType.REVIEWS);
    _bvParam.setSubjectType(SubjectType.PRODUCT);
    _bvParam.setSubjectId("PR6");

    BVUIContent _bvOutput = new BVManagedUIContent(_bvConfig);
    String sBvOutputReviews = _bvOutput.getContent(_bvParam);
  }
}
