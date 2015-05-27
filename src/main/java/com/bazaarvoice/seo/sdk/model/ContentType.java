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

/**
 * ContentType enum : Allows to specify the type of contents to get from the
 * cloud.
 *
 * @author Anandan Narayanaswamy
 *
 */
public enum ContentType {

  REVIEWS("re"),
  REVIEWSPAGE("rp"),
  QUESTIONS("qu"),
  QUESTIONSPAGE("qp"),
  STORIES("sy"),
  STORIESPAGE("sp"),
  UNIVERSAL("un"),
  SPOTLIGHTS("sl");

  private final String ctxKeyword;

  ContentType(String ctxKeyword) {
    this.ctxKeyword = ctxKeyword;
  }

  public String uriValue() {
    return this.toString().toLowerCase();
  }

  public String getIntegrationScriptProperty() {
    return ctxKeyword + "Script";
  }

  public static ContentType ctFromKeyWord(String ctxKeyWord) {
    if (ctxKeyWord.equalsIgnoreCase("re")) {
      return REVIEWS;
    }

    if (ctxKeyWord.equalsIgnoreCase("sl")) {
      return SPOTLIGHTS;
    }

    if (ctxKeyWord.equalsIgnoreCase("rp")) {
      return REVIEWSPAGE;
    }

    if (ctxKeyWord.equalsIgnoreCase("qa")) {
      return QUESTIONS;
    }

    if (ctxKeyWord.equalsIgnoreCase("qp")) {
      return QUESTIONSPAGE;
    }

    if (ctxKeyWord.equalsIgnoreCase("sy")) {
      return STORIES;
    }

    if (ctxKeyWord.equalsIgnoreCase("un")) {
      return UNIVERSAL;
    }

    return null;
  }
}
