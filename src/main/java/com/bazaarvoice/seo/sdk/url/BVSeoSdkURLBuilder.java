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
import com.bazaarvoice.seo.sdk.util.BVConstant;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Builds the proper url to access the bazaarvoice content.
 *
 * @author Anandan Narayanaswamy
 */
public class BVSeoSdkURLBuilder implements BVSeoSdkUrl {

  private static final String BV_PAGE = "bvpage";
  private static final String BV_STATE = "bvstate";
  private static final String NUM_ONE_STR = "1";
  private static final String HTML_EXT = ".htm";

  private BVConfiguration bvConfiguration;
  private BVParameters bvParameters;
  private String fragmentString;
  private String queryString;

  public BVSeoSdkURLBuilder(
    BVConfiguration bvConfiguration,
    BVParameters bvParameters
  ) {
    this.bvConfiguration = bvConfiguration;
    this.bvParameters = bvParameters;
    this.queryString = queryString();
    this.fragmentString = fragmentString();
  }

  /**
   * Corrects the baseUri that is supplied
   *
   * @return String Corrected baseUri.
   */
  public String correctedBaseUri() {
    return BVUtility.removeBVParameters(
      bvParameters.getBaseURI() == null ? "" : bvParameters.getBaseURI()
    );
  }

  /**
   * Returns the fragmentString.
   * Note: Usually server-side doesn't see the fragment but if the client
   * passes in pageURI containing the fragment then we would see this.
   *
   * @return String The fragment string.
   */
  public String fragmentString() {
    if (this.fragmentString == null) {
      this.fragmentString = BVUtility.getFragmentString(bvParameters.getPageURI());
    }
    return this.fragmentString;
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
   * Returns the _escaped_fragment_ value from queryString
   * @return String The _escaped_fragment_ value
   */
  private String getEscapedFragmentValue()
  {
    if (
      queryString != null &&
      queryString.contains(BVConstant.ESCAPED_FRAGMENT_KEY) &&
      !queryString.endsWith(BVConstant.ESCAPED_FRAGMENT_KEY)
    ) {
      return queryString.split(
        BVConstant.ESCAPED_FRAGMENT_KEY
      )[1];
    }
    return "";
  }

  /**
   * forms the url to the seo content.
   * Implementation includes seo content url to load from
   * 1. file system for bvstate
   * 2. http url for bvstate
   * 3. file system for C2013.
   * 4. http url for C2013.
   * 5. file system for PRR.
   * 6. http url for PRR.
   *
   * @throws URISyntaxException
   */
  public URI seoContentUri() {
    String pageURI = bvParameters.getPageURI();

    // Extract content url from bvstate
    if (pageURI != null && pageURI.contains(BV_STATE)) {
      URI uri;
      Pattern pattern = Pattern.compile(BVConstant.BVSTATE_REGEX);
      // Url fragment has highest priority for bvstate
      uri = bvstateMatcherAndURIExtractor(
        pattern,
        fragmentString
      );

      if (uri == null) {
        // Next priority - bvstate in ESCAPED_FRAGMENT
        // Assuming that spec for escaped fragment is being followed and
        // that it is the last query string parameter
        uri = bvstateMatcherAndURIExtractor(
          pattern,
          getEscapedFragmentValue()
        );

        // Finally, try bvstate in query parameter
        if (uri == null) {
          uri = bvstateMatcherAndURIExtractor(
            pattern,
            queryString
          );
        }
      }
      if (uri != null) {
        return uri;
      }
    }

    // Fallback to extract from bvpage
    // We can think about removing this when we no longer support legacy parameters
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

  private URI bvstateMatcherAndURIExtractor(
    Pattern pattern,
    String bvstateInputStr
  )
  {
    if (bvstateInputStr != null && bvstateInputStr.contains(BV_STATE)) {
      Matcher matcher = pattern.matcher(bvstateInputStr);
      if (matcher.find()) {
        return bvstateUri(matcher.group());
      }
    }
    return null;
  }

  /**
   * Get URI from bvstate
   * @param bvstateQueryString input containing bvstate key-value pairs
   * @return URI when bvstate is valid, null otherwise
   */
  private URI bvstateUri(String bvstateQueryString) {
    ContentType contentType = null;
    SubjectType subjectType = null;
    String subjectId = null;
    String pageNumber = null;

    List<NameValuePair> parameters = URLEncodedUtils.parse(
      bvstateQueryString,
      Charset.forName("UTF-8")
    );

    for(NameValuePair parameter : parameters) {
      if (parameter.getName().equals(BV_STATE)) {
        StringTokenizer tokens = new StringTokenizer(
          parameter.getValue(),
          BVConstant.BVSTATE_TOKEN_SEPARATOR_CHAR
        );
        while (tokens.hasMoreTokens()) {
          String token = tokens.nextToken();
          if (token.startsWith("pg")) {
            pageNumber = extractValueFromBVStateKeyValueString(token);
          } else if (token.startsWith("ct")) {
            contentType = ContentType.ctFromBVStateKeyword(
              extractValueFromBVStateKeyValueString(token)
            );
          } else if (token.startsWith("st")) {
            subjectType = SubjectType.subjectType(
              extractValueFromBVStateKeyValueString(token)
            );
          } else if (token.startsWith("id")) {
            subjectId = extractValueFromBVStateKeyValueString(token);
          }
        }
      }
    }

    /**
     * Ignore bvstate values if
     * 1. contentType is missing (or)
     * 2. bvstate contentType doesn't match with bvParameters contentType
     */
    if (
      contentType == null ||
      (
        bvParameters.getContentType() != null &&
        !contentType.uriValue().equalsIgnoreCase(
          bvParameters.getContentType().uriValue()
        )
      )
    ) {
      // when no uri is returned, it falls back to legacy seo parameters
      return null;
    }

    // Defaulting logic if no subjectType is provided
    if (subjectType == null) {
      subjectType = (contentType == ContentType.SPOTLIGHTS)
        ?
        SubjectType.CATEGORY
        :
        SubjectType.PRODUCT;
    }
    subjectId = (StringUtils.isBlank(subjectId))
      ?
      bvParameters.getSubjectId()
      :
      subjectId;
    bvParameters.setSubjectId(subjectId);

    if (
      StringUtils.isBlank(pageNumber) ||
      !StringUtils.isNumeric(pageNumber)
    ) {
      pageNumber = NUM_ONE_STR;
    }
    bvParameters.setPageNumber(pageNumber);

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

  private String extractValueFromBVStateKeyValueString(String keyValueString) {
    if (
      keyValueString.contains(BVConstant.BVSTATE_KEYVALUE_SEPARATOR_CHAR) &&
      !keyValueString.endsWith(BVConstant.BVSTATE_KEYVALUE_SEPARATOR_CHAR)
    ) {
      return keyValueString.split(BVConstant.BVSTATE_KEYVALUE_SEPARATOR_CHAR)[1];
    }
    return "";
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
