package com.bazaarvoice.seo.sdk;

import com.bazaarvoice.seo.sdk.model.BVParameters;

/**
 * 
 * @author Anandan Narayanaswamy
 *
 */
public interface BVUIContent {

	/**
	 * Gets the complete bazaarvoice seo content.
	 * @param bvQueryParams
	 * @return String:seo content
	 */
	String getContent(BVParameters bvQueryParams);
	
	/**
	 * Gets only the aggregateRating.
	 * @param bvQueryParams
	 * @return String aggregate content.
	 */
	String getAggregateRating(BVParameters bvQueryParams);
	
	/**
	 * Gets only the reviews.
	 * @param bvQueryParams
	 * @return String reviews content.
	 */
	String getReviews(BVParameters bvQueryParams);
	
}
