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
