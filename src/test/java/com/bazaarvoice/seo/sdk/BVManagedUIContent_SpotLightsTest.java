package com.bazaarvoice.seo.sdk;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.bazaarvoice.seo.sdk.config.BVClientConfig;
import com.bazaarvoice.seo.sdk.config.BVConfiguration;
import com.bazaarvoice.seo.sdk.config.BVSdkConfiguration;
import com.bazaarvoice.seo.sdk.helpers.SpotlightsTestcaseConstants;
import com.bazaarvoice.seo.sdk.model.BVParameters;
import com.bazaarvoice.seo.sdk.model.ContentType;
import com.bazaarvoice.seo.sdk.model.SubjectType;

/**
 * Test case for testing spotlights using BVManagedUIContent.getContent.
 *
 * @author anandan.narayanaswamy
 */
public class BVManagedUIContent_SpotLightsTest {

  /**
   * Tests getContent API with spotlights in BVParameters with
   * invalid product.
   *
   * Basically the invalid URL should still give a valid output but with a
   * resource not found message.
   */
  @Test
  public void testGetContent_Invalid_Product() {

    BVConfiguration bvConfiguration = new BVSdkConfiguration();
    bvConfiguration.addProperty(
      BVClientConfig.BV_ROOT_FOLDER,
      SpotlightsTestcaseConstants.getTestRootFolder()
    );
    bvConfiguration.addProperty(
      BVClientConfig.CLOUD_KEY,
      SpotlightsTestcaseConstants.getTestCloudKey()
    );
    bvConfiguration.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "false"
    );
    bvConfiguration.addProperty(
      BVClientConfig.SEO_SDK_ENABLED,
      "true"
    );
    bvConfiguration.addProperty(
      BVClientConfig.STAGING,
      "true"
    );

    BVParameters bvQueryParams = new BVParameters();
    bvQueryParams.setBaseURI("http://localhost/");
    bvQueryParams.setPageURI("http://localhost/?bvreveal=debug");
    bvQueryParams.setSubjectType(SubjectType.CATEGORY);
    bvQueryParams.setContentType(ContentType.SPOTLIGHTS);
    bvQueryParams.setSubjectId(SpotlightsTestcaseConstants
        .getTestProduct_1() + "_invalid_category");
    bvQueryParams.setUserAgent("googlebot");

    BVUIContent bvUiContent = new BVManagedUIContent(bvConfiguration);
    String uiContent = bvUiContent.getContent(bvQueryParams);

