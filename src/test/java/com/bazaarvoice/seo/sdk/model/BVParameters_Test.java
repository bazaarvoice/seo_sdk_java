package com.bazaarvoice.seo.sdk.model;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * BVParameters equality test.
 * This is to ensure there is no bug induced by the libararies that are used
 * and ensuring integrity overrides.
 * @author Anandan Narayanaswamy
 *
 */
public class BVParameters_Test {

	/**
	 * Since we have used EqualsBuilder and HashCodeBuilder from Apache commons,
	 * to ensure integrity and to be cautious this test case is written.
	 */
	@Test
	public void testEquality() {
		BVParameters bvParamObj1 = new BVParameters();
		bvParamObj1.setUserAgent("googlebot");
		bvParamObj1.setBaseURI("Example-Vector.jsp"); // this value is pagination links
		bvParamObj1.setPageURI("http://localhost/Example-Vector.jsp?someQuery=value1"); //this should be the current page, full URL
		bvParamObj1.setContentType(ContentType.REVIEWS);
		bvParamObj1.setSubjectType(SubjectType.PRODUCT);
		bvParamObj1.setSubjectId("1501");
		
		BVParameters bvParamObj2 = null;
		
		Assert.assertEquals(bvParamObj1.equals(bvParamObj2), false, "object1 and object2 should not be equal");
		
		bvParamObj2 = bvParamObj1;
		Assert.assertEquals(bvParamObj1.equals(bvParamObj2), true, "object1 and object2 should be equal");
		
		/*
		 * Other object instance test
		 */
		Assert.assertEquals(bvParamObj1.equals(new String("AbCD")), false, "object1 and someother should not be equal");
		
		bvParamObj2 = new BVParameters();
		bvParamObj2.setUserAgent("googlebot");
		bvParamObj2.setBaseURI("Example-Vector.jsp"); // this value is pagination links
		bvParamObj2.setPageURI("http://localhost/Example-Vector.jsp?someQuery=value1"); //this should be the current page, full URL
		bvParamObj2.setContentType(ContentType.REVIEWS);
		bvParamObj2.setSubjectType(SubjectType.PRODUCT);
		bvParamObj2.setSubjectId("1501");
		Assert.assertEquals(bvParamObj1.equals(bvParamObj2), true, "object1 and object2 should be equal");
		
		bvParamObj2 = new BVParameters();
		bvParamObj2.setUserAgent("msnbot");
		bvParamObj2.setBaseURI("Example-Vector.jsp"); // this value is pagination links
		bvParamObj2.setPageURI("http://localhost/Example-Vector.jsp?someQuery=value1"); //this should be the current page, full URL
		bvParamObj2.setContentType(ContentType.REVIEWS);
		bvParamObj2.setSubjectType(SubjectType.PRODUCT);
		bvParamObj2.setSubjectId("1501");
		Assert.assertEquals(bvParamObj1.equals(bvParamObj2), false, "object1 and object2 should not be equal");
	}
}
