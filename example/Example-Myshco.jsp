<%@page import="com.bazaarvoice.seo.sdk.model.ContentType"%>
<%@page import="com.bazaarvoice.seo.sdk.model.SubjectType"%>
<%@page import="com.bazaarvoice.seo.sdk.model.BVParameters"%>
<%@page import="com.bazaarvoice.seo.sdk.BVManagedUIContent"%>
<%@page import="com.bazaarvoice.seo.sdk.BVUIContent"%>
<%@page import="com.bazaarvoice.seo.sdk.config.BVClientConfig"%>
<%@page import="com.bazaarvoice.seo.sdk.config.BVSdkConfiguration"%>
<%@page import="com.bazaarvoice.seo.sdk.config.BVConfiguration"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%><%

/**
 * Establish a new BVConfiguration and adjust configurations for your preferences.
 */
BVConfiguration _bvConfig = new BVSdkConfiguration();
// Use this as a kill switch. This is often passed in from a common utility class.
_bvConfig.addProperty(
  BVClientConfig.SEO_SDK_ENABLED,
  "true"
);
// Adjust as desired.  Values below 1000 are not recommended.
_bvConfig.addProperty(
  BVClientConfig.EXECUTION_TIMEOUT,
  "1500"
);
// Adjust this as required for bot functionality.
_bvConfig.addProperty(
  BVClientConfig.EXECUTION_TIMEOUT_BOT,
  "2000"
);
// Use this to override the default list of search engine bot user agents
//_bvConfig.addProperty(
//  BVClientConfig.CRAWLER_AGENT_PATTERN,
//  "msnbot|google|teoma|bingbot|yandexbot|yahoo"
//);

/**
 * CLOUD_KEY, STAGING and BV_ROOT_FOLDER must be updated to real data instead
 * of using sample information as specified in this example.
 */
// Get this cloudKey value from BV.
_bvConfig.addProperty(
  BVClientConfig.CLOUD_KEY,
  "myshco-3e3001e88d9c32d19a17cafacb81bec7"
);
// Set to true for staging environment data.
_bvConfig.addProperty(
  BVClientConfig.STAGING,
  "true"
);
// Get this value from BV. This is also known as Display Code.
_bvConfig.addProperty(
  BVClientConfig.BV_ROOT_FOLDER,
  "9344"
);

/**
 * Create BVParameters for each injection.  If the page contains multiple
 * injections, for example, reviews and questions, set unique parameters
 * for each injection.
 */
BVParameters _bvParam = new BVParameters();
_bvParam.setUserAgent(request.getHeader( "User-Agent" ));
// This should be URI/URL of the current page with all URL parameters.
_bvParam.setPageURI(request.getRequestURI() + "?" + request.getQueryString());

// Set to REVIEWS, or to QUESTIONS, etc.
//
// The following are the default values. These properties only need to be set
// explicitly if using other values.
//_bvParam.setContentType(ContentType.REVIEWS);
//_bvParam.setSubjectType(SubjectType.PRODUCT);

/**
 * baseURI and subjectId must be configured for each page.
 */
// Insert the URI/URL of the page. This is typically the canonical URL.
// The SDK will append pagination parameters to this URI/URL to create
// search-friendly pagination links.
_bvParam.setBaseURI("Example-Myshco.jsp");
// Insert the product ID
_bvParam.setSubjectId("5000001");

BVUIContent _bvOutput = new BVManagedUIContent(_bvConfig);
// getContent returns both the reviews markup and aggregate rating into a single string.
// Use this method if there is no summary div.
//String sBvOutputReviews = _bvOutput.getContent(_bvParam);
// getAggregateRating returns only the aggregate rating markup.
String sBvOutputSummary = _bvOutput.getAggregateRating(_bvParam);
// getReviews returns only the reviews markup with no aggregate rating markup.
String sBvOutputReviews = _bvOutput.getReviews(_bvParam);
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<body>

  <div id="BVRRSummaryContainer">
  <%=sBvOutputSummary%>
  </div>

  <div id="BVRRContainer">
  <%=sBvOutputReviews%>
  </div>

</body>
</html>
