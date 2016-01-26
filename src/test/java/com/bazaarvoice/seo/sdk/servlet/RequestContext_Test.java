package com.bazaarvoice.seo.sdk.servlet;

import ch.lambdaj.function.matcher.Predicate;
import com.bazaarvoice.seo.sdk.test.Util;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import java.util.*;

import static org.mockito.Mockito.*;

import com.bazaarvoice.seo.sdk.test.Assert;

import static org.testng.Assert.*;
import static ch.lambdaj.Lambda.*;

/**
 * Set of tests for {@link com.bazaarvoice.seo.sdk.servlet.RequestContext}
 */
public class RequestContext_Test {

  @DataProvider
  public Object[][] setHeadersData() {
    return new Object[][]{
      new Object[]{
        requests[0],
        true
      },
      new Object[]{
        requests[1],
        true
      },
      new Object[]{
        requests[2],
        true
      },
      new Object[]{
        requests[3],
        false
      }
    };
  }

  @DataProvider
  public Object[][] headersData() {
    return new Object[][]{
      new Object[]{
        requests[0]
      },
      new Object[]{
        requests[1]
      },
      new Object[]{
        requests[2]
      },
      new Object[]{
        requests[3]
      }
    };
  }

  /**
   * Shared ServletRequests for DataProviders
   */
  private ServletRequest[] requests;

  @BeforeTest
  public void createRequests() {
    requests = new ServletRequest[]{
      getServletRequestWithHeaders(getPresetHeaderMap(3)),
      getServletRequestWithHeaders(getPresetHeaderMap(1)),
      getServletRequestWithHeaders(getPresetHeaderMap(0)),
      mock(ServletRequest.class)
    };
  }

  //<editor-fold desc="Test Data SetUp Helper Methods">
  static final String[] headerNames = new String[]{
    "host",
    "proxy-connection",
    "user-agent"
  };
  static final String[] headerValues = new String[]{
    "doesnotexist.bazaarvoice.com",
    "keep-alive",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36"
  };

  private Map<String, String> getPresetHeaderMap(Integer count) {
    if (count > headerNames.length) {
      throw new IllegalArgumentException(String.format("count must be less than %s", headerNames.length + 1));
    }
    Map<String, String> headers = new HashMap<String, String>();
    for (int i = 0; i < headerNames.length; i++) {
      headers.put(headerNames[i], headerValues[i]);
    }
    return headers;
  }

  private HttpServletRequest getServletRequestWithHeaders(final Map<String, String> headers) {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getHeaderNames()).thenAnswer(new Answer() {
      public Object answer(InvocationOnMock invocation) {
        final Iterator<String> iterator = headers.keySet().iterator();
        return new Enumeration<String>() {
          public boolean hasMoreElements() {
            return iterator.hasNext();
          }

          public String nextElement() {
            return iterator.next();
          }
        };
      }
    });
    when(request.getHeader(anyString())).thenAnswer(new Answer() {
      public Object answer(InvocationOnMock invocation) {
        return headers.get(invocation.getArgumentAt(0, String.class));
      }
    });
    return request;
  }
  //</editor-fold>

  @Test(dataProvider = "setHeadersData")
  public void testSetHeaders(ServletRequest request, boolean expectedReturnValue) {
    boolean success = RequestContext.setHeaders(request);
    assertEquals(success, expectedReturnValue);
  }

  @Test(dataProvider = "headersData")
  public void testGetHeaderNames(ServletRequest request) {
    boolean success = RequestContext.setHeaders(request);
    if (success) {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      Assert.assertEquals(RequestContext.getHeaderNames(), httpRequest.getHeaderNames());
    } else {
      Assert.assertEmpty(RequestContext.getHeaderNames());
    }
  }

  @Test(dataProvider = "headersData")
  public void testGetHeader(ServletRequest request) {
    RequestContext.setHeaders(request);
    final Collection<String> headers = Util.makeCollection(RequestContext.getHeaderNames());
    HttpServletRequest httpRequest = request instanceof HttpServletRequest ? (HttpServletRequest) request : null;
    if (httpRequest == null) {
      Assert.assertEmpty(headers);
    } else {
      for (String headerName : headers) {
        assertEquals(RequestContext.getHeader(headerName), httpRequest.getHeader(headerName));
      }
    }
    for (String headerName : filter(new Predicate<String>() {
      public boolean apply(String element) {
        return !headers.contains(element);
      }
    }, headerNames)) {
      assertNull(RequestContext.getHeader(headerName));
    }
  }
}

