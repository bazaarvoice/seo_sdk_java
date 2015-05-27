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

package com.bazaarvoice.seo.sdk.config;

import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.util.BVConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of configuration settings. This loads the Bazaarvoice
 * specific configuration and also user specific setting.
 *
 * @author Anandan Narayanaswamy
 */
public class BVSdkConfiguration implements BVConfiguration {

  private static Logger _logger = LoggerFactory.getLogger(
    BVSdkConfiguration.class
  );
  private Map<String, String> _instanceConfiguration;

  /**
   * Default constructor. If configuration should be overwritten or if you
   * need to load properties/configuration every time please use the add
   * method.
   */
  public BVSdkConfiguration() {
    _instanceConfiguration = new HashMap<String, String>();

    /*
     * Adding bvcore properties.
     */
    _instanceConfiguration.put(
      BVCoreConfig.PRODUCTION_S3_HOSTNAME.getPropertyName(),
      BVConstant.PRODUCTION_S3_HOSTNAME
    );
    _instanceConfiguration.put(
      BVCoreConfig.STAGING_S3_HOSTNAME.getPropertyName(),
      BVConstant.STAGING_S3_HOSTNAME
    );
    _instanceConfiguration.put(
      BVCoreConfig.TESTING_PRODUCTION_S3_HOSTNAME.getPropertyName(),
      BVConstant.TESTING_PRODUCTION_S3_HOSTNAME
    );
    _instanceConfiguration.put(
      BVCoreConfig.TESTING_STAGING_S3_HOSTNAME.getPropertyName(),
      BVConstant.TESTING_STAGING_S3_HOSTNAME
    );

    addProperty(
      BVClientConfig.EXECUTION_TIMEOUT,
      BVConstant.EXECUTION_TIMEOUT);
    addProperty(
      BVClientConfig.EXECUTION_TIMEOUT_BOT,
      BVConstant.EXECUTION_TIMEOUT_BOT
    );
    addProperty(
      BVClientConfig.CRAWLER_AGENT_PATTERN,
      BVConstant.CRAWLER_AGENT_PATTERN
    );
    addProperty(
      BVClientConfig.CONNECT_TIMEOUT,
      BVConstant.CONNECT_TIMEOUT);
    addProperty(
      BVClientConfig.SOCKET_TIMEOUT,
      BVConstant.SOCKET_TIMEOUT
    );
    addProperty(
      BVClientConfig.STAGING,
      BVConstant.STAGING
    );
    addProperty(
      BVClientConfig.TESTING,
      BVConstant.TESTING
    );
    addProperty(
      BVClientConfig.SEO_SDK_ENABLED,
      BVConstant.SEO_SDK_ENABLED
    );
    addProperty(
      BVClientConfig.PROXY_HOST,
      BVConstant.PROXY_HOST
    );
    addProperty(
      BVClientConfig.PROXY_PORT,
      BVConstant.PROXY_PORT
    );

    _logger.debug("Completed default properties in BVSdkConfiguration.");
  }

  public BVConfiguration addProperty(
    BVClientConfig bvConfig,
    String propertyValue
  ) {
    if (StringUtils.isBlank(propertyValue)) {
      throw new BVSdkException("ERR0006");
    }

    this._instanceConfiguration.put(bvConfig.getPropertyName(), propertyValue);
    return this;
  }

  public String getProperty(String propertyName) {
    return this._instanceConfiguration.get(propertyName);
  }
}
