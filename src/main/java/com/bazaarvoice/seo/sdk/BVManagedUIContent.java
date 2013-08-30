package com.bazaarvoice.seo.sdk;

import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVCoreConfig;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkURLBuilder;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkUrl;
import com.bazaarvoice.seo.sdk.util.BVMessageUtil;
import com.bazaarvoice.seo.sdk.util.BVUtilty;
import com.bazaarvoice.seo.sdk.validation.BVParameterValidator;
import com.bazaarvoice.seo.sdk.validation.BVValidator;

/**
 * Implementation class for BVUIContent.
 * This class is the default implementation class to get Bazaarvoice content.
 * Based on the configurations that are set, the actual contents will be retrieved.
 * 
 * Following are the test classes/cases that this class bound to:
 * ensure test cases are not affected by the changes.
 * Refer to individual test case for an explanation.
 * Most of the test case are use case based implementation.
 * 
 * List of test cases associated with this class "BVManagedUIContent"
 * 1. BVManagedUIContentTest.java
 * 2. BVManagedUIContent1_Test.java
 * 3. 
 * 
 * @author Anandan Narayanaswamy
 *
 */
public class BVManagedUIContent implements BVUIContent {

	private final static Logger _logger = LoggerFactory.getLogger(BVManagedUIContent.class);
	private static final String BVREVEAL = "bvreveal";
	private static final String DBG_BVREVEAL = "bvreveal=debug";
	private static final String JS_DISPLAY_MSG = "JavaScript-only Display";
	private static final String INCLUDE_PAGE_URI = "{INSERT_PAGE_URI}";
	
	private BVConfiguration _bvConfiguration;
	private BVValidator bvParamValidator;
	private BVSeoSdkUrl bvSeoSdkUrl;
	
	/**
	 * Default constructor.
	 * Loads all default configuration within.
	 * 
	 */
	public BVManagedUIContent() {
		this(null);
	}
	
	/**
	 * Constructor with BVConfiguration argument.
	 * 
	 * @param bvConfiguration	The configuration/settings that has to be supplied for BVcontent to work.
	 */
	public BVManagedUIContent(BVConfiguration bvConfiguration) {
		this._bvConfiguration = bvConfiguration;
		
		if (bvConfiguration == null) {
			this._bvConfiguration = new BVSdkConfiguration();
		}
		
		bvParamValidator = new BVParameterValidator();
	}
	
	/**
	 * Searches for the bv managed content based on the parameters that are passed.
	 *  
	 */
	public String getContent(BVParameters bvParameters) {
		
		/*
		 * Check if sdk is enabled or not and additionally check if queryString does not contain BVREVEAL string
		 */
		boolean isSdkEnabled = Boolean.parseBoolean(_bvConfiguration.getProperty(BVClientConfig.SEO_SDK_ENABLED.getPropertyName()));
		
		/*
		 * Validator to check if all the bvParameters are valid.
		 */
		bvParamValidator.validate(bvParameters);
		
		bvSeoSdkUrl = new BVSeoSdkURLBuilder(_bvConfiguration, bvParameters);
		String queryString = bvSeoSdkUrl.queryString();
		
		if (!isSdkEnabled && !queryString.contains(BVREVEAL)) {
			_logger.info(BVMessageUtil.getMessage("MSG0003"));
			return "";
		}
		
		
		long startTime = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		String bvRootFolder = _bvConfiguration.getProperty(BVClientConfig.BV_ROOT_FOLDER.getPropertyName());
		String version = _bvConfiguration.getProperty(BVCoreConfig.VERSION.getPropertyName());
		boolean isBotDetection = Boolean.parseBoolean(_bvConfiguration.getProperty(BVClientConfig.BOT_DETECTION.getPropertyName()));
		
//		int page = BVUtilty.getPageNumber(queryString);
		String displayJSOnly = null;
		StringBuilder message = new StringBuilder();
		try {
			//includes integration script if one is enabled.
			includeIntegrationCode(sb, bvParameters.getContentType(), bvParameters.getSubjectType(), bvParameters.getSubjectId());
			
			/*
			 * Hit only when botDetection is disabled or if the queryString is appended with bvreveal or if it matches any 
			 * crawler pattern that is configured at the client configuration. 
			 */
			if (!isBotDetection || queryString.contains(BVREVEAL) || showUserAgentSEOContent(bvParameters.getUserAgent())) {
				URI seoContentUrl = bvSeoSdkUrl.seoContentUri();
				String correctedBaseUri = bvSeoSdkUrl.correctedBaseUri();
				getBvContent(sb, seoContentUrl, correctedBaseUri);
				if (queryString.contains(DBG_BVREVEAL)) {
					boolean isStaging = Boolean.parseBoolean(_bvConfiguration.getProperty(BVClientConfig.STAGING.getPropertyName()));
					addDebugMsg(sb, bvParameters, queryString, isBotDetection, isStaging, seoContentUrl.toString());
				}
			} else {
				displayJSOnly = JS_DISPLAY_MSG;
			}
        } catch (BVSdkException e) {
//            return composeLog(bvRootFolder, version, "Error: " + e.getMessage());
        	message.append("msg:").append(e.getMessage()).append("|");
        }
		
		long endTime = System.currentTimeMillis();
		
		
		message.append("timer:").append(Long.toString(endTime - startTime) + "ms");
		if (displayJSOnly != null) {
			message.append("|").append(displayJSOnly);
		}
		
		//Used to output the total time taken to get the contents, also displays client and api information
		sb.append(composeLog(bvRootFolder, version, message.toString()));
		
		return sb.toString();
	}

