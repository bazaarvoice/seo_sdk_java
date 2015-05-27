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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.bazaarvoice.seo.sdk.exception.BVSdkException;

/**
 * Utility class to read and get the message from resource bundle.
 *
 * @author Anandan Narayanaswamy
 */
public final class BVMessageUtil {

  private static ResourceBundle _rsrcBundle;

  static {
    _rsrcBundle = ResourceBundle.getBundle("message", Locale.US);
  }

  private BVMessageUtil() {}

  /**
   * Gets the message from resource bundle.
   *
   * Pass the message code including error code and you should get a message
   * that is in the resource bundle message_en_US.properties file.
   *
   * Returns the same message code if it is not configured in resource bundle.
   *
   * @throws {@link MissingResourceException}
   * @param String code A message code.
   * @return String Message from resource bundle.
   */
  public static String getMessage(String code) {
    if (StringUtils.isBlank(code)) {
      throw new BVSdkException("ERR0001");
    }

    String message = null;
    try {
      message = _rsrcBundle.getString(code);
    } catch (RuntimeException ex) {
      message = code;
    }

    return message;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException("This is a singleton instance.");
  }
}
