/*
 * ===========================================================================
 * Copyright 2014 Bazaarvoice, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
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

public enum BVClientConfig {

    BV_ROOT_FOLDER("bv.root.folder"),
    CLOUD_KEY("cloudKey"),
    LOAD_SEO_FILES_LOCALLY("loadSEOFilesLocally"),
    LOCAL_SEO_FILE_ROOT("localSEOFileRoot"),
    CONNECT_TIMEOUT("connectTimeout"),
    SOCKET_TIMEOUT("socketTimeout"),
    INCLUDE_DISPLAY_INTEGRATION_CODE("includeDisplayIntegrationCode"),
    CRAWLER_AGENT_PATTERN("crawlerAgentPattern"),
    SEO_SDK_ENABLED("seo.sdk.enabled"),
    STAGING("staging"),
    EXECUTION_TIMEOUT("seo.sdk.execution.timeout"),
    EXECUTION_TIMEOUT_BOT("seo.sdk.execution.timeout.bot"),
    PROXY_HOST("seo.sdk.http.proxy.host"),
    PROXY_PORT("seo.sdk.http.proxy.port"),
    CHARSET("seo.sdk.charset"),
    SSL_ENABLED("seo.sdk.ssl.enabled");
    
    private String propertyName;

    private BVClientConfig(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