	private void addDebugMsg(StringBuilder sb, BVParameters bvParameters, String queryString, boolean isBotDetection, boolean isStaging, String seoContentUrl) {
		sb.append("\n    userAgent: ").append(bvParameters.getUserAgent())
    	.append("\n    baseURL: ").append(bvParameters.getBaseURI())
    	.append("\n    seoContentUrl: ").append(seoContentUrl)
    	.append("\n    seo.sdk.enabled: ").append(_bvConfiguration.getProperty(BVClientConfig.SEO_SDK_ENABLED.getPropertyName()))
        .append("\n    queryString: ").append(queryString)
        .append("\n    contentType: ").append(bvParameters.getContentType())
        .append("\n    subjectType: ").append(bvParameters.getSubjectType())
        .append("\n    subjectId: ").append(bvParameters.getSubjectId())
        .append("\n    staging: ").append(Boolean.toString(isStaging))
        .append("\n    pattern: ")
        .append(_bvConfiguration.getProperty(BVClientConfig.CRAWLER_AGENT_PATTERN.getPropertyName()))
        .append("\n    detectionEnabled: ").append(isBotDetection).append("\n");
	}

	private void getBvContent(StringBuilder sb, URI seoContentUrl, String baseUri) {
		
        if (isContentFromFile()) {
        	sb.append(loadContentFromFile(seoContentUrl));
        } else {
        	sb.append(loadContentFromHttp(seoContentUrl));
        }
       
        BVUtilty.replaceString(sb, INCLUDE_PAGE_URI, baseUri + (baseUri.contains("?") ? "&" : "?"));
        
	}

	private boolean showUserAgentSEOContent(String userAgent) {
		if (userAgent == null) {
			return false;
		}
		
        String crawlerAgentPattern = _bvConfiguration.getProperty(BVClientConfig.CRAWLER_AGENT_PATTERN.getPropertyName());
        Pattern pattern = Pattern.compile(crawlerAgentPattern, Pattern.CASE_INSENSITIVE);

        return (pattern.matcher(userAgent).matches() || userAgent.toLowerCase().contains("google"));
    }
	
	private void includeIntegrationCode(StringBuilder sb, ContentType contentType, SubjectType subjectType, String subjectId) {
		String includeScriptStr = _bvConfiguration.getProperty(BVClientConfig.INCLUDE_DISPLAY_INTEGRATION_CODE.getPropertyName());
		boolean includeIntegrationScript = Boolean.parseBoolean(includeScriptStr);
		
		if (!includeIntegrationScript) {
			return;
		}
		
        Object[] params = {subjectType.uriValue(), subjectId};
        String integrationScriptValue = _bvConfiguration.getProperty(contentType.getIntegrationScriptProperty());
        String integrationScript = MessageFormat.format(integrationScriptValue, params); 
        
        sb.append(integrationScript);
    }

	private String loadContentFromHttp(URI path) {
        
        int connectionTimeout = Integer.parseInt(_bvConfiguration.getProperty(BVClientConfig.CONNECT_TIMEOUT.getPropertyName()));
        int socketTimeout = Integer.parseInt(_bvConfiguration.getProperty(BVClientConfig.SOCKET_TIMEOUT.getPropertyName()));
		String content = null;
		try {
			content = Request.Get(path).connectTimeout(connectionTimeout).
					socketTimeout(socketTimeout).execute().returnContent().asString();
		} catch (ClientProtocolException e) {
			throw new BVSdkException("ERR0012");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return content;
	}

	private String loadContentFromFile(URI path) {
		
		String content = null;
		try {
			content = BVUtilty.readFile(path);
		} catch (IOException e) {
			throw new BVSdkException("ERR0012");
		}
		
		return content;
	}
	
	/**
	 * @deprecated should be removed at the time of cleanup
	 * @param path
	 * @return
	 */
	private String loadContentFromFile(String path) {
		String fileRoot = _bvConfiguration.getProperty(BVClientConfig.LOCAL_SEO_FILE_ROOT.getPropertyName());
		if (StringUtils.isBlank(fileRoot)) {
			throw new BVSdkException("ERR0010");
		}
		
		String fullFilePath = fileRoot + "/" + path;
		String content = null;
		try {
			content = BVUtilty.readFile(fullFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return content;
	}

	private String composeLog(String deploymentZoneId, String version, String message) {
		return "\n<!--BVSEO|dz:" + deploymentZoneId + "|sdk:v" + version + "-j|" + message + "-->\n";
	}
	
	public String getContent(BVConfiguration bvConfig, BVParameters bvParameters) {
		if (bvConfig == null) {
			throw new BVSdkException("ERR0007");
		}
		
		return getContent(bvParameters);
	}
	
	private boolean isContentFromFile() {
		boolean loadFromFile = Boolean.parseBoolean(_bvConfiguration.
				getProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY.getPropertyName()));
		return loadFromFile;
	}
}
