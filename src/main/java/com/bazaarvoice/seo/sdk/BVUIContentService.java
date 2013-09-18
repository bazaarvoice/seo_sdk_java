package com.bazaarvoice.seo.sdk;

import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkUrl;

/**
 * BVUIContentCaller an implementation class of Callable.
 * Once the caller is invoked, any message that is part of the 
 * @author Anandan Narayanaswamy
 *
 */
public interface BVUIContentService {
	
	/**
	 * Implementation to check if sdk is enabled/disabled.
	 * The settings are based on the configurations from BVConfiguration and BVParameters.
	 * @return true if sdk is enabled and false if it is enabled.
	 */
	boolean isSdkEnabled();
	
	/**
	 * Executes the server side call or the file level call within a specified execution timeout.
	 * when reload is set true then it gives from the cache that was already executed in the previous call.
	 * this reduces the number hit to the server.
	 * 
	 * Any related message after execution is stored in getMessage which can be retrieved for later use.
	 * 
	 * @param reload
	 * @return StringBuilder representation of the content.
	 */
	StringBuilder executeCall(boolean reload);
	
	/**
	 * Gets the messages if there are any after executeCall is invoked or if it is still in the cache.
	 * @return Messages if there are any else blank.
	 */
	StringBuilder getMessage();
	
	/**
	 * boolean to check if content should be displaed for the user agent set in bvParameters.
	 * @return
	 */
	boolean showUserAgentSEOContent();
	
	void setBVParameters(BVParameters bvParameters);
	
	void setBVSeoSdkUrl(BVSeoSdkUrl bvSeoSdkUrl);

}
