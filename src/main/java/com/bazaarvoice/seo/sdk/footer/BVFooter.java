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

import com.bazaarvoice.seo.sdk.url.BVSeoSdkUrl;

/**
 * Interface for adding bazaarvoice footer in the bazaarvoice seo content.
 *
 * @author Anandan Narayanaswamy
 */
public interface BVFooter {

  /**
   * returns the footer based on the configuration that is set.
   *
   * @return footer
   */
  String displayFooter(String accessMethod);

  /**
   * Add some message to the Footer.
   *
   * @param message
   */
  void addMessage(String message);

  void setExecutionTime(long executionTime);

  void setBvSeoSdkUrl(BVSeoSdkUrl _bvSeoSdkUrl);

}
