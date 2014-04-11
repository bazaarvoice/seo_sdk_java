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
    
//Establish a new BVConfiguration and adjust configurations for your preferences.
BVConfiguration _bvConfig = new BVSdkConfiguration();
		_bvConfig.addProperty(BVClientConfig.SEO_SDK_ENABLED, "true"); // Use this as a kill switch.  The true/false string for this is often passed in from a common utility class.
		_bvConfig.addProperty(BVClientConfig.EXECUTION_TIMEOUT, "1500"); // Adjust as desired.  Values below 1000 are not recommended.
		_bvConfig.addProperty(BVClientConfig.BOT_DETECTION, "false"); // Set to true if user agent/bot detection is desired.
		//_bvConfig.addProperty(BVClientConfig.CRAWLER_AGENT_PATTERN, "msnbot|google|teoma|bingbot|yandexbot|yahoo"); // Use this to override the default list of search engine bot user agents
		
		//the following 3 lines must be updated to not use sample configurations.
		_bvConfig.addProperty(BVClientConfig.CLOUD_KEY, "myshco-69cb945801532dcfb57ad2b0d2471b68"); // Grab this value from the config hub. From the left panel, select "Technical Setup" > "SEO Configuration." This value will be in the "Cloud key" field.
		_bvConfig.addProperty(BVClientConfig.BV_ROOT_FOLDER, "Main_Site-en_US"); // Default value. For other locales, append the appropriate locale to the the deployment zone. For example: Main_Site-pt_BR would be the deployment zone for Brazilian Portuguese.
		
//Create BVParameters for each injection.  If the page contains multiple injections, for example, 
//reviews and questions, set unique parameters for each injection. 
BVParameters _bvParam = new BVParameters();
		_bvParam.setUserAgent(request.getHeader( "User-Agent" ));
		_bvParam.setPageURI(request.getRequestURI() + "?" + request.getQueryString()); // This should be URI/URL of the current page with all URL parameters.
		_bvParam.setContentType(ContentType.REVIEWS); // Set to REVIEWS, QUESTIONS, etc.
		_bvParam.setSubjectType(SubjectType.PRODUCT);
		
		//the following 2 lines must be configured for each page.
		_bvParam.setBaseURI("Example-Myshco.jsp"); // Insert the URI/URL of the page. This is typically the canonical URL.  The SDK will append pagination parameters to this URI/URL to create search-friendly pagination links.
		_bvParam.setSubjectId("5000001"); // Insert the product ID. This will 

BVUIContent _bvOutput = new BVManagedUIContent(_bvConfig);
		//String sBvOutputReviews = _bvOutput.getContent(_bvParam);  // This method returns both the reviews markup and aggregate rating into a single string.  Use this method if there is no summary div.
		String sBvOutputSummary = _bvOutput.getAggregateRating(_bvParam);  // This method returns only the aggregate rating markup.
		String sBvOutputReviews = _bvOutput.getReviews(_bvParam);  // This method returns only the reviews markup with no aggregate rating markup.


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
