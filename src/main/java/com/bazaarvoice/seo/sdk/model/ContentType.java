package com.bazaarvoice.seo.sdk.model;

public enum ContentType {
    REVIEWS("re"),
    REVIEWSPAGE("rp"),
    QUESTIONS("qu"),
    QUESTIONSPAGE("qp"),
    STORIES("sy"),
    STORIESPAGE("sp"),
    UNIVERSAL("un");

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
