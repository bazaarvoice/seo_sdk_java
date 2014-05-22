package com.bazaarvoice.seo.sdk.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton instance of cached thread pool.
 * @author Anandan Narayanaswamy
 *
 */
public final class BVThreadPool {

	private static final ExecutorService executorService = Executors.newCachedThreadPool();
	
	public static ExecutorService getExecutorService() {
		return executorService;
	}
}
