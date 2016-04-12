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

package com.bazaarvoice.seo.sdk.footer;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVCoreConfig;
import com.bazaarvoice.seo.sdk.exception.BVSdkException;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.SubjectType;
import com.bazaarvoice.seo.sdk.servlet.RequestContext;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkUrl;
import com.bazaarvoice.seo.sdk.util.BVUtility;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.*;

/**
 * Implementation class for adding bazaarvoice footer in the bazaarvoice seo
 * content. This class is designed to display footer in the bazaarvoice seo
 * content in HTML formatted tags.
 *
 */
public class BVHTMLFooter implements BVFooter {

  private static final String LINE_SEPARATOR = System.lineSeparator();

  private BVConfiguration _bvConfiguration;
  private BVParameters _bvParameters;
  private BVSeoSdkUrl _bvSeoSdkUrl;

  private long executionTime;
  private List<String> messageList;

  public BVHTMLFooter(
    BVConfiguration bvConfiguration,
    BVParameters bvParameters
  ) {
    if (bvConfiguration == null) {
      throw new BVSdkException("ERR0007");
    }

    _bvConfiguration = bvConfiguration;
    _bvParameters = bvParameters;

    messageList = new ArrayList<String>();
  }

  /**
   * returns the footer based on the configuration that is set.
   * @return String footer.
   */
  public String displayFooter(String accessMethod) {
    HashMap<String, String> metaDataContext = new LinkedHashMap<String, String>();
    StringBuilder footerBuilder = new StringBuilder();



    String methodType = Boolean.parseBoolean(_bvConfiguration.getProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY.getPropertyName()
    )) ? "LOCAL" : "CLOUD";

    metaDataContext.put("sdk", StringUtils.join(new Object[]{
            "bvseo_sdk",
            "java_sdk",
            "bvseo-" + ResourceBundle.getBundle("sdk").getString("version")
    }, ", "));
    metaDataContext.put("sp_mt", StringUtils.join(new Object[]{
            methodType,
            accessMethod,
            executionTime + "ms"
    }, ", "));
    if(_bvParameters != null) {
      metaDataContext.put("ct_st", StringUtils.join(new Object[]{
              _bvParameters.getContentType(),
              _bvParameters.getSubjectType()
      }, ", "));
    } else {
      metaDataContext.put("ct_st", "");
    }
    String message = getMessage();
    if(StringUtils.isNotBlank(message)) {
      metaDataContext.put("ms", message);
    }

    footerBuilder.append(getSdkMetaDataHtml(metaDataContext));


    if (BVUtility.isRevealDebugEnabled(_bvParameters)) {
      footerBuilder.append(LINE_SEPARATOR);
      footerBuilder.append(LINE_SEPARATOR);
      Map<String, String> debugContext = getRevealMap();
      if(debugContext != null){
        String userAgent = RequestContext.getHeader("User-Agent");
        if(_bvParameters != null) {
          if (StringUtils.isBlank(userAgent)) {
            userAgent = _bvParameters.getUserAgent();
          }
          debugContext.put("userAgent", userAgent);
          debugContext.put("baseURI", _bvParameters.getBaseURI());
          debugContext.put("pageURI", _bvParameters.getPageURI());
          debugContext.put("subjectId", _bvParameters.getSubjectId());
          debugContext.put("contentType", _bvParameters.getContentType().toString());
          debugContext.put("subjectType", _bvParameters.getSubjectType().toString());
        } else {
          if (StringUtils.isBlank(userAgent)) {
            userAgent = "";
          }
          debugContext.put("userAgent", userAgent);
          debugContext.put("baseURI", "");
          debugContext.put("pageURI", "");
          debugContext.put("subjectId", "");
          debugContext.put("contentType", "");
          debugContext.put("subjectType", "");
        }
        String url = getUrl();
        if(StringUtils.isNotBlank(url)) {
          debugContext.put("contentURL", url);
        }
      }
      footerBuilder.append(getDebugDataHtml(debugContext));
    }

