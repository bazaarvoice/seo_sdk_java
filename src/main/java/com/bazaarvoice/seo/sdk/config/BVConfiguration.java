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

/**
 * Configuration Interface to get or set configurations.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public interface BVConfiguration {

	/**
	 * Add configurations to existing configuration(s).
	 * Make sure all the configurations mentioned are added from BVConfig.
	 * Any value that is already added to the configuration will be over written.
	 * 
	 * @param bvConfig
	 * @param propertyValue
	 * @return BVConfiguration
	 */
	BVConfiguration addProperty (BVClientConfig bvConfig, String propertyValue);
	
	/**
	 * Retrieves the value of the property.
	 * This could be retrieved from anywhere.
	 * It could be the property from properties or from hashmap or
	 * from a custom implementation.
	 * 
	 * @param propertyName
	 * @return property from the configuration.
	 */
	String getProperty (String propertyName);
	
}
