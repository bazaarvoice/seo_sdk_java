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

package com.bazaarvoice.seo.sdk.proxy;

import static org.testng.Assert.assertEquals;

import org.eclipse.jetty.proxy.ConnectHandler;
import org.eclipse.jetty.server.AbstractNetworkConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.BVManagedUIContent;
import com.bazaarvoice.seo.sdk.BVUIContent;
import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;

/**
 * Test class to test proxy suppport.
 *
 * @author Anandan Narayanaswamy
 */
public class ProxyTest {

  /**
   * Do not set any proxy server and the system should function correctly.
   */
  @Test
  public void testWithoutProxy() {
    BVConfiguration bvConfig = new BVSdkConfiguration();
    bvConfig.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "false"
    );
    bvConfig.addProperty(
      BVClientConfig.STAGING,
      "true"
    );
    bvConfig.addProperty(
      BVClientConfig.CLOUD_KEY,
      "agileville-78B2EF7DE83644CAB5F8C72F2D8C8491"
    );
    bvConfig.addProperty(
      BVClientConfig.BV_ROOT_FOLDER,
      "Main_Site-en_US"
    );
    BVUIContent uiContent = new BVManagedUIContent(bvConfig);

    BVParameters bvParameters = new BVParameters();
    bvParameters.setUserAgent("google");
    bvParameters.setPageURI(
      "/someproduct.jsp?bvpage=ctre/iddata-gen-5zkafmln4wymhcfbp5u6hmv5q/stp"
    );

    String theUIContent = uiContent.getContent(bvParameters);
    assertEquals(
      theUIContent.contains("class=\"bvseo-review\" itemprop=\"review\" itemscope itemtype=\"http://schema.org/Review\""),
      true,
      "there should be BvRRSourceID in the content"
    );
    assertEquals(
      theUIContent.contains("bvseo-msg: Connect to localhost:9999 timed out"),
      false,
      "there should not be connection timed out message."
    );
  }

  /**
   * Set an incorrect proxy server and try accessing and the system should fail.
   */
  @Test
  public void testProxyImplementation_Failure() {
    BVConfiguration bvConfig = new BVSdkConfiguration();
    bvConfig.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "false"
    );
    bvConfig.addProperty(
      BVClientConfig.STAGING,
      "true"
    );
    bvConfig.addProperty(
      BVClientConfig.CLOUD_KEY,
      "agileville-78B2EF7DE83644CAB5F8C72F2D8C8491"
    );
    bvConfig.addProperty(
      BVClientConfig.BV_ROOT_FOLDER,
      "Main_Site-en_US"
    );
    bvConfig.addProperty(
      BVClientConfig.PROXY_HOST,
      "localhost"
    );
    bvConfig.addProperty(
      BVClientConfig.PROXY_PORT,
      "9999"
    );
    BVUIContent uiContent = new BVManagedUIContent(bvConfig);

    BVParameters bvParameters = new BVParameters();
    bvParameters.setUserAgent("google");
    bvParameters.setPageURI("/someproduct.jsp?bvpage=ctre/iddata-gen-5zkafmln4wymhcfbp5u6hmv5q/stp");

    String theUIContent = uiContent.getContent(bvParameters);
    assertEquals(
      theUIContent.contains("class=\"bvseo-review\" itemprop=\"review\" itemscope itemtype=\"http://schema.org/Review\""),
      false,
      "there should be BvRRSourceID in the content"
    );
  }

  /**
   * Set a correct proxy server and the system should function
   */
  @Test
  public void testProxyImplementation_Success() {
    Server proxyServer = startProxyServer();

    BVConfiguration bvConfig = new BVSdkConfiguration();
    bvConfig.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "false"
    );
    bvConfig.addProperty(
      BVClientConfig.STAGING,
      "true"
    );
    bvConfig.addProperty(
      BVClientConfig.CLOUD_KEY,
      "agileville-78B2EF7DE83644CAB5F8C72F2D8C8491"
    );
    bvConfig.addProperty(
      BVClientConfig.BV_ROOT_FOLDER,
      "Main_Site-en_US"
    );
    bvConfig.addProperty(
      BVClientConfig.PROXY_HOST,
      "localhost"
    );
    bvConfig.addProperty(
      BVClientConfig.PROXY_PORT,
      "9999"
    );

    BVUIContent uiContent = new BVManagedUIContent(bvConfig);

    BVParameters bvParameters = new BVParameters();
    bvParameters.setUserAgent("google");
    bvParameters.setPageURI("/someproduct.jsp?bvpage=ctre/id3000001/stp");

    String theUIContent = uiContent.getContent(bvParameters);
    stopProxyServer(proxyServer);

    assertEquals(
      theUIContent.contains("bvseo-msg: The resource to the URL or file is currently unavailable."),
      true,
      "there should be resource not available message in the content"
    );
    assertEquals(
      theUIContent.contains("bvseo-msg: Connect to localhost:9999 timed out"),
      false,
      "there should not be connection timed out message."
    );

  }

  private Server startProxyServer() {
    Server proxyServer = new Server();
    ConnectHandler handler = new ConnectHandler();
    handler.setConnectTimeout(5000);
    proxyServer.setHandler(handler);

    AbstractNetworkConnector proxyConnector = new ServerConnector(proxyServer);
    proxyConnector.setHost("localhost");
    proxyConnector.setPort(9999);

    proxyServer.addConnector(proxyConnector);
    try {
      proxyServer.start();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return proxyServer;
  }

  private void stopProxyServer(Server server) {
    try {
      server.stop();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
