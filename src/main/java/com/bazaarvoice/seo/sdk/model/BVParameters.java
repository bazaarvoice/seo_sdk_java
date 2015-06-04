/*
 * ===========================================================================
 * Copyright 2014 Bazaarvoice, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ===========================================================================
 *
 */

package com.bazaarvoice.seo.sdk.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Model for holding the Bazaarvoice content specific query parameters.
 *
 * @author Anandan Narayanaswamy
 */
public class BVParameters {

  private String userAgent;
  private String baseURI;
  private String pageURI;
  private String subjectId;
  private String pageNumber;
  private ContentType contentType;
  private SubjectType subjectType;
  private ContentSubType contentSubType;

  public BVParameters () {
    // Most usage is for content type REVIEWS and subject type PRODUCT, so we
    // default to those values.
    this.setContentType(ContentType.REVIEWS);
    this.setSubjectType(SubjectType.PRODUCT);
  }

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

  public String getPageNumber() {
    return pageNumber;
  }
  public void setPageNumber(String pageNumber) {
    this.pageNumber = pageNumber;
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

  public ContentSubType getContentSubType() {
    return contentSubType;
  }
  public void setContentSubType(ContentSubType contentSubType) {
    this.contentSubType = contentSubType;
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
    .append(getUserAgent(), rhs.getUserAgent())
    .append(getPageNumber(), rhs.getPageNumber())
    .append(getContentSubType(), rhs.getContentSubType());

    return eqBuilder.isEquals();
  }

  @Override
  public int hashCode() {
    HashCodeBuilder hBuilder = new HashCodeBuilder(17, 31);
    hBuilder.append(getUserAgent())
    .append(getBaseURI())
    .append(getPageURI())
    .append(getSubjectId())
    .append(getContentType())
    .append(getSubjectType())
    .append(getContentSubType())
    .append(getPageNumber());

    return hBuilder.hashCode();
  }

}
