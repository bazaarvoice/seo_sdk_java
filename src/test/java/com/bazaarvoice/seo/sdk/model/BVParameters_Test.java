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

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * BVParameters equality test.
 * This is to ensure there is no bug induced by the libraries that are used
 * and ensuring integrity for equals and hashcode overrides.
 * 
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
		bvParamObj1.setPageNumber("1");
		
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
		bvParamObj2.setPageNumber("1");
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
