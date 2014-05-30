/*
 * ===========================================================================
 * Copyright 2014 Bazaarvoice, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
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
 * Content sub types enum support added for the purpose of stories and storiesgrid.
 * @author Anandan Narayanaswamy
 *
 */
public enum ContentSubType {
	
    NONE (""),
    STORIES_LIST("stories"),
    STORIES_GRID("storiesgrid");

    private final String contentKeyword;

    ContentSubType(String contentKeyword) {
        this.contentKeyword = contentKeyword;
    }

    public String getContentKeyword() {
        return this.contentKeyword;
    }
    
}
