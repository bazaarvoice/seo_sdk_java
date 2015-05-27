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

import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkUrl;

/**
 * BVUIContentCaller an implementation class of Callable.
 *
 * @author Anandan Narayanaswamy
 */
public interface BVUIContentService {

  /**
   * Implementation to check if sdk is enabled/disabled.
   *
   * The settings are based on the configurations from BVConfiguration and
   * BVParameters.
   *
   * @return true if sdk is enabled and false if it is enabled.
   */
  boolean isSdkEnabled();

  /**
   * Executes the server side call or the file level call within a specified
   * execution timeout. When reload is set true then it gives from the cache
   * that was already executed in the previous call. This reduces the number hit
   * to the server.
   *
   * Any related message after execution is stored in getMessage which can be
   * retrieved for later use.
   *
   * @param reload
   * @return StringBuilder representation of the content.
   */
  StringBuilder executeCall(boolean reload);

  /**
   * Gets the messages if there are any after executeCall is invoked or if it is
   * still in the cache.
   *
   * @return Messages if there are any else blank.
   */
  StringBuilder getMessage();

  /**
   * boolean to check if content should be displaed for the user agent set in
   * bvParameters.
   */
  boolean showUserAgentSEOContent();

  void setBVParameters(BVParameters bvParameters);

  void setBVSeoSdkUrl(BVSeoSdkUrl bvSeoSdkUrl);
}
