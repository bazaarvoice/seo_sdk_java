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

package com.bazaarvoice.seo.sdk.validation;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * Test case implementation for BVParameterValidator.
 *
 * @author Anandan Narayanaswamy
 */
public class BVValidatorTest {

  TestLogger logger = TestLoggerFactory.getTestLogger(BVDefaultValidator.class);

  /**
   * Test case for validate method in BVParameterValidator.
   *
   * This test case starts from failure scenario to till success scenario.
   */
  @Test
  public void testValidation() {
    String errorMessage = null;
    BVConfiguration bvConfig = null;
    BVParameters bvParams = null;

    BVValidator bvValidator = new BVDefaultValidator();
    errorMessage = bvValidator.validate(bvConfig, bvParams);
    assertEquals(
      errorMessage.contains(
        "BVConfiguration is null, please set a valid BVConfiguration.;"
      ),
      true, "Error Messages are different."
    );

    bvConfig = new BVSdkConfiguration();
    bvParams = new BVParameters();
    bvValidator = new BVDefaultValidator();
    errorMessage = bvValidator.validate(bvConfig, bvParams);
    assertEquals(
      errorMessage.contains(
        "CLOUD_KEY is not configured in BVConfiguration.;BV_ROOT_FOLDER is not"
        + " configured in BVConfiguration.;"
        + "SubjectId cannot be null or empty.;"
      ),
      true,
      "Error Messages are different."
    );
    assertTrue(logger.getLoggingEvents().contains(LoggingEvent.warn("userAgent in BVParameters is null and"
            + " com.bazaarvoice.seo.sdk.servlet.RequestFilter was not configured as a filter"
            + " in the web.xml for your application."
            + " Please configure one of these to provide userAgent information.;")));

  }

}
