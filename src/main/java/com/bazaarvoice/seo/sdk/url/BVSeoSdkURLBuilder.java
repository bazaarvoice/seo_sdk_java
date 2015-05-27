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

package com.bazaarvoice.seo.sdk.url;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVCoreConfig;
import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentSubType;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;
import com.bazaarvoice.seo.sdk.util.BVUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Builds the proper url to access the bazaarvoice content.
 *
 * @author Anandan Narayanaswamy
 */
public class BVSeoSdkURLBuilder implements BVSeoSdkUrl {

  private static final String BV_PAGE = "bvpage";
  private static final String NUM_ONE_STR = "1";
  private static final String HTML_EXT = ".htm";

  private BVConfiguration bvConfiguration;
  private BVParameters bvParameters;
  private String queryString;

  public BVSeoSdkURLBuilder(
    BVConfiguration bvConfiguration,
    BVParameters bvParameters
  ) {
    this.bvConfiguration = bvConfiguration;
    this.bvParameters = bvParameters;
    this.queryString = queryString();
  }

  /**
   * Corrects the baseUri that is supplied
   *
   * @return String Corrected baseUri.
   */
  public String correctedBaseUri() {
    String baseUri = bvParameters.getBaseURI() == null ? "" : bvParameters.getBaseURI();

    if (
      StringUtils.contains(baseUri, "bvrrp") ||
      StringUtils.contains(baseUri, "bvqap") ||
      StringUtils.contains(baseUri, "bvsyp") ||
      StringUtils.contains(baseUri, "bvpage")
    ) {
       baseUri = BVUtility.removeBVQuery(baseUri);
    }

    return baseUri;
  }

  /**
   * Returns the queryString.
   *
   * @return String The query string.
   */
  public String queryString() {
    if (this.queryString == null) {
      this.queryString = BVUtility.getQueryString(bvParameters.getPageURI());
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
    String path = getPath(
      bvParameters.getContentType(),
      bvParameters.getSubjectType(),
      getPageNumber(),
      bvParameters.getSubjectId(),
      bvParameters.getContentSubType()
    );

    if (isContentFromFile()) {
      return fileUri(path);
    }

    return httpUri(path);
  }

  private URI fileUri(String path) {
    String fileRoot = bvConfiguration.getProperty(
      BVClientConfig.LOCAL_SEO_FILE_ROOT.getPropertyName()
    );
    if (StringUtils.isBlank(fileRoot)) {
      throw new BVSdkException("ERR0010");
    }

    String fullFilePath = fileRoot + "/" + path;
    File file = new File(fullFilePath);
    return file.toURI();
  }

  private URI httpUri(String path) {
    boolean isTesting = Boolean.parseBoolean(bvConfiguration.getProperty(
      BVClientConfig.TESTING.getPropertyName()
    ));
    boolean isStaging = Boolean.parseBoolean(bvConfiguration.getProperty(
      BVClientConfig.STAGING.getPropertyName()
    ));
    boolean isHttpsEnabled = Boolean.parseBoolean(bvConfiguration.getProperty(
      BVClientConfig.SSL_ENABLED.getPropertyName()
    ));


    String s3Hostname;
    if (isTesting)
    {
      s3Hostname = isStaging ? bvConfiguration.getProperty(
        BVCoreConfig.TESTING_STAGING_S3_HOSTNAME.getPropertyName()
      ) : bvConfiguration.getProperty(
        BVCoreConfig.TESTING_PRODUCTION_S3_HOSTNAME.getPropertyName()
      );
    }
    else
    {
      s3Hostname = isStaging ? bvConfiguration.getProperty(
        BVCoreConfig.STAGING_S3_HOSTNAME.getPropertyName()
      ) : bvConfiguration.getProperty(
        BVCoreConfig.PRODUCTION_S3_HOSTNAME.getPropertyName()
      );
    }
    String cloudKey = bvConfiguration.getProperty(
      BVClientConfig.CLOUD_KEY.getPropertyName()
    );

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
   * TODO: This method can be further optimized. But make sure that the
   * functionality doesn't break.
   */
  private URI c2013Uri() {
    ContentType contentType = null;
    SubjectType subjectType = null;
    String subjectId = null;

    List<NameValuePair> parameters = URLEncodedUtils.parse(
      queryString,
      Charset.forName("UTF-8")
    );

    for(NameValuePair parameter : parameters) {
      if (parameter.getName().equals(BV_PAGE)) {
        StringTokenizer tokens = new StringTokenizer(parameter.getValue(), "/");
        while (tokens.hasMoreTokens()) {
          String token = tokens.nextToken();
          if (
            token.startsWith("pg") &&
            StringUtils.isBlank(bvParameters.getPageNumber())
          ) {
            bvParameters.setPageNumber(getValue(token));
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

    contentType = (contentType == null) ?
      bvParameters.getContentType() : contentType;

    subjectType = (subjectType == null) ?
      bvParameters.getSubjectType() : subjectType;

    subjectId = (StringUtils.isBlank(subjectId)) ?
      bvParameters.getSubjectId() : subjectId;

    if (StringUtils.isBlank(bvParameters.getPageNumber())) {
      bvParameters.setPageNumber(NUM_ONE_STR);
    }

    String path = getPath(
      contentType,
      subjectType,
      bvParameters.getPageNumber(),
      subjectId,
      bvParameters.getContentSubType()
    );

    if (isContentFromFile()) {
      return fileUri(path);
    }

    return httpUri(path);
  }

  private String getValue(String valueString) {
    return valueString.substring(2, valueString.length());
  }

  private String getPath(
    ContentType contentType,
    SubjectType subjectType,
    String pageNumber,
    String subjectId,
    ContentSubType contentSubType
  ) {
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
      path.append(contentSubType.getContentKeyword()).append(PATH_SEPARATOR);
    }

    path.append(subjectId).append(HTML_EXT);

    return path.toString();
  }

  private String getPageNumber() {
    String pageNumber = bvParameters.getPageNumber();
    if (StringUtils.isBlank(pageNumber)) {
      pageNumber = BVUtility.getPageNumber(queryString());
      bvParameters.setPageNumber(pageNumber);
    }
    return pageNumber;
  }

  private String getRootFolder() {
    return bvConfiguration.getProperty(
      BVClientConfig.BV_ROOT_FOLDER.getPropertyName()
    );
  }

  private boolean isContentFromFile() {
    boolean loadFromFile = Boolean.parseBoolean(bvConfiguration.getProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY.getPropertyName())
    );
    return loadFromFile;
  }
}