    Document jsoupDocument = Jsoup.parse(uiContent);
    Elements messageElement = jsoupDocument.select("li[data-bvseo=ms]");
    String expectedString = "bvseo-msg: The resource to the URL or file is currently unavailable.;";
    Assert.assertEquals(
      messageElement.text(),
      expectedString,
      "Should contain resource unavailable specific error."
    );

  }

  /**
   * Tests getContent API with spotlights in BVParameters with valid product.
   * Finally assert on  aggregateCategories, productRecommendations.
   */
  @Test
  public void testGetContent_Valid_Product() {
    BVConfiguration bvConfiguration = new BVSdkConfiguration();
    bvConfiguration.addProperty(
      BVClientConfig.BV_ROOT_FOLDER,
      SpotlightsTestcaseConstants.getTestRootFolder()
    );
    bvConfiguration.addProperty(
      BVClientConfig.CLOUD_KEY,
      SpotlightsTestcaseConstants.getTestCloudKey()
    );
    bvConfiguration.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "false"
    );
    bvConfiguration.addProperty(
      BVClientConfig.SEO_SDK_ENABLED,
      "true"
    );
    bvConfiguration.addProperty(
      BVClientConfig.STAGING,
      "true"
    );

    BVParameters bvQueryParams = new BVParameters();
    bvQueryParams.setBaseURI("http://localhost/");
    bvQueryParams.setPageURI("http://localhost/?bvreveal=debug");
    bvQueryParams.setSubjectType(SubjectType.CATEGORY);
    bvQueryParams.setContentType(ContentType.SPOTLIGHTS);
    bvQueryParams.setSubjectId(SpotlightsTestcaseConstants.getTestProduct_1());
    bvQueryParams.setUserAgent("googlebot");

    BVUIContent bvUiContent = new BVManagedUIContent(bvConfiguration);
    String uiContent = bvUiContent.getContent(bvQueryParams);

    Assert.assertFalse(
      "".equals(uiContent),
      "uiContent should not be empty"
    );

    Document jsoupDocument = Jsoup.parse(uiContent);
    assertCategoryAggregateSection(jsoupDocument);
    assertRecommendedProducts(jsoupDocument);
  }

  /**
   *  Tests getAggregateRating API with spotlights in BVParameters with valid
   *  product.
   *
   *  Finally assert on aggregateCategories.
   *
   *  Note: As of now the behavior for this API is not determined.
   *  By default we are allowing what ever it returns.
   */
  @Test
  public void testGetAggregate() {

    BVConfiguration bvConfiguration = new BVSdkConfiguration();
    bvConfiguration.addProperty(
      BVClientConfig.BV_ROOT_FOLDER,
      SpotlightsTestcaseConstants.getTestRootFolder()
    );
    bvConfiguration.addProperty(
      BVClientConfig.CLOUD_KEY,
      SpotlightsTestcaseConstants.getTestCloudKey()
    );
    bvConfiguration.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "false"
    );
    bvConfiguration.addProperty(
      BVClientConfig.SEO_SDK_ENABLED,
      "true"
    );
    bvConfiguration.addProperty(
      BVClientConfig.STAGING,
      "true"
    );

    BVParameters bvQueryParams = new BVParameters();
    bvQueryParams.setBaseURI("http://localhost/");
    bvQueryParams.setPageURI("http://localhost/?bvreveal=debug");
    bvQueryParams.setSubjectType(SubjectType.CATEGORY);
    bvQueryParams.setContentType(ContentType.SPOTLIGHTS);
    bvQueryParams.setSubjectId(SpotlightsTestcaseConstants.getTestProduct_1());
    bvQueryParams.setUserAgent("googlebot");

    BVUIContent bvUiContent = new BVManagedUIContent(bvConfiguration);
    String uiContent = bvUiContent.getAggregateRating(bvQueryParams);

    Document jsoupDocument = Jsoup.parse(uiContent);
    assertCategoryAggregateSection(jsoupDocument);
  }

  /**
   *  Tests getReviews API with spotlights in BVParameters with valid
   *  product.
   *
   *  Finally assert on category aggregateCategories.
   *
   *  Note: As of now the behavior for this API is not determined.
   *  By default we are allowing what ever it returns.
   */
  @Test
  public void testGetReviews() {
    BVConfiguration bvConfiguration = new BVSdkConfiguration();
    bvConfiguration.addProperty(
      BVClientConfig.BV_ROOT_FOLDER,
      SpotlightsTestcaseConstants.getTestRootFolder()
    );
    bvConfiguration.addProperty(
      BVClientConfig.CLOUD_KEY,
      SpotlightsTestcaseConstants.getTestCloudKey()
    );
    bvConfiguration.addProperty(
      BVClientConfig.LOAD_SEO_FILES_LOCALLY,
      "false"
    );
    bvConfiguration.addProperty(
      BVClientConfig.SEO_SDK_ENABLED,
      "true"
    );
    bvConfiguration.addProperty(
      BVClientConfig.STAGING,
      "true"
    );

    BVParameters bvQueryParams = new BVParameters();
    bvQueryParams.setBaseURI("http://localhost/");
    bvQueryParams.setPageURI("http://localhost/?bvreveal=debug");
    bvQueryParams.setSubjectType(SubjectType.CATEGORY);
    bvQueryParams.setContentType(ContentType.SPOTLIGHTS);
    bvQueryParams.setSubjectId(
      SpotlightsTestcaseConstants.getTestProduct_1()
    );
    bvQueryParams.setUserAgent("googlebot");

    BVUIContent bvUiContent = new BVManagedUIContent(bvConfiguration);
    String uiContent = bvUiContent.getReviews(bvQueryParams);

    Document jsoupDocument = Jsoup.parse(uiContent);
    assertCategoryAggregateSection(jsoupDocument);
    assertRecommendedProducts(jsoupDocument);
  }

  /**
   * Assertions for category aggregate section.
   *
   * Boolean assertions are made on aggregate categories and finally equality
   * tests are made keeping aggregateRating attribute.
   *
   * @param jsoupDocument
   */
  private void assertCategoryAggregateSection(final Document jsoupDocument) {
    Elements aggregateElementByCategory = jsoupDocument.select(
      "div.bv-category-aggregate"
    );
    Assert.assertFalse("".equals(
      aggregateElementByCategory.toString()),
      "There should be aggregateSection Elements."
    );
  }

  /**
   * Assertions for recommended product lists and product reviews. Assertions on
   * size and size match are made.
   *
   * @param jsoupDocument
   */
  private void assertRecommendedProducts(Document jsoupDocument) {

    Elements recommendedProducts = jsoupDocument.select(
      "li.bv-recommended-product"
    );

    Assert.assertTrue(
      recommendedProducts.size() > 0,
      "Recommended products should be greater than 0"
    );

    Elements productReviews = jsoupDocument.select(
      "ol.bv-reviews-list"
    );

    Assert.assertTrue(
      productReviews.size() > 0,
      "Product reviews should be greater than 0"
    );

    Assert.assertEquals(
      recommendedProducts.size(),
      productReviews.size(),
      "number of recommended products vs. product reviews should be same"
    );
  }
}
