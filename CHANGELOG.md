# Changelog
## 4.0.0
* Change minimum JRE version to 1.6
* Remove code utilizing JRE 1.7 features

## 3.2.0
* Pass-through of User Agent to gather statistics.
* Support gzip compression from CloudFront
* Improve memory usage

## 3.1.1
* Added direct dependency to commons-collections 3.2.2 to avoid older vulnerable library(3.2.1) from being included from velocity. 
* Force ordering on some footer metadata tags

## 3.1.0

* Added support for seller ratings.

## 2.3.0.0

* Added default value for BVParameters.contentType to ContentType.REVIEWS if
nothing is set in BVParameters.setContentType(ContentType).
* Added default value for BVParameters.subjectType to SubjectType.PRODUCT if
nothing is set in BVParameters.setSubjectType(SubjectType).
* Added support for bvstate parameter.
* Fix for 'sp_mt' metadata.

## 2.2.0.4

* Enable dummy use of the old `BOT_DETECTION` to prevent breakage on
upgrade of old versions.

## 2.2.0.3

* Fixed SEO SDK http connections that are not closed.

## 2.2.0.2

* SDK metadata is comma delimited, but there should be a space after
each comma. Additionally, the value for `data-bvseo="sdk"` should be
`"bvseo_sdk..."`.

## 2.2.0.1

* Added support to retrieve content for Spotlights product.

## 2.1.0.1

* `BVParameters.pageNumber` has been added. When used, this parameter will
override page number variables extracted from PageURI.
* The methods used to ensure optimal speed for end users and bots changed
significantly in this release. The updated functionality provides an aggressive
timeout for end users and a more generous timeout for bots. In either case, the
system will revert to a JavaScript-Only display if the operations exceed the
applicable execution timeout.
* `BVClient.BOT_DETECTION` has been removed. Bot detection functionality now
only applies to execution timeouts.
* `BVClient.EXECUTION_TIMEOUT_BOT` has been added with default value of 2000ms,
which is the execution timeout intended for search bots. The minimum
configurable value for this timeout is 100ms.
* Default value for `BVClient.EXECUTION_TIMEOUT` is set to 500ms from its
original 3000ms. If this value is set to 0ms, no connection attempts will
occur.
* Default value for `CONNECT_TIMEOUT` and `SOCKET_TIMEOUT` is increased to
2000ms (to match `BVClient.EXECUTION_TIMEOUT_BOT`).
* Error handling for `EXECUTION_TIMEOUT` and `EXECUTION_TIMEOUT_BOT`
implementation.
* Support for Java 1.5 has been fixed.

## 2.1.0.0-beta-1

* SSL support has been added and can be enabled in BVConfiguration using
`BVClientConfig.SSL_ENABLED` enum with values `true` or `false`.
* Custom charset support has been added. Custom charset can be in
BVConfiguration using `BVClientConfig.CHARSET` enum and supply supported
charset string.
* BV content validation has been added to validate content correctness using BV
string pattern.
* ExecutorService Thread creation has been moved to a static class reducing the
number of thread pool creations.
* Reporting enhancements – debug message in BVSEOSDK footer comment will now
have URL to the cloud SEO contents and botdetection flag.
* All http calls are now been made using java.net.HttpURLConnection and
javax.net.ssl.HttpsURLConnection instead of Apache fluent API.
* OSGi bundle libraries have been fixed and now use only required libraries.
* OSGi bundle version placeholder has been fixed to replace the correct
version.
* getReviews and getAggregateRatings validation rule has been fixed when
validating for review contents and aggregate rating contents.

## 2.0.1.5

* HTTP Proxy support has been added in BVConfiguration.
* StoryList and StoryGrid enhancement to read Stories from Bazaarvoice SEO
Content.
* Null pointer fix in BVParameter.pageURI when query string is not passed.

## 2.0.1.4

* `Bvconfig.properties` and `bvclient.properties` have been completely removed
as these are no longer necessary.
* MessageUtil - locales has been set explicitly to US locale for reporting use.
* Reporting enhancements on BVFooter HTML.

## 2.0.1.3

* Decoding in BVUtitily has been removed to fix encoding issues.
* Validation to check  for valid URI in `BVParameters.baseURI` and
`BVParameters.pageURI` has been implemented.

## 2.0.1.2

* Velocity log files were not created in linux environment due to permission
issue and this release includes the fix.
* Includes fix for Bazaarvoice footer which does not display properly in ATG
framework.
* Frequent user agent logging as info message has been turned to debug message
in the logs.

## 2.0.1.1

* BVSEOSDK footer - comment is now replaced by UL/LI tags that are populated
via velocity template.
* Code optimization for execution timeout functionality.
* BVDefaultValidation implementation to include validation of BVConfiguration
properties.
* Single thread scheduled service which caused too many threads been created
now this has been replaced by cached thread pool in
BVUIContentServiceProvider.

## 2.0.0 Beta – 2

* OSGi bundle release has been added
* Supports conversation 2013 implementation
* Execution timeout has been added so when the execution of the complete job is
not finished in a given time, it is canceled and a comment tag is displayed.
* Two more APIs are added in BVUIContent namely getAggregateRating and
getReviews.
* Crawler agent pattern are made to normal text string instead of complete
String pattern so user can set plain agent pattern in BVConfiguration. For
multiple agent crawler text, separate with ‘|’ delimiter.
* Connection timeout for cloud SEO implementation has been handled.
* bvreveal=debug will now display all the BVParameters attributes.

## 2.0.0 Beta

* Property driven using bvclient.properties
* Override bvclient.properties option
* Multiple configuration support to override bvclient.properties
* Parameters as object when accessing Bazaarvoice content API.
* Simplified usage of Bazaarvoice content API.
* Use Bazaarvoice contents API with default configuration or supply with user
configuration.
* Include integration scripts.
* Bazaarvoice support for Reviews, Question/Answers & Stories.
* Content type support for category and products.
* User friendly error messages for most of the programmatic and known
scenarios.
