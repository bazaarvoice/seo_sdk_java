package com.bazaarvoice.seo.sdk.servlet;

import com.bazaarvoice.seo.sdk.util.BVMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * When configured in web.xml for javax.servlet, stores incoming requests as inheritable thread locals on the
 * request thread then pass through the request.
 * <p/>
 * Example:
 * <pre>
 * {@code <filter>
 *  <filter-name>BVSeoSdkRequestFilter</filter-name>
 *  <filter-class>com.bazaarvoice.seo.sdk.servlet.RequestFilter</filter-class>
 * </filter>
 * <filter-mapping>
 *  <filter-name>BVSeoSdkRequestFilter</filter-name>
 *  <url-pattern>/*</url-pattern>
 * </filter-mapping>
 * }
 * </pre>
 */
public class RequestFilter implements Filter {

  private final static Logger LOGGER = LoggerFactory.getLogger(
    RequestFilter.class
  );

  public void init(FilterConfig filterConfig) throws ServletException {
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    try {
      if (!RequestContext.setHeaders(servletRequest)) {
        LOGGER.debug(BVMessageUtil.getMessage("MSG0006"));
      }
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {
      RequestContext.clearHeaders();
    }
  }

  public void destroy() {
  }


}