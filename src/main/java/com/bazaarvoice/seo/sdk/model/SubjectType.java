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
 * SubjectType enum selections for Product, Category, Entry and Detail types.
 *
 * @author Anandan Narayanaswamy
 */
public enum SubjectType {
  PRODUCT ("p"),
  CATEGORY ("c"),
  ENTRY ("e"),
  DETAIL ("d"),
  SELLER ("s");

  private String cs2013Text;

  private SubjectType(String cs2013Text) {
    this.cs2013Text = cs2013Text;
  }

  public String uriValue() {
    return this.toString().toLowerCase();
  }

  public String getCS2013Text() {
    return cs2013Text;
  }

  public static SubjectType subjectType(String subjectType) {
    if (subjectType.equalsIgnoreCase("p")) {
      return PRODUCT;
    }

    if (subjectType.equalsIgnoreCase("c")) {
      return CATEGORY;
    }

    if (subjectType.equalsIgnoreCase("e")) {
      return ENTRY;
    }

    if (subjectType.equalsIgnoreCase("d")) {
      return DETAIL;
    }

    if (subjectType.equalsIgnoreCase("s")) {
      return SELLER;
    }

    return null;
  }
}
