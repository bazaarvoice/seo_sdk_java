package com.bazaarvoice.seo.sdk.model;

/**
 * SubjectType enum selections for Product, Category, Entry and Detail types.
 * @author Anandan Narayanaswamy
 *
 */
public enum SubjectType {
    PRODUCT ("p"),
    CATEGORY ("c"),
    ENTRY ("e"),
    DETAIL ("d");

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
    	
    	return null;
    }
}