    footerBuilder.append(LINE_SEPARATOR);
    return footerBuilder.toString();
  }

  private Map<String, String> getRevealMap() {
    Map<String, String> revealMap = new LinkedHashMap<String, String>();
    String configName;
    if (_bvParameters.getSubjectType() != SubjectType.SELLER) {
      for (BVCoreConfig bvCoreConfig : BVCoreConfig.values()) {
        configName = bvCoreConfig.getPropertyName();
        revealMap.put(configName, _bvConfiguration.getProperty(configName));
      }
    }

    for (BVClientConfig bvClientConfig : BVClientConfig.values()) {
      configName = bvClientConfig.getPropertyName();
      revealMap.put(configName, _bvConfiguration.getProperty(configName));
    }
    return revealMap;
  }

  private String getUrl() {
    String url = null;
    boolean loadFilesLocally = Boolean.valueOf(_bvConfiguration.getProperty(
            BVClientConfig.LOAD_SEO_FILES_LOCALLY.getPropertyName()
    ));
    if (!loadFilesLocally && _bvSeoSdkUrl != null) {
      url = _bvSeoSdkUrl.seoContentUri().toString();
    }
    return url;
  }

  private String getMessage() {
    StringBuilder messageBuilder = new StringBuilder();
    if (messageList != null && !messageList.isEmpty()) {
      messageBuilder.append("bvseo-msg: ");
      for (String messageStr : messageList) {
        messageBuilder.append(messageStr);
      }
    }
    return messageBuilder.toString();
  }

  /**
   * Message format for debug data list.
   * <P><B>Keys</B></P>
   * <OL start="0">
   *   <LI>List Content</LI>
   * </OL>
   */
  private static final String DEBUG_DATA_UL_FORMAT = "<ul id=\"BVSEOSDK_meta_debug\" style=\"display:none !important\">{0}</ul>";

  /**
   * Message format for meta data list.
   * <P><B>Keys</B></P>
   * <OL start="0">
   *   <LI>List Content</LI>
   * </OL>
   */
  private static final String META_DATA_UL_FORMAT = "<ul id=\"BVSEOSDK_meta\" style=\"display:none !important\">{0}</ul>";

  /**
   * Message format for list items in a bv seo data list.
   *
   * <P><B>Keys</B></P>
   * <OL start="0">
   *   <LI>key</LI>
   *   <LI>value</LI>
   * </OL>
   */
  private static final String BV_SEO_LI_FORMAT = "  <li data-bvseo=\"{0}\">{1}</li>";

  /**
   * Get the html content for meta data
   * @param context contains a set of keys and values to add as list items to the html content
   * @return
   */
  private String getSdkMetaDataHtml(Map<String, String> context) {
    return getHtmlList(META_DATA_UL_FORMAT, BV_SEO_LI_FORMAT, context);
  }

  /**
   * Get the html content for debug information
   * @param context contains a set of keys and values to add as list items to the html content
   * @return
   */
  private String getDebugDataHtml(Map<String, String> context) {
    return getHtmlList(DEBUG_DATA_UL_FORMAT, BV_SEO_LI_FORMAT, context);
  }

  private String getHtmlList(String ulFormat, String liFormat, Map<String, String> context){
    StringBuilder dataInnerList = new StringBuilder();
    dataInnerList.append(LINE_SEPARATOR);
    for(String key : context.keySet()){
      dataInnerList.append(
              MessageFormat.format(
                      liFormat,
                      key,
                      StringEscapeUtils.escapeHtml4(
                              StringUtils.defaultIfBlank(context.get(key), "")
                      )
              )
      );
      dataInnerList.append(LINE_SEPARATOR);
    }
    return MessageFormat.format(ulFormat, dataInnerList.toString());
  }

  public void addMessage(String message) {
    if (!StringUtils.isBlank(message)) {
      messageList.add(message);
    }
  }

  public void setExecutionTime(long executionTime) {
    this.executionTime = executionTime;
  }

  public void setBvSeoSdkUrl(BVSeoSdkUrl _bvSeoSdkUrl) {
    this._bvSeoSdkUrl = _bvSeoSdkUrl;
  }

}
