package com.bazaarvoice.seo.sdk.model;

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
