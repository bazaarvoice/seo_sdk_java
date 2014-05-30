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

package com.bazaarvoice.seo.sdk.util;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.exception.BVSdkException;

/**
 * Test case for MessageUtil class.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public class BVSdkMessageUtilTest {

	/**
	 * Tests to check if the message classes are able to get the messages
	 * 
	 */
	@Test
	public void testMessages() {
		/*
		 * Null message code
		 */
		String messageCode = null;
		String message = null;
		String errorMessage = null;
		try {
			message = BVMessageUtil.getMessage(messageCode);
		} catch (BVSdkException bvExc) {
			errorMessage = bvExc.getMessage();
		}
		assertNull(message, "message should be null here");
		assertNotNull(errorMessage, "There should be an error message in errorMessage");
		
		/*
		 * Empty message code
		 */
		messageCode = "";
		errorMessage = null;
		try {
			message = BVMessageUtil.getMessage(messageCode);
		} catch (BVSdkException bvExc) {
			errorMessage = bvExc.getMessage();
		}
		assertNull(message, "message should be null here");
		assertNotNull(errorMessage, "There should be an error message in errorMessage");
		
		/*
		 * Invalid message code
		 */
		messageCode = "INVALID_CODE";
		errorMessage = null;
		try {
			message = BVMessageUtil.getMessage(messageCode);
		} catch (BVSdkException bvExc) {
			errorMessage = bvExc.getMessage();
			fail("There was an error please check the exception which should have not occured.");
		}
		assertSame(message, messageCode, "message should be same as messageCode");
		assertNull(errorMessage, "There should not be an error message in errorMessage");
		
		/*
		 * Valid message code
		 */
		messageCode = "MSG0000";
		errorMessage = null;
		try {
			message = BVMessageUtil.getMessage(messageCode);
		} catch (BVSdkException bvExc) {
			errorMessage = bvExc.getMessage();
		}
		assertNotNull(message, "message should not be null.");
		assertNull(errorMessage, "There should not be an error message in errorMessage");
	}
	
	
	/**
	 * Test case ensures that the MessageUtil class is a singleton class.
	 */
	@Test
	public void testSingletonInstance() {
		Object theInstance = null;
		try {
			theInstance = Class.forName(BVMessageUtil.class.getName()).newInstance();
			fail("Singleton class creation failed. ");
		} catch (ClassNotFoundException e) {
			fail("Threw ClassNotFoundException which is incorrect.");
		} catch (InstantiationException e) {
			fail("Threw InstantiationException which is incorrect.");
		} catch (IllegalAccessException e) {
			//There will be an exception here.
		}
		assertNull(theInstance, "theInstance object should be null since it should singleton");
	}
}
