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
  "srd-testcustomer-1-c3a130de760b3105e75e8202cb22e541"
);
// Set to true for staging environment data.
_bvConfig.addProperty(
  BVClientConfig.STAGING,
  "false"
);
// Set to true for testing/qa environment data.
_bvConfig.addProperty(
  BVClientConfig.TESTING,
  "true"
);
// Get this value from BV. This is also known as Display Code.
_bvConfig.addProperty(
  BVClientConfig.BV_ROOT_FOLDER,
  "Main_Site-en_US"
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

// Set the Content Type to REVIEWS
_bvParam.setContentType(ContentType.REVIEWS);
// Override the default setting of PRODUCT with SELLER
_bvParam.setSubjectType(SubjectType.SELLER);

/**
 * baseURI and subjectId must be configured for each page.
 */
// Insert the URI/URL of the page. This is typically the canonical URL.
// The SDK will append pagination parameters to this URI/URL to create
// search-friendly pagination links.
_bvParam.setBaseURI("ExampleSellerRatings.jsp");
// SubjectID must be "seller"
_bvParam.setSubjectId("seller");

BVUIContent _bvOutput = new BVManagedUIContent(_bvConfig);
// getContent returns spotlight content.
String sBvOutputContent = _bvOutput.getContent(_bvParam);
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<body>

  <div id="BVContent">
  <%=sBvOutputContent%>
  </div>

</body>
</html>
