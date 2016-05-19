package com.bazaarvoice.seo.sdk.servlet;


import com.bazaarvoice.seo.sdk.test.Util;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;

import static org.mockito.Mockito.*;

import com.bazaarvoice.seo.sdk.test.Assert;

/**
 * Set of tests for {@link com.bazaarvoice.seo.sdk.servlet.RequestFilter}
 */
public class RequestFilter_Test {


  HttpServletRequest request;
  ServletResponse response;
  FilterChain chain;

  @BeforeMethod
  public void createServletObjects() {
    request = mock(HttpServletRequest.class);
    response = mock(ServletResponse.class);
    chain = mock(FilterChain.class);
  }

  @DataProvider
  public Object[][] headerSets() {
    return new Object[][]{
      new Object[]{
        new String[]{
          "user-agent",
          "content-type",
          "accept"
        }
      },
      new Object[]{
        new String[]{}
      }
    };
  }

  @Test(dataProvider = "headerSets")
  public void testDoFilter(final String[] headers) throws IOException, ServletException {
    when(request.getHeaderNames()).thenAnswer(new Answer() {
      public Object answer(InvocationOnMock invocation) {
        return Util.getEnumeration(headers);
      }
    });

    doAnswer(new Answer<Void>() {
      public Void answer(InvocationOnMock invocation) {
        HttpServletRequest request = invocation.getArgumentAt(0, HttpServletRequest.class);
        Assert.assertEquals(RequestContext.getHeaderNames(), request.getHeaderNames());
        return null;
      }
    }).when(chain).doFilter(request, response);

    RequestFilter filter = new RequestFilter();
    filter.doFilter(request, response, chain);
    verify(chain, times(1)).doFilter(request, response);
  }

  @Test()
  public void testDoFilterWithBadRequestType() throws IOException, ServletException {
    ServletRequest badRequest = mock(ServletRequest.class);

    doAnswer(new Answer<Void>() {
      public Void answer(InvocationOnMock invocation) {
        Enumeration<String> emptyEnumeration = Collections.enumeration(Collections.<String>emptyList());
        Assert.assertEquals(RequestContext.getHeaderNames(), emptyEnumeration);
        return null;
      }
    }).when(chain).doFilter(badRequest, response);


    RequestFilter filter = new RequestFilter();
    filter.doFilter(badRequest, response, chain);
    verify(chain, times(1)).doFilter(badRequest, response);
  }
}
