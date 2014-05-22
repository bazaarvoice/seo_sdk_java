package com.bazaarvoice.seo.sdk.url;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVCoreConfig;
import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentSubType;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;
import com.bazaarvoice.seo.sdk.util.BVUtilty;

/**
 * Builds the proper url to access the bazaarvoice content
 * @author Anandan Narayanaswamy
 *
 */
public class BVSeoSdkURLBuilder implements BVSeoSdkUrl {
	
	private static final String BV_PAGE = "bvpage"; 
	private static final String NUM_ONE_STR = "1";
	private static final String HTML_EXT = ".htm";
	
	private BVConfiguration bvConfiguration;
	private BVParameters bvParameters;
	private String queryString;
	
	public BVSeoSdkURLBuilder(BVConfiguration bvConfiguration, BVParameters bvParameters) {
		this.bvConfiguration = bvConfiguration;
		this.bvParameters = bvParameters;
		this.queryString = queryString();
	}

	/**
	 * Corrects the baseUri that is supplied
	 * @return
	 */
	public String correctedBaseUri() {
		String baseUri = bvParameters.getBaseURI() == null ? "" : bvParameters.getBaseURI();
        
        if (StringUtils.contains(baseUri, "bvrrp") || 
        		StringUtils.contains(baseUri, "bvqap") || 
        		StringUtils.contains(baseUri, "bvsyp") || StringUtils.contains(baseUri, "bvpage")) {
        	 baseUri = BVUtilty.removeBVQuery(baseUri);
        }
        
        return baseUri;
	}
	
	/**
	 * Returns the queryString.
	 */
	public String queryString() {
		if (this.queryString == null) {
			this.queryString = BVUtilty.getQueryString(bvParameters.getPageURI()); 
		}
		return this.queryString;
	}
	
	/**
	 * forms the url to the seo content.
	 * Implementation includes seo content url to load from 
	 * 1. file system for C2013.
	 * 2. http url for C2013.
	 * 3. file system for PRR.
	 * 4. http url for PRR.
	 *
	 * @throws URISyntaxException 
	 */
	public URI seoContentUri() {
		/*
		 * if bvParameters.pageUri contains bvpage then we consider it as C2013 implementation.
		 * TODO: the above in this is a todo.
		 */
		if (queryString != null && queryString.contains(BV_PAGE)) {
			return c2013Uri();
		}
		
		/*
		 * Default we consider it as prr uri.
		 * here goes PRR implementation for file path uri
		 */
		return prrUri();
		
		
	}

	private URI prrUri() {
		String path = getPath(bvParameters.getContentType(), bvParameters.getSubjectType(), getPageNumber(), 
				bvParameters.getSubjectId(), bvParameters.getContentSubType());
		if (isContentFromFile()) {
			return fileUri(path);
		}
		
		return httpUri(path);
	}
	
	private URI fileUri(String path) {
		String fileRoot = bvConfiguration.getProperty(BVClientConfig.LOCAL_SEO_FILE_ROOT.getPropertyName());
		if (StringUtils.isBlank(fileRoot)) {
			throw new BVSdkException("ERR0010");
		}
		
		String fullFilePath = fileRoot + "/" + path;
		File file = new File(fullFilePath);
		return file.toURI();
	}
	
	private URI httpUri(String path) {
		boolean isStaging = Boolean.parseBoolean(bvConfiguration.getProperty(BVClientConfig.STAGING.getPropertyName()));
		boolean isHttpsEnabled = Boolean.parseBoolean(bvConfiguration.getProperty(BVClientConfig.SSL_ENABLED.getPropertyName()));
		
		String s3Hostname = isStaging ? bvConfiguration.getProperty(BVCoreConfig.STAGING_S3_HOSTNAME.getPropertyName()) : 
			bvConfiguration.getProperty(BVCoreConfig.PRODUCTION_S3_HOSTNAME.getPropertyName());
		
        String cloudKey = bvConfiguration.getProperty(BVClientConfig.CLOUD_KEY.getPropertyName());
        String urlPath = "/" + cloudKey + "/" + path;
        URIBuilder builder = new URIBuilder();
        
        String httpScheme = isHttpsEnabled ? "https" : "http";
        
        builder.setScheme(httpScheme).setHost(s3Hostname).setPath(urlPath);
		
        try {
			return builder.build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * TODO: This method can be further optimized. but make sure that the functionality doesn't break.
	 * @return
	 */
	private URI c2013Uri() {
		ContentType contentType = null;
		SubjectType subjectType = null;
		String subjectId = null;
		String pageNumber = null;
		
		List<NameValuePair> parameters = URLEncodedUtils.parse (queryString, Charset.forName("UTF-8"));
        for(NameValuePair parameter : parameters) {
            if (parameter.getName().equals(BV_PAGE)) {
            	StringTokenizer tokens = new StringTokenizer(parameter.getValue(), "/");
            	while (tokens.hasMoreTokens()) {
            		String token = tokens.nextToken();
            		if (token.startsWith("pg")) {
            			pageNumber = getValue(token);
            		} else if (token.startsWith("ct")) {
            			contentType = ContentType.ctFromKeyWord(getValue(token));
            		} else if (token.startsWith("st")) {
            			subjectType = SubjectType.subjectType(getValue(token));
            		} else if (token.startsWith("id")) {
            			subjectId = getValue(token);
            		}
            	}
            }
        }
		
        contentType = (contentType == null) ? bvParameters.getContentType() : contentType;
        subjectType = (subjectType == null) ? bvParameters.getSubjectType() : subjectType;
        subjectId = (StringUtils.isBlank(subjectId)) ? bvParameters.getSubjectId() : subjectId;
        pageNumber = (StringUtils.isBlank(pageNumber)) ? NUM_ONE_STR : pageNumber;
        
        String path = getPath(contentType, subjectType, pageNumber, subjectId, bvParameters.getContentSubType());
		if (isContentFromFile()) {
			return fileUri(path);
		}
		
		return httpUri(path);
	}
	
	private String getValue(String valueString) {
		return valueString.substring(2, valueString.length());
	}

	private String getPath(ContentType contentType, SubjectType subjectType, String pageNumber, String subjectId, ContentSubType contentSubType) {
		StringBuilder path = new StringBuilder();
		path.append(getRootFolder())
		.append(PATH_SEPARATOR)
		.append(contentType.uriValue())
		.append(PATH_SEPARATOR)
		.append(subjectType.uriValue())
		.append(PATH_SEPARATOR)
		.append(pageNumber)
		.append(PATH_SEPARATOR);
		
		if (contentSubType != null && contentSubType != ContentSubType.NONE) {
			path.append(contentSubType.getContentKeyword())
			.append(PATH_SEPARATOR);
		}
		
		path.append(subjectId)
		.append(HTML_EXT);
		
		return path.toString();
	}

	private String getPageNumber() {
		return BVUtilty.getPageNumber(queryString());
	}
	
	private String getRootFolder() {
		return bvConfiguration.getProperty(BVClientConfig.BV_ROOT_FOLDER.getPropertyName());
	}

	private boolean isContentFromFile() {
		boolean loadFromFile = Boolean.parseBoolean(bvConfiguration.
				getProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY.getPropertyName()));
		return loadFromFile;
	}
}
