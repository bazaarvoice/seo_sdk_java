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

package com.bazaarvoice.seo.sdk.util;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for BVUtility.
 *
 * @author Anandan Narayanaswamy
 */
public class BVUtilityTest {

  /**
   * Test case for testing BV Pattern.
   */
  @Test
  public void testBVPattern() {
    /*
     * White space match with BV.
     */
    String content = "This is a valid content which has BV and let us see";
    boolean validContent = BVUtility.validateBVContent(content);
    Assert.assertTrue(validContent);

    content = "This content has bV in the content";
    validContent = BVUtility.validateBVContent(content);
    Assert.assertTrue(validContent);

    content = "This content has Bv in the content";
    validContent = BVUtility.validateBVContent(content);
    Assert.assertTrue(validContent);

    content = "Thsi content has bv in the content";
    validContent = BVUtility.validateBVContent(content);
    Assert.assertTrue(validContent);

    content = "thisisstrangeBvcontent";
    validContent = BVUtility.validateBVContent(content);
    Assert.assertTrue(validContent);

    content = "corrupted";
    validContent = BVUtility.validateBVContent(content);
    Assert.assertFalse(validContent);

    content = "";
    validContent = BVUtility.validateBVContent(content);
    Assert.assertFalse(validContent);
  }

}
