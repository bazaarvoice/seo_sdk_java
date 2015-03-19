Bazaarvoice SEO SDK for Java
============================
This repository contains documentation and code base for SEO SDK project. To know more about the project and the code, 
please refer to the documentation folder for information.

Source code for the project is located in the src folder. However we recommend to checkout the root folder since it
is a fully maven enabled project with pom.xml residing at the root folder.

## Binaries:

* Latest releases of the libraries are available at the following URL: http://knowledge.bazaarvoice.com/wp-content/conversations/en_US/KB/#SEO/Developer/SEO_Developer_Overview.htm

 
## Release Notes:
### 2.2.0.1
SEO-599: Update JAVA SDK to Support Spotlights Content type

Description: Add support to the SDK to retrieve content for Spotlights product

### 2.2.0.2
SEO-633: Java SDK metadata is incorrect

Description: SDK metadata is comma delimited, but there should be a space after each comma. Additionally, the value for *data-bvseo="sdk"* should be "bvseo_sdk..."

### 2.2.0.3
CCS-3133: SEO SDK http connections are not closed

Description: SEO SDK http connections are not closed. InputStream should be closed in a finally block.