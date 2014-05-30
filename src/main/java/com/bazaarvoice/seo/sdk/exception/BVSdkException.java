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

package com.bazaarvoice.seo.sdk.exception;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.bazaarvoice.seo.sdk.util.BVMessageUtil;

/**
 * Bazaarvoice sdk exception class.
 * 
 * @author Anandan Narayanaswamy
 *
 */
public class BVSdkException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorCode;
	
	public BVSdkException(String errorCode) {
		super();
		this.errorCode = errorCode;
	}
	
	public BVSdkException(String errorCode, IOException e) {
		super(e);
		this.errorCode = errorCode;
	}

	@Override
	public String getMessage() {
		if (StringUtils.isNotBlank(errorCode)) {
			return BVMessageUtil.getMessage(errorCode);
		}
		
		return super.getMessage();
	}
	
}
