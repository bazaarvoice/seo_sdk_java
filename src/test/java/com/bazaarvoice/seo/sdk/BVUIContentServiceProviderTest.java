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
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkURLBuilder;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkUrl;

/**
 * Test class for {@link BVUIContentServiceProvider}
 *
 * @author Anandan Narayanaswamy
 */
public class BVUIContentServiceProviderTest {

  private BVUIContentService bvUIContentService;
  private BVConfiguration bvConfiguration;

  @BeforeMethod
  public void doSetup() {
    bvConfiguration = new BVSdkConfiguration();
  }

  /**
   * Test case to check if showUserAgentSEOContent is working as expected.
   * Refer to implementation on various cases how this is achieved.
   */
  @Test
  public void testShowUserAgentSeoContent() {
    bvUIContentService = new BVUIContentServiceProvider(bvConfiguration);

    BVParameters bvParameters = null;
    bvUIContentService.setBVParameters(bvParameters);

    boolean showUserAgent = bvUIContentService.showUserAgentSEOContent();
    StringBuilder message = bvUIContentService.getMessage();
    assertFalse(showUserAgent, "value retuned should be false");
    assertTrue(
      message.toString().isEmpty(),
      "there should not be any message."
    );

    bvParameters = new BVParameters();
    bvUIContentService.setBVParameters(bvParameters);
    showUserAgent = bvUIContentService.showUserAgentSEOContent();
    message = bvUIContentService.getMessage();
    assertFalse(showUserAgent, "value retuned should be false");
    assertTrue(
      message.toString().isEmpty(),
      "there should not be any message."
    );

    bvParameters.setUserAgent("some other string representation");
    showUserAgent = bvUIContentService.showUserAgentSEOContent();
    message = bvUIContentService.getMessage();
    assertFalse(showUserAgent, "value retuned should be false");
    assertTrue(
      message.toString().isEmpty(),
      "there should not be any message."
    );

    /*
     * Custom configuration for crawler agent
     */
    bvConfiguration.addProperty(
      BVClientConfig.CRAWLER_AGENT_PATTERN,
      "custombot"
    );
    bvParameters.setUserAgent("custombot");
    showUserAgent = bvUIContentService.showUserAgentSEOContent();
    message = bvUIContentService.getMessage();
    assertTrue(showUserAgent, "value retuned should be true");
    assertTrue(
      message.toString().isEmpty(),
      "there should not be any message."
    );

    /**
     * Custom crawler but with google user agent.
     */
    bvConfiguration.addProperty(
      BVClientConfig.CRAWLER_AGENT_PATTERN,
      "custombot"
    );
    bvParameters.setUserAgent("googlebot");
    showUserAgent = bvUIContentService.showUserAgentSEOContent();
    message = bvUIContentService.getMessage();
    assertTrue(showUserAgent, "value retuned should be true");
    assertTrue(
      message.toString().isEmpty(),
      "there should not be any message."
    );
  }

  /**
   * Test case to check if sdkEnabled gives the expected result.
   */
  @Test
  public void testSDKEnabled() {
    bvUIContentService = new BVUIContentServiceProvider(bvConfiguration);
    BVParameters bvParameters = null;

    bvUIContentService.setBVParameters(bvParameters);
    boolean isSdkEnabled = bvUIContentService.isSdkEnabled();
    assertTrue(isSdkEnabled, "SDK enabled should be true here.");

    /*
     * Consistent behavior check
     */
    bvConfiguration.addProperty(BVClientConfig.SEO_SDK_ENABLED, "False");
    isSdkEnabled = bvUIContentService.isSdkEnabled();
    assertTrue(isSdkEnabled, "SDK enabled should be still true here.");

    /*
     * Disabled behavior check.
     */
    bvConfiguration.addProperty(BVClientConfig.SEO_SDK_ENABLED, "False");
    bvParameters = new BVParameters();
    bvParameters.setPageURI("http://localhost:8080/sampleapp/thecontent.jsp?product=abc");
    BVSeoSdkUrl bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParameters);
    bvUIContentService = new BVUIContentServiceProvider(bvConfiguration);
    bvUIContentService.setBVSeoSdkUrl(bvSeoSdkUrl);
    isSdkEnabled = bvUIContentService.isSdkEnabled();
    assertFalse(isSdkEnabled, "SDK enabled should be false here.");

    /*
     * Disable SDK but upon bvreveal sdkEnabled should be true.
     */
    bvConfiguration.addProperty(BVClientConfig.SEO_SDK_ENABLED, "False");
    bvParameters = new BVParameters();
    bvParameters.setPageURI("http://localhost:8080/sampleapp/thecontent.jsp?product=abc&bvreveal");
    bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParameters);
    bvUIContentService = new BVUIContentServiceProvider(bvConfiguration);
    bvUIContentService.setBVSeoSdkUrl(bvSeoSdkUrl);
    isSdkEnabled = bvUIContentService.isSdkEnabled();
    assertTrue(isSdkEnabled, "SDK enabled should be true here.");
  }

}
