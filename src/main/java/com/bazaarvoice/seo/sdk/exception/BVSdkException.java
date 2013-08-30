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
