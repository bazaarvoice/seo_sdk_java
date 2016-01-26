package com.bazaarvoice.seo.sdk.servlet;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collections;
import java.util.Enumeration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Initialize a default request context for Content Tests
 */
public class DefaultRequestContext {

  private final static Logger LOGGER = LoggerFactory.getLogger(
    DefaultRequestContext.class
  );

  public static void initialize(){
    HttpServletRequest request = mock(HttpServletRequest.class);

    when(request.getHeaderNames()).thenAnswer(new Answer<Enumeration<String>>(){
      public Enumeration<String> answer(InvocationOnMock invocationOnMock) throws Throwable {
        return Collections.emptyEnumeration();
      }
    });

    try {
      RequestContext.setHeaders(request);
    } catch (Exception ex){
      LOGGER.warn(ex.getMessage());
    }
  }
}
