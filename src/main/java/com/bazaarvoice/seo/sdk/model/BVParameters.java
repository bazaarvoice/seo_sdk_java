package com.bazaarvoice.seo.sdk.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj == this) {
			return true;
		}
		
		if (!(obj instanceof BVParameters)) {
			return false;
		}
		
		BVParameters rhs = (BVParameters) obj;
		EqualsBuilder eqBuilder = new EqualsBuilder();
		eqBuilder.append(getBaseURI(), rhs.getBaseURI())
		.append(getContentType(), rhs.getContentType())
		.append(getPageURI(), rhs.getPageURI())
		.append(getSubjectId(), rhs.getSubjectId())
		.append(getSubjectType(), rhs.getSubjectType())
		.append(getUserAgent(), rhs.getUserAgent());
		
		return eqBuilder.isEquals();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hBuilder = new HashCodeBuilder(17, 31);
		hBuilder.append(userAgent)
		.append(baseURI)
		.append(pageURI)
		.append(subjectId)
		.append(contentType)
		.append(subjectType);
		
		return hBuilder.hashCode();
	}

}
