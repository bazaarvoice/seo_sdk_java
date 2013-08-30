package com.bazaarvoice.seo.sdk.url;

import java.net.URI;

/**
 * Interface to frame bazaarvoice url.
 * @author Anandan Narayanaswamy
 *
 */
public interface BVSeoSdkUrl {

	String PATH_SEPARATOR = "/";
	
	/**
	 * Forms either an http URL or file URL to access the content.
	 * @return URI of the seoContent
	 */
	URI seoContentUri();
	
	/**
	 * retrieves the query string.
	 * @return queryString.
	 */
	String queryString();
	
	/**
	 * Corrects the base uri from bvParameters and returns the baseUri which is corrected.
	 * @return corrected base uri from bvParameters.
	 */
	String correctedBaseUri();
}
