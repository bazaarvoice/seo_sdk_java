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
 * Test class to test proxy suppport
 * @author Anandan Narayanaswamy
 *
 */
public class ProxyTest {
	
	/**
	 * Do not set any proxy server and the system should function correctly.
	 */
	@Test
	public void testWithoutProxy() {
		BVConfiguration bvConfig = new BVSdkConfiguration();
		bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");
		bvConfig.addProperty(BVClientConfig.CLOUD_KEY, "seo_sdk_testcase-159b6108bb11967e554a92c6a3c39cb3");
		bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "9344seob");
		BVUIContent uiContent = new BVManagedUIContent(bvConfig);
		
		BVParameters bvParameters = new BVParameters();
		bvParameters.setUserAgent("google");
		bvParameters.setPageURI("/someproduct.jsp?bvpage=ctre/id5000002/stp");
		
		String theUIContent = uiContent.getContent(bvParameters);
		//System.out.println(theUIContent);
		assertEquals(theUIContent.contains("BVRRSourceID"), true, "there should be BvRRSourceID in the content");
		assertEquals(theUIContent.contains("bvseo-msg: Connect to localhost:9999 timed out"), false, 
				"there should not be connection timed out message.");
	}

	/**
	 * Set an incorrect proxy server and try accessing and the system should fail.
	 */
	@Test
	public void testProxyImplementation_Failure() {
		BVConfiguration bvConfig = new BVSdkConfiguration();
		bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");
		bvConfig.addProperty(BVClientConfig.CLOUD_KEY, "myshco-359c29d8a8cbe3822bc0d7c58cb9f9ca");
		bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "9344seob");
		bvConfig.addProperty(BVClientConfig.PROXY_HOST, "localhost");
		bvConfig.addProperty(BVClientConfig.PROXY_PORT, "9999");
		BVUIContent uiContent = new BVManagedUIContent(bvConfig);
		
		BVParameters bvParameters = new BVParameters();
		bvParameters.setUserAgent("google");
		bvParameters.setPageURI("/someproduct.jsp?bvpage=ctre/id3000001/stp");
		
		String theUIContent = uiContent.getContent(bvParameters);
		System.out.println(theUIContent);
		assertEquals(theUIContent.contains("BVRRSourceID"), false, "there should not be BvRRSourceID in the content");
		assertEquals(theUIContent.contains("connect timed out"), true, 
				"there should be connection timed out message.");
	}
	
	/**
	 * Set a correct proxy server and the system should function
	 */
	@Test
	public void testProxyImplementation_Success() {
		Server proxyServer = startProxyServer();
		
		BVConfiguration bvConfig = new BVSdkConfiguration();
		bvConfig.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");
		bvConfig.addProperty(BVClientConfig.CLOUD_KEY, "myshco-359c29d8a8cbe3822bc0d7c58cb9f9ca");
		bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "9344seob");
		bvConfig.addProperty(BVClientConfig.PROXY_HOST, "localhost");
		bvConfig.addProperty(BVClientConfig.PROXY_PORT, "9999");
		
		BVUIContent uiContent = new BVManagedUIContent(bvConfig);
		
		BVParameters bvParameters = new BVParameters();
		bvParameters.setUserAgent("google");
		bvParameters.setPageURI("/someproduct.jsp?bvpage=ctre/id3000001/stp");
		
		String theUIContent = uiContent.getContent(bvParameters);
		stopProxyServer(proxyServer);
		
		System.out.println(theUIContent);
		assertEquals(theUIContent.contains("bvseo-msg: The resource to the URL or file is currently unavailable."), true, 
				"there should be resource not available message in the content");
		assertEquals(theUIContent.contains("bvseo-msg: Connect to localhost:9999 timed out"), false, 
				"there should not be connection timed out message.");
		
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
//		proxyServer.
		
//		handler.
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
