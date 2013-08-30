package com.bazaarvoice.seo.sdk.url;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;

/**
 * Test class for BVSeoSdkURLBuilder.
 * Ref. to each individual test cases for detail description / use case scenarios.
 * @author Anandan Narayanaswamy
 *
 */
public class BVSeoSdkURLBuilderTest {

	private BVSeoSdkUrl bvSeoSdkUrl;
	private BVConfiguration bvConfiguration;
	
	@BeforeMethod
	public void setup() {
		bvConfiguration = new BVSdkConfiguration();
		
		bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");
		bvConfiguration.addProperty(BVClientConfig.CLOUD_KEY,
				"godaddy-a4501eb5be8bf8efda68f3f4ff7b3cf4");
		
		bvConfiguration.addProperty(BVClientConfig.LOCAL_SEO_FILE_ROOT, "/filePath");
		bvConfiguration.addProperty(BVClientConfig.BV_ROOT_FOLDER, "6574-en_us");
	}
	
	/**
	 * PRR http url test cases.
	 * Test case with various possible BaseURI and PageURI
	 * that can be supplied inside bvParameters.
	 * These are valid Base and PageURI and the code implementation 
	 * should handle all scenarios and should not throw any error or behave improperly.
	 * Also note that base and pageUri can be null and we assume that user has entered
	 * empty string for the same.
	 */
	@Test
	public void testBase_PageUri_For_PRR_HTTP() {
		BVParameters bvParam = new BVParameters();
		bvParam.setBaseURI(null);
		bvParam.setPageURI(null);
		bvParam.setContentType(ContentType.REVIEWS);
		bvParam.setSubjectType(SubjectType.PRODUCT);
		bvParam.setSubjectId("ssl-certificates");

		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		String expectedBaseUri = "";
		String expectedQueryString = "";
		String expectedSeoContentUri = "http://seo.bazaarvoice.com/godaddy-a4501eb5be8bf8efda68f3f4ff7b3cf4/6574-en_us/reviews/product/1/ssl-certificates.htm";
		
		String actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		String actualQueryString = bvSeoSdkUrl.queryString();
		String actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, expectedSeoContentUri, "actual and expected seo content uri should be same");
		
		/*
		 * When base uri and page uri are empty.
		 */
		bvParam.setBaseURI("");
		bvParam.setPageURI("");

		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		expectedBaseUri = "";
		expectedQueryString = "";
		expectedSeoContentUri = "http://seo.bazaarvoice.com/godaddy-a4501eb5be8bf8efda68f3f4ff7b3cf4/6574-en_us/reviews/product/1/ssl-certificates.htm";
		
		actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		actualQueryString = bvSeoSdkUrl.queryString();
		actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, expectedSeoContentUri, "actual and expected seo content uri should be same");
		
		/*
		 * When base uri and page uri are complete urls.
		 */
		bvParam.setBaseURI("http://localhost:8080/Sample/Example-1.jsp");
		bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvrrp=abcd");

		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		expectedBaseUri = "http://localhost:8080/Sample/Example-1.jsp";
		expectedQueryString = "bvrrp=abcd";
		expectedSeoContentUri = "http://seo.bazaarvoice.com/godaddy-a4501eb5be8bf8efda68f3f4ff7b3cf4/6574-en_us/reviews/product/1/ssl-certificates.htm";
		
		actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		actualQueryString = bvSeoSdkUrl.queryString();
		actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, expectedSeoContentUri, "actual and expected seo content uri should be same");
		
		/*
		 * When base uri and page uri has bvrrp parameters.
		 */
		bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/3/ssl-certificates.htm");
		bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/3/ssl-certificates.htm");
		
		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		expectedBaseUri = "/sample_seo_sdk_web/scenario-2.jsp?null";
		expectedQueryString = "null&bvrrp=6574-en_us/reviews/product/3/ssl-certificates.htm";
		expectedSeoContentUri = "http://seo.bazaarvoice.com/godaddy-a4501eb5be8bf8efda68f3f4ff7b3cf4/6574-en_us/reviews/product/3/ssl-certificates.htm";
		
		actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		actualQueryString = bvSeoSdkUrl.queryString();
		actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, expectedSeoContentUri, "actual and expected seo content uri should be same");
		
		/*
		 * When base uri and page uri has bvrrp parameters.
		 */
		bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/2/ssl-certificates.htm");
		bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/2/ssl-certificates.htm");
		
		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		expectedBaseUri = "/sample_seo_sdk_web/scenario-2.jsp?null";
		expectedQueryString = "null&bvrrp=6574-en_us/reviews/product/2/ssl-certificates.htm";
		expectedSeoContentUri = "http://seo.bazaarvoice.com/godaddy-a4501eb5be8bf8efda68f3f4ff7b3cf4/6574-en_us/reviews/product/2/ssl-certificates.htm";
		
		actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		actualQueryString = bvSeoSdkUrl.queryString();
		actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, expectedSeoContentUri, "actual and expected seo content uri should be same");
		
		/*System.out.println("correctedBaseUri --> " + actualBaseUri);
		System.out.println("queryString --> " + actualQueryString);
		System.out.println("seoContentUri --> " + actualSeoContentUri);*/
	}
	
	/**
	 * PRR file url test cases.
	 */
	@Test
	public void testBase_PageUri_For_PRR_File() {
		bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "true");
		
		BVParameters bvParam = new BVParameters();
		bvParam.setBaseURI(null);
		bvParam.setPageURI(null);
		bvParam.setContentType(ContentType.REVIEWS);
		bvParam.setSubjectType(SubjectType.PRODUCT);
		bvParam.setSubjectId("ssl-certificates");

		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		String expectedBaseUri = "";
		String expectedQueryString = "";
		String expectedSeoContentUri = "/filePath/6574-en_us/reviews/product/1/ssl-certificates.htm";
		
		String actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		String actualQueryString = bvSeoSdkUrl.queryString();
		String actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, new File(expectedSeoContentUri).toURI().toString(), "actual and expected seo content uri should be same");
		
		/*
		 * When base uri and page uri are empty.
		 */
		bvParam.setBaseURI("");
		bvParam.setPageURI("");

		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		expectedBaseUri = "";
		expectedQueryString = "";
		expectedSeoContentUri = "/filePath/6574-en_us/reviews/product/1/ssl-certificates.htm";
		
		actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		actualQueryString = bvSeoSdkUrl.queryString();
		actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, new File(expectedSeoContentUri).toURI().toString(), "actual and expected seo content uri should be same");
		
		/*
		 * When base uri and page uri are complete urls.
		 */
		bvParam.setBaseURI("http://localhost:8080/Sample/Example-1.jsp");
		bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvrrp=abcd");

		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		expectedBaseUri = "http://localhost:8080/Sample/Example-1.jsp";
		expectedQueryString = "bvrrp=abcd";
		expectedSeoContentUri = "/filePath/6574-en_us/reviews/product/1/ssl-certificates.htm";
		
		actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		actualQueryString = bvSeoSdkUrl.queryString();
		actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, new File(expectedSeoContentUri).toURI().toString(), "actual and expected seo content uri should be same");
		
		/*
		 * When base uri and page uri has bvrrp parameters.
		 */
		bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/3/ssl-certificates.htm");
		bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/3/ssl-certificates.htm");
		
		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		expectedBaseUri = "/sample_seo_sdk_web/scenario-2.jsp?null";
		expectedQueryString = "null&bvrrp=6574-en_us/reviews/product/3/ssl-certificates.htm";
		expectedSeoContentUri = "/filePath/6574-en_us/reviews/product/3/ssl-certificates.htm";
		
		actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		actualQueryString = bvSeoSdkUrl.queryString();
		actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, new File(expectedSeoContentUri).toURI().toString(), "actual and expected seo content uri should be same");
		
		/*
		 * When base uri and page uri has bvrrp parameters.
		 */
		bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/2/ssl-certificates.htm");
		bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvrrp=6574-en_us/reviews/product/2/ssl-certificates.htm");
		
		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		expectedBaseUri = "/sample_seo_sdk_web/scenario-2.jsp?null";
		expectedQueryString = "null&bvrrp=6574-en_us/reviews/product/2/ssl-certificates.htm";
		expectedSeoContentUri = "/filePath/6574-en_us/reviews/product/2/ssl-certificates.htm";
		
		actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		actualQueryString = bvSeoSdkUrl.queryString();
		actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, new File(expectedSeoContentUri).toURI().toString(), "actual and expected seo content uri should be same");
		
		/*System.out.println("correctedBaseUri --> " + actualBaseUri);
		System.out.println("queryString --> " + actualQueryString);
		System.out.println("seoContentUri --> " + actualSeoContentUri);*/
	}
	
	/**
	 * C2013 http url test cases.
	 * If pageUri contains bvpage param then it is a valid C2013 implementation
	 * anything apart from that is PRR implementation.
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	public void testUri_For_C2013_HTTP() throws UnsupportedEncodingException {
		bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "false");
		
		BVParameters bvParam = new BVParameters();
		bvParam.setBaseURI("http://localhost:8080/Sample/Example-1.jsp");
		bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvpage=pg2/ctrp/stp");
		bvParam.setContentType(ContentType.REVIEWS);
		bvParam.setSubjectType(SubjectType.PRODUCT);
		bvParam.setSubjectId("ssl-certificates");

		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		String expectedBaseUri = "http://localhost:8080/Sample/Example-1.jsp";
		String expectedQueryString = "bvpage=pg2/ctrp/stp";
		String expectedSeoContentUri = "http://seo.bazaarvoice.com/godaddy-a4501eb5be8bf8efda68f3f4ff7b3cf4/6574-en_us/reviewspage/product/2/ssl-certificates.htm";
		
		String actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		String actualQueryString = bvSeoSdkUrl.queryString();
		String actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, expectedSeoContentUri, "actual and expected seo content uri should be same");
		
		/*
		 * When base uri and page uri has bvpage parameters.
		 * and the subjectid is part of bvpage. also base uri has different page number and subjectid.
		 * also the ct is reviews.
		 */
		bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvpage=pg2/ctrp/stp/iddogfood");
		bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvpage=pg3/ctre/stp/idcatfood");
		
		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		expectedBaseUri = "/sample_seo_sdk_web/scenario-2.jsp?null";
		expectedQueryString = "null&bvpage=pg3/ctre/stp/idcatfood";
		expectedSeoContentUri = "http://seo.bazaarvoice.com/godaddy-a4501eb5be8bf8efda68f3f4ff7b3cf4/6574-en_us/reviews/product/3/catfood.htm";
		
		actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		actualQueryString = bvSeoSdkUrl.queryString();
		actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, expectedSeoContentUri, "actual and expected seo content uri should be same");
		
		/*
		 * When base uri and page uri has bvpage parameters but not a fully qualified url.
		 * also the subjectId is part of bvParam and not in the bvpage.
		 */
		bvParam.setBaseURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvpage=pg3/ctre/stp/idcatfood");
		bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvpage=pg4/ctrp/stp");
		bvParam.setSubjectId("p5543");
		
		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		expectedBaseUri = "/sample_seo_sdk_web/scenario-2.jsp?null";
		expectedQueryString = "null&bvpage=pg4/ctrp/stp";
		expectedSeoContentUri = "http://seo.bazaarvoice.com/godaddy-a4501eb5be8bf8efda68f3f4ff7b3cf4/6574-en_us/reviewspage/product/4/p5543.htm";
		
		actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		actualQueryString = bvSeoSdkUrl.queryString();
		actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, expectedSeoContentUri, "actual and expected seo content uri should be same");
		
		/*
		 * reviews category roll-up (future product), page 2, subject ID = c8765
		 */
		bvParam.setBaseURI(null);
		bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvpage=pg2/ctre/stc");
		bvParam.setSubjectId("c8765");
		
		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		expectedBaseUri = "";
		expectedQueryString = "null&bvpage=pg2/ctre/stc";
		expectedSeoContentUri = "http://seo.bazaarvoice.com/godaddy-a4501eb5be8bf8efda68f3f4ff7b3cf4/6574-en_us/reviews/category/2/c8765.htm";
		
		actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		actualQueryString = bvSeoSdkUrl.queryString();
		actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, expectedSeoContentUri, "actual and expected seo content uri should be same");
		
		/*
		 * questions/answers detail page, question ID = q45677
		 */
		bvParam.setBaseURI(null);
		bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvpage=ctqa/std/id45677");
		bvParam.setSubjectId("c8765");
		
		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		expectedBaseUri = "";
		expectedQueryString = "null&bvpage=ctqa/std/id45677";
		expectedSeoContentUri = "http://seo.bazaarvoice.com/godaddy-a4501eb5be8bf8efda68f3f4ff7b3cf4/6574-en_us/questions/detail/1/45677.htm";
		
		actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		actualQueryString = bvSeoSdkUrl.queryString();
		actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, expectedSeoContentUri, "actual and expected seo content uri should be same");
		
		/*
		 * entry/landing page (future product), id = myshirtspage
		 */
		bvParam.setBaseURI(null);
		bvParam.setPageURI("/sample_seo_sdk_web/scenario-2.jsp?null&bvpage=ctun/ste/idmyshirtspage");
		bvParam.setSubjectId(null);
		
		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		expectedBaseUri = "";
		expectedQueryString = "null&bvpage=ctun/ste/idmyshirtspage";
		expectedSeoContentUri = "http://seo.bazaarvoice.com/godaddy-a4501eb5be8bf8efda68f3f4ff7b3cf4/6574-en_us/universal/entry/1/myshirtspage.htm";
		
		actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		actualQueryString = bvSeoSdkUrl.queryString();
		actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, expectedSeoContentUri, "actual and expected seo content uri should be same");
		
	}
	
	/**
	 * C2013 file url test cases.
	 * Test case to only check if the file url is pointing correctly.
	 */
	@Test
	public void testUri_For_C2013_File() {
		bvConfiguration.addProperty(BVClientConfig.LOAD_SEO_FILES_LOCALLY, "true");
		
		BVParameters bvParam = new BVParameters();
		bvParam.setBaseURI("http://localhost:8080/Sample/Example-1.jsp");
		bvParam.setPageURI("http://localhost:8080/Sample/Example-1.jsp?bvpage=pg2/ctrp/stp");
		bvParam.setContentType(ContentType.REVIEWS);
		bvParam.setSubjectType(SubjectType.PRODUCT);
		bvParam.setSubjectId("ssl-certificates");

		bvSeoSdkUrl = new BVSeoSdkURLBuilder(bvConfiguration, bvParam);
		
		String expectedBaseUri = "http://localhost:8080/Sample/Example-1.jsp";
		String expectedQueryString = "bvpage=pg2/ctrp/stp";
		String expectedSeoContentUri = new File("/filePath/6574-en_us/reviewspage/product/2/ssl-certificates.htm").toURI().toString();
		
		String actualBaseUri = bvSeoSdkUrl.correctedBaseUri();
		String actualQueryString = bvSeoSdkUrl.queryString();
		String actualSeoContentUri = bvSeoSdkUrl.seoContentUri().toString();
		
		assertEquals(actualBaseUri, expectedBaseUri, "actual and expected base uri should be same");
		assertEquals(actualQueryString, expectedQueryString, "actual and expected query string should be same");
		assertEquals(actualSeoContentUri, expectedSeoContentUri, "actual and expected seo content uri should be same");
		
		/*System.out.println("correctedBaseUri --> " + actualBaseUri);
		System.out.println("queryString --> " + actualQueryString);
		System.out.println("seoContentUri --> " + actualSeoContentUri);*/
	}
	
}
