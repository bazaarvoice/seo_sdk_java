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

package com.bazaarvoice.seo.sdk.validation;

import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.model.BVParameters;

/**
 * Validation interface to validate the attributes in BVParameters.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public interface BVValidator {

	/**
	 * Method to validate bvConfiguration & bvParameters.
	 * 
	 * @param bvConfigration	the bvConfiguration object.
	 * @param bvParams			the bvParameter object.
	 * @return String errors if invalid attribute is found.
	 */
	String validate(BVConfiguration bvConfiguration, BVParameters bvParams) throws BVSdkException;
	
}
