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

package com.bazaarvoice.seo.sdk;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.footer.BVFooter;
import com.bazaarvoice.seo.sdk.footer.BVHTMLFooter;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkURLBuilder;
import com.bazaarvoice.seo.sdk.url.BVSeoSdkUrl;
import com.bazaarvoice.seo.sdk.util.BVMessageUtil;
import com.bazaarvoice.seo.sdk.validation.BVDefaultValidator;
import com.bazaarvoice.seo.sdk.validation.BVValidator;

/**
 * Implementation class for BVUIContent.
 *
 * This class is the default implementation class to get Bazaarvoice content.
 * Based on the configurations that are set, the actual contents will be r
 * etrieved.
 *
 * @author Anandan Narayanaswamy
 */
public class BVManagedUIContent implements BVUIContent {

  private final static Logger _logger = LoggerFactory.getLogger(
    BVManagedUIContent.class
  );

  private BVConfiguration _bvConfiguration;
  private BVValidator bvParamValidator;

  private BVSeoSdkUrl bvSeoSdkUrl;
  private BVFooter bvFooter;
  private StringBuilder message;
  private BVParameters bvParameters;
  private boolean reloadContent;
  private BVUIContentService bvUiContentService;
  private String validationError;

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
   * @param bvConfiguration The configuration instance.
   */
  public BVManagedUIContent(BVConfiguration bvConfiguration) {
    this._bvConfiguration = bvConfiguration;

    if (bvConfiguration == null) {
      this._bvConfiguration = new BVSdkConfiguration();
    }
  }

  /**
   * Searches for the bv managed content based on the parameters that are
   * passed.
   */
  public String getContent(BVParameters bvParameters) {
    long startTime = System.currentTimeMillis();
    postProcess(bvParameters);

    StringBuilder uiContent = null;

    if (StringUtils.isBlank(validationError)) {
      if (bvUiContentService.isSdkEnabled()) {
        uiContent = bvUiContentService.executeCall(reloadContent);
      } else {
        _logger.info(BVMessageUtil.getMessage("MSG0003"));
        uiContent = new StringBuilder();
      }
      bvFooter.addMessage(bvUiContentService.getMessage().toString());
    } else {
      uiContent = new StringBuilder();
      bvFooter.addMessage(validationError);
    }

    bvFooter.setExecutionTime(System.currentTimeMillis() - startTime);

    if (uiContent == null) {
      uiContent = new StringBuilder();
    }

    uiContent.append(bvFooter.displayFooter("getContent"));

    return uiContent.toString();
  }

  public String getAggregateRating(BVParameters bvQueryParams) {
    long startTime = System.currentTimeMillis();
    postProcess(bvQueryParams);

    StringBuilder uiContent = null;
    if (StringUtils.isBlank(validationError)) {
      if (bvUiContentService.isSdkEnabled()) {
        uiContent = bvUiContentService.executeCall(reloadContent);
      } else {
        _logger.info(BVMessageUtil.getMessage("MSG0003"));
        uiContent = new StringBuilder();
      }

      if (uiContent != null) {
        int startIndex = uiContent.indexOf("<!--begin-reviews-->");
        if (startIndex != -1) {
          String endReviews = "<!--end-reviews-->";
          int endIndex = uiContent.indexOf(endReviews) + endReviews.length();
          uiContent.delete(startIndex, endIndex);

          startIndex = uiContent.indexOf("<!--begin-pagination-->");
          if (startIndex != -1) {
            String endPagination = "<!--end-pagination-->";
            endIndex = uiContent.indexOf(endPagination) + endPagination.length();
            uiContent.delete(startIndex, endIndex);
          }
        }

        startIndex = uiContent.indexOf("<!--begin-aggregate-rating-->");
        if (startIndex == -1 && bvUiContentService.getMessage().length() == 0) {
          message.append(BVMessageUtil.getMessage("ERR0003"));
        }
      } else {
        uiContent = new StringBuilder();
      }

      bvFooter.addMessage(bvUiContentService.getMessage().toString());
    } else {
      uiContent = new StringBuilder();
      bvFooter.addMessage(validationError);
    }


    bvFooter.addMessage(message.toString());
    bvFooter.setExecutionTime(System.currentTimeMillis() - startTime);
    uiContent.append(bvFooter.displayFooter("getAggregateRating"));

    return uiContent.toString();
  }

  public String getReviews(BVParameters bvQueryParams) {
    long startTime = System.currentTimeMillis();
    postProcess(bvQueryParams);

    StringBuilder uiContent = null;
    if (StringUtils.isBlank(validationError)) {

      if (bvUiContentService.isSdkEnabled()) {
        uiContent = bvUiContentService.executeCall(reloadContent);
      } else {
        _logger.info(BVMessageUtil.getMessage("MSG0003"));
        uiContent = new StringBuilder();
      }

      if (uiContent != null) {
        int startIndex = uiContent.indexOf("<!--begin-aggregate-rating-->");

        if (startIndex != -1) {
          String endReviews = "<!--end-aggregate-rating-->";
          int endIndex = uiContent.indexOf(endReviews) + endReviews.length();
          uiContent.delete(startIndex, endIndex);
        }

        startIndex = uiContent.indexOf("<!--begin-reviews-->");
        if (startIndex == -1 && bvUiContentService.getMessage().length() == 0) {
          message.append(BVMessageUtil.getMessage("ERR0013"));
        }
      } else {
        uiContent = new StringBuilder();
      }

      bvFooter.addMessage(bvUiContentService.getMessage().toString());
      bvFooter.addMessage(message.toString());
    } else {
      uiContent = new StringBuilder();
      bvFooter.addMessage(validationError);
    }

    /*
     * Remove schema.org text from reviews if one exists
     * itemscope itemtype="http://schema.org/Product"
     */
    String schemaOrg = "itemscope itemtype=\"http://schema.org/Product\"";
    int startIndex = uiContent.indexOf(schemaOrg);
    if (startIndex != -1) {
      uiContent.delete(startIndex, startIndex + schemaOrg.length());
    }

    bvFooter.setExecutionTime(System.currentTimeMillis() - startTime);
    uiContent.append(bvFooter.displayFooter("getReviews"));

    return uiContent.toString();
  }

  private void postProcess(BVParameters bvParameters) {
    bvFooter = new BVHTMLFooter(_bvConfiguration, bvParameters);
    message = new StringBuilder();

    /*
     * Validator to check if all the bvParameters are valid.
     */
    bvParamValidator = new BVDefaultValidator();
    validationError = bvParamValidator.validate(_bvConfiguration, bvParameters);

    if (!StringUtils.isBlank(validationError)) {
      return;
    }

    reloadContent = bvParameters.equals(this.bvParameters);

    if (!reloadContent) {

      this.bvParameters = bvParameters;

      bvSeoSdkUrl = new BVSeoSdkURLBuilder(_bvConfiguration, bvParameters);

      bvUiContentService = new BVUIContentServiceProvider(_bvConfiguration);
      bvUiContentService.setBVParameters(this.bvParameters);
      bvUiContentService.setBVSeoSdkUrl(bvSeoSdkUrl);
      bvFooter.setBvSeoSdkUrl(bvSeoSdkUrl);
    }

  }

}
