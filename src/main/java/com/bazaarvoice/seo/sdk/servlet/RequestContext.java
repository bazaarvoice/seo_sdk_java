package com.bazaarvoice.seo.sdk.servlet;

import com.bazaarvoice.seo.sdk.util.BVMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Request data for the current request. Requires configuration of {@link com.bazaarvoice.seo.sdk.servlet.RequestFilter}
 */
public class RequestContext {
  private final static Logger LOGGER = LoggerFactory.getLogger(
    RequestContext.class
  );


  /**
   * @return Iterator of all the header names the current request contains.
   */
  public static Iterator<String> getHeaderNames() {
    Map<String, String> headers = HEADERS_THREAD_LOCAL.get();
    return (validateRequestBasedThreadLocal(headers) ? headers.keySet().iterator() : new HashSet<String>().iterator());
  }

  public static String getHeader(String headerName){
    Map<String, String> headers = HEADERS_THREAD_LOCAL.get();
    return validateRequestBasedThreadLocal(headers) ? headers.get(headerName.toLowerCase()) : null;
  }

  private static boolean validateRequestBasedThreadLocal(Object local){
    if(local != null){
      return true;
    }
    LOGGER.warn(BVMessageUtil.getMessage("WRN0000"));
    return false;
  }

  /**
   * Store header information for this request thread tree.
   * @param request ServletRequest for the current thread.
   * @return true if headers could be set. Otherwise, false. Headers cannot be set if the request is not an HttpServletRequest.
   */
  static boolean setHeaders(ServletRequest request){
    Map<String, String> headers = new HashMap<String, String>();
    if(request instanceof HttpServletRequest) {
      populateHeaderMap(headers, (HttpServletRequest) request);
      HEADERS_THREAD_LOCAL.set(headers);
      return true;
    } else {
      HEADERS_THREAD_LOCAL.set(headers);
      return false;
    }
  }

  private static void populateHeaderMap(Map<String, String> headers, HttpServletRequest request){
    Enumeration<String> headerNames = request.getHeaderNames();
    while(headerNames.hasMoreElements()){
      String headerName = headerNames.nextElement().toLowerCase();
      headers.put(headerName, request.getHeader(headerName));
    }
  }

  static void clearHeaders(){
    HEADERS_THREAD_LOCAL.remove();
  }

  private final static InheritableThreadLocal<Map<String, String>> HEADERS_THREAD_LOCAL;

  static {
    HEADERS_THREAD_LOCAL = new InheritableThreadLocal<Map<String, String>>();
  }
}
