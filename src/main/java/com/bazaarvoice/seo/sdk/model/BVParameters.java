package com.bazaarvoice.seo.sdk.model;

/**
 * Wrapper for holding the Bazaarvoice content specific query parameters.
 * @author Anandan Narayanaswamy
 *
 */
public class BVParameters {

	private String userAgent;
	private String baseURI;
	private String pageURI;
	private String subjectId;
	private ContentType contentType;
	private SubjectType subjectType;
	
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	public String getBaseURI() {
		return baseURI;
	}
	public void setBaseURI(String baseURI) {
		this.baseURI = baseURI;
	}
	
	public String getPageURI() {
		return pageURI;
	}
	public void setPageURI(String pageURI) {
		this.pageURI = pageURI;
	}
	
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	
	public ContentType getContentType() {
		return contentType;
	}
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}
	
	public SubjectType getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(SubjectType subjectType) {
		this.subjectType = subjectType;
	}
	
}
