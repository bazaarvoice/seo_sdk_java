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

import com.bazaarvoice.seo.sdk.model.BVParameters;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

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
    assertTrue(validContent);

    content = "This content has bV in the content";
    validContent = BVUtility.validateBVContent(content);
    assertTrue(validContent);

    content = "This content has Bv in the content";
    validContent = BVUtility.validateBVContent(content);
    assertTrue(validContent);

    content = "Thsi content has bv in the content";
    validContent = BVUtility.validateBVContent(content);
    assertTrue(validContent);

    content = "thisisstrangeBvcontent";
    validContent = BVUtility.validateBVContent(content);
    assertTrue(validContent);

    content = "corrupted";
    validContent = BVUtility.validateBVContent(content);
    assertFalse(validContent);

    content = "";
    validContent = BVUtility.validateBVContent(content);
    assertFalse(validContent);
  }

  @Test
  public void testGetFragment() {
    String inputUrl = "http://localhost:8080/index.jsp";
    String actualFragment = BVUtility.getFragmentString(inputUrl);
    String expectedFragment = "";
    assertEquals(
      actualFragment,
      expectedFragment,
      "actual and expected fragment should be same"
    );

    inputUrl = "http://localhost:8080/index.jsp#!a=b";
    actualFragment = BVUtility.getFragmentString(inputUrl);
    expectedFragment = "!a=b";
    assertEquals(
      actualFragment,
      expectedFragment,
      "actual and expected fragment should be same"
    );
  }

  @Test
  public void testRemoveBVParameters() {
    // input with empty url
    String inputUrl = "";
    String actualUrl = BVUtility.removeBVParameters(inputUrl);
    String expectedUrl = "";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    // input with no bvparameters
    inputUrl = "http://localhost:8080/index.jsp";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    // input with bvrrp bvparameter and no additional query parameters
    inputUrl = "http://localhost:8080/index.jsp?bvrrp=9344/reviews/product/4/5000001.htm";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    // input with bvrrp bvparameter and an additional query parameter
    inputUrl = "http://localhost:8080/index.jsp?a=b&bvrrp=9344/reviews/product/4/5000001.htm";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp?a=b";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    // input with bvqap bvparameter and an additional query parameter
    inputUrl = "http://localhost:8080/index.jsp?a=b&bvqap=xyz";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp?a=b";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    // input with bvsyp bvparameter and an additional query parameter
    inputUrl = "http://localhost:8080/index.jsp?a=b&bvsyp=xyz";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp?a=b";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    // input with bvpage bvparameter and an additional query parameter
    inputUrl = "http://localhost:8080/index.jsp?a=b&bvpage=xyz";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp?a=b";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    /**
     * input with bvstate bvparameter and no additional query parameter.
     * bvstate with all key-value pairs
     */
    inputUrl = "http://localhost:8080/index.jsp?bvstate=pg:2/ct:r/st:p/id:5000001/reveal:debug";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    /**
     * input with bvstate bvparameter and no additional query parameter.
     * bvstate with all key-value pairs with additional / at the end
     */
    inputUrl = "http://localhost:8080/index.jsp?bvstate=pg:2/ct:r/st:p/id:5000001/reveal:notdebug/";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    /**
     * input with bvstate bvparameter and no additional query parameter.
     * bvstate with minimum key-value pairs
     */
    inputUrl = "http://localhost:8080/index.jsp?bvstate=ct:r";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    /**
     * input with bvstate bvparameter and additional query parameter.
     * bvstate with all key-value pairs
     */
    inputUrl = "http://localhost:8080/index.jsp?bvstate=pg:2/ct:r/st:p/id:5000001/reveal:debug&a=b";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp?a=b";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    /**
     * input with bvstate bvparameter and additional query parameter before bvstate.
     * bvstate with all key-value pairs
     */
    inputUrl = "http://localhost:8080/index.jsp?a=b&bvstate=pg:2/ct:r/st:p/id:5000001/reveal:notdebug";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp?a=b";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    /**
     * input with bvstate bvparameter and no additional hash parameter.
     * bvstate in hashbang with all key-value pairs
     */
    inputUrl = "http://localhost:8080/index.jsp#!bvstate=pg:2/ct:r/st:p/id:5000001/reveal:debug";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp#!";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    /**
     * input with bvstate bvparameter and additional hash parameter.
     * bvstate in hashbang with all key-value pairs
     */
    inputUrl = "http://localhost:8080/index.jsp#!bvstate=pg:2/ct:r/st:p/id:5000001/reveal:debug&a=b";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp#!a=b";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    /**
     * input with bvstate bvparameter and no additional parameter.
     * bvstate in escaped_fragment with all key-value pairs
     */
    inputUrl = "http://localhost:8080/index.jsp?_escaped_fragment_=bvstate=pg:2/ct:r/st:p/id:5000001/reveal:debug";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp?_escaped_fragment_=";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    /**
     * input with bvstate bvparameter and additional escaped_fragment parameter.
     * bvstate in escaped_fragment with all key-value pairs
     */
    inputUrl = "http://localhost:8080/index.jsp?_escaped_fragment_=bvstate=pg:2/ct:r/st:p/id:5000001/reveal:debug%26c=d";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp?_escaped_fragment_=c=d";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    /**
     * input with bvstate bvparameter, additional escaped_fragment parameter
     * and additional query parameter.
     * bvstate in escaped_fragment with all key-value pairs
     */
    inputUrl = "http://localhost:8080/index.jsp?a=b&_escaped_fragment_=bvstate=pg:2/ct:r/st:p/id:5000001/reveal:debug%26c=d";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp?a=b&_escaped_fragment_=c=d";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    /**
     * input with multiple bvparameters like bvstate and bvrrp.
     * No other additional query parameters
     */
    inputUrl = "http://localhost:8080/index.jsp?bvstate=pg:2/ct:r/st:p/id:5000001/&bvrrp=xyz";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );

    /**
     * input with multiple bvparameters like bvstate and bvrrp.
     * Has additional query parameter
     */
    inputUrl = "http://localhost:8080/index.jsp?bvstate=pg:2/ct:r/st:p/id:5000001/&bvrrp=xyz&a=b";
    actualUrl = BVUtility.removeBVParameters(inputUrl);
    expectedUrl = "http://localhost:8080/index.jsp?a=b";
    assertEquals(
      actualUrl,
      expectedUrl,
      "actual and expected url should be same"
    );
  }

  @Test
  public void testPageURIReplacementInContent() {
    /*
     * bvrrp in content with no query/fragment string in baseUri.
     * append ?
     */
    String data = "{INSERT_PAGE_URI}bvrrp=9344/reviews/product/4/5000001.htm";
    StringBuilder content = new StringBuilder();
    content.append(data);
    String baseUri = "http://localhost:8080/index.jsp";
    String expectedContent = "http://localhost:8080/index.jsp?bvrrp=9344/reviews/product/4/5000001.htm";
    BVUtility.replacePageURIFromContent(
      content,
      baseUri
    );
    assertEquals(
      content.toString(),
      expectedContent,
      "actual and expected content should be same"
    );

    /*
     * bvrrp in content with existing query string in baseUri.
     * append &
     */
    data = "{INSERT_PAGE_URI}bvrrp=9344/reviews/product/4/5000001.htm";
    content = new StringBuilder();
    content.append(data);
    baseUri = "http://localhost:8080/index.jsp?a=b";
    expectedContent = "http://localhost:8080/index.jsp?a=b&bvrrp=9344/reviews/product/4/5000001.htm";
    BVUtility.replacePageURIFromContent(
      content,
      baseUri
    );
    assertEquals(
      content.toString(),
      expectedContent,
      "actual and expected content should be same"
    );

    /*
     * bvstate in content with no query/fragment string in baseUri.
     * append ?
     */
    data = "{INSERT_PAGE_URI}bvstate=pg:2/ct:r/st:p/id:5000001";
    content = new StringBuilder();
    content.append(data);
    baseUri = "http://localhost:8080/index.jsp";
    expectedContent = "http://localhost:8080/index.jsp?bvstate=pg:2/ct:r/st:p/id:5000001";
    BVUtility.replacePageURIFromContent(
      content,
      baseUri
    );
    assertEquals(
      content.toString(),
      expectedContent,
      "actual and expected content should be same"
    );

    /*
     * bvstate in content with existing query string in baseUri.
     * append &
     */
    data = "{INSERT_PAGE_URI}bvstate=pg:2/ct:r/st:p/id:5000001";
    content = new StringBuilder();
    content.append(data);
    baseUri = "http://localhost:8080/index.jsp?a=b";
    expectedContent = "http://localhost:8080/index.jsp?a=b&bvstate=pg:2/ct:r/st:p/id:5000001";
    BVUtility.replacePageURIFromContent(
      content,
      baseUri
    );
    assertEquals(
      content.toString(),
      expectedContent,
      "actual and expected content should be same"
    );

    /*
     * bvstate in content with existing fragment string in baseUri.
     * baseUri - ends with #!
     * append nothing
     */
    data = "{INSERT_PAGE_URI}bvstate=pg:2/ct:r/st:p/id:5000001";
    content = new StringBuilder();
    content.append(data);
    baseUri = "http://localhost:8080/index.jsp#!";
    expectedContent = "http://localhost:8080/index.jsp#!bvstate=pg:2/ct:r/st:p/id:5000001";
    BVUtility.replacePageURIFromContent(
      content,
      baseUri
    );
    assertEquals(
      content.toString(),
      expectedContent,
      "actual and expected content should be same"
    );

    /*
     * bvstate in content with existing fragment string in baseUri.
     * baseUri - not end with #! but ends with ?
     * append nothing
     */
    data = "{INSERT_PAGE_URI}bvstate=pg:2/ct:r/st:p/id:5000001";
    content = new StringBuilder();
    content.append(data);
    baseUri = "http://localhost:8080/index.jsp#!a=b?";
    expectedContent = "http://localhost:8080/index.jsp#!a=b?bvstate=pg:2/ct:r/st:p/id:5000001";
    BVUtility.replacePageURIFromContent(
      content,
      baseUri
    );
    assertEquals(
      content.toString(),
      expectedContent,
      "actual and expected content should be same"
    );

    /*
     * bvstate in content with existing fragment string in baseUri.
     * baseUri - not end with #! and not ends with ? but contains ?
     * append &
     */
    data = "{INSERT_PAGE_URI}bvstate=pg:2/ct:r/st:p/id:5000001";
    content = new StringBuilder();
    content.append(data);
    baseUri = "http://localhost:8080/index.jsp#!a=b?testing=true";
    expectedContent = "http://localhost:8080/index.jsp#!a=b?testing=true&bvstate=pg:2/ct:r/st:p/id:5000001";
    BVUtility.replacePageURIFromContent(
      content,
      baseUri
    );
    assertEquals(
      content.toString(),
      expectedContent,
      "actual and expected content should be same"
    );

    /*
     * bvstate in content with existing fragment string in baseUri.
     * baseUri - not end with #! and doesnot contain ?
     * append ?
     */
    data = "{INSERT_PAGE_URI}bvstate=pg:2/ct:r/st:p/id:5000001";
    content = new StringBuilder();
    content.append(data);
    baseUri = "http://localhost:8080/index.jsp#!a=b";
    expectedContent = "http://localhost:8080/index.jsp#!a=b?bvstate=pg:2/ct:r/st:p/id:5000001";
    BVUtility.replacePageURIFromContent(
      content,
      baseUri
    );
    assertEquals(
      content.toString(),
      expectedContent,
      "actual and expected content should be same"
    );

    /*
     * bvstate in content with existing _escaped_fragment_ string in baseUri.
     * baseUri - ends with _escaped_fragment_=
     * append nothing
     */
    data = "{INSERT_PAGE_URI}bvstate=pg:2/ct:r/st:p/id:5000001";
    content = new StringBuilder();
    content.append(data);
    baseUri = "http://localhost:8080/index.jsp?_escaped_fragment_=";
    expectedContent = "http://localhost:8080/index.jsp?_escaped_fragment_=bvstate=pg:2/ct:r/st:p/id:5000001";
    BVUtility.replacePageURIFromContent(
      content,
      baseUri
    );
    assertEquals(
      content.toString(),
      expectedContent,
      "actual and expected content should be same"
    );

    /*
     * bvstate in content with existing _escaped_fragment_ string in baseUri.
     * baseUri - contains _escaped_fragment_ with ? in it
     * append %26
     */
    data = "{INSERT_PAGE_URI}bvstate=pg:2/ct:r/st:p/id:5000001";
    content = new StringBuilder();
    content.append(data);
    baseUri = "http://localhost:8080/index.jsp?_escaped_fragment_=a/b?c=d";
    expectedContent = "http://localhost:8080/index.jsp?_escaped_fragment_=a/b?c=d%26bvstate=pg:2/ct:r/st:p/id:5000001";
    BVUtility.replacePageURIFromContent(
      content,
      baseUri
    );
    assertEquals(
      content.toString(),
      expectedContent,
      "actual and expected content should be same"
    );

    /*
     * bvstate in content with existing _escaped_fragment_ string in baseUri.
     * baseUri - contains _escaped_fragment_ with no ? in it
     * append ?
     */
    data = "{INSERT_PAGE_URI}bvstate=pg:2/ct:r/st:p/id:5000001";
    content = new StringBuilder();
    content.append(data);
    baseUri = "http://localhost:8080/index.jsp?_escaped_fragment_=a/b";
    expectedContent = "http://localhost:8080/index.jsp?_escaped_fragment_=a/b?bvstate=pg:2/ct:r/st:p/id:5000001";
    BVUtility.replacePageURIFromContent(
      content,
      baseUri
    );
    assertEquals(
      content.toString(),
      expectedContent,
      "actual and expected content should be same"
    );
  }

  @Test
  public void testIsRevealDebugEnabled() {
    BVParameters bvParam = new BVParameters();
    // No bvreveal=debug in query param or reveal:debug in bvstate
    bvParam.setBaseURI("http://localhost:8080/Sample/Example-1.jsp");
    bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvstate=pg:2/ct:r/st:p/id:ssl-certificates");
    assertFalse(
      BVUtility.isRevealDebugEnabled(bvParam),
      "reveal debug should be false"
    );

    // Has bvreveal=notdebug in query param. No reveal:debug in bvstate
    bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvstate=pg:2/ct:r/st:p/id:ssl-certificates&bvreveal=notdebug");
    assertFalse(
      BVUtility.isRevealDebugEnabled(bvParam),
      "reveal debug should be false"
    );

    // Has bvreveal=debug in query param
    bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvstate=pg:2/ct:r/st:p/id:ssl-certificates&bvreveal=debug");
    assertTrue(
      BVUtility.isRevealDebugEnabled(bvParam),
      "reveal debug should be true"
    );

    // Has reveal:notdebug in bvstate
    bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvstate=pg:2/ct:r/st:p/id:ssl-certificates/reveal:notdebug");
    assertFalse(
      BVUtility.isRevealDebugEnabled(bvParam),
      "reveal debug should be false"
    );

    // Has reveal:debug in bvstate
    bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvstate=pg:2/ct:r/st:p/id:ssl-certificates/reveal:debug");
    assertTrue(
      BVUtility.isRevealDebugEnabled(bvParam),
      "reveal debug should be true"
    );

    // Has reveal:debug in bvstate with minimum bvstate key-value pairs
    bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvstate=ct:r/reveal:debug");
    assertTrue(
      BVUtility.isRevealDebugEnabled(bvParam),
      "reveal debug should be true"
    );
  }
}
