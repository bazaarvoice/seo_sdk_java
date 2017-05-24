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

/**
 * Common constants class used through out SEO SDK implementations.
 *
 * @author Anandan Narayanaswamy
 */
public final class BVConstant {

  public static final String BVREVEAL_DEBUG = "bvreveal=debug";
  public static final String BVSTATE_REGEX = "bvstate=((([^?&/%:])*:([^?&/%])*)(/)?)+";
  public static final String BVSTATE_REGEX_WITH_TRAILING_SEPARATOR = BVSTATE_REGEX + "(&|%26){0,1}";
  public static final String BVSTATE_REVEAL_DEBUG_REGEX = "bvstate=((([^?&/%:])*:([^?&/%])*)(/)?)+(reveal:debug)";
  public static final String BVSTATE_KEYVALUE_SEPARATOR_CHAR = ":";
  public static final String BVSTATE_TOKEN_SEPARATOR_CHAR = "/";
  public static final String ESCAPED_FRAGMENT_KEY = "_escaped_fragment_=";
  public static final String ESCAPED_FRAGMENT_MULTIVALUE_SEPARATOR = "%26";
  public static final String FRAGMENT_MARKER = "#!";
  public static final String JS_DISPLAY_MSG = "JavaScript-only Display;";
  public static final String INCLUDE_PAGE_URI = "{INSERT_PAGE_URI}";
  public static final String ENVIRONMENT_TESTING_STAGING = "qa-stg";
  public static final String ENVIRONMENT_TESTING = "qa";
  public static final String ENVIRONMENT_STAGING = "stg";
  public static final String ENVIRONMENT_PROD = "prod";

  /*
   * Some of the default property values.
   */
  public static final String STAGING_S3_HOSTNAME = "seo-stg-bazaarvoice-com.atgnow.com";
  public static final String PRODUCTION_S3_HOSTNAME = "seo-bazaarvoice-com.atgnow.com";
  public static final String SELLER_RATINGS_S3_HOSTNAME = "srd.bazaarvoice.com";
  public static final String TESTING_PRODUCTION_S3_HOSTNAME = "seo-qa.bazaarvoice.com";
  public static final String TESTING_STAGING_S3_HOSTNAME = "seo-qa-stg.bazaarvoice.com";
  public static final String EXECUTION_TIMEOUT = "500";
  public static final String EXECUTION_TIMEOUT_BOT = "2000";
  public static final String CRAWLER_AGENT_PATTERN = "msnbot|google|teoma|bingbot|yandexbot|yahoo";
  public static final String BV_STRING_PATTERN = "BV";
  public static final String CONNECT_TIMEOUT = "2000";
  public static final String SOCKET_TIMEOUT = "2000";
  public static final String STAGING = "false";
  public static final String TESTING = "false";
  public static final String SEO_SDK_ENABLED = "true";
  public static final String PROXY_HOST = "none";
  public static final String PROXY_PORT = "0";
}
