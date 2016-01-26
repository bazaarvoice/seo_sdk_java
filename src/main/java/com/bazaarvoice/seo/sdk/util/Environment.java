package com.bazaarvoice.seo.sdk.util;
import org.apache.commons.lang.SystemUtils;

/**
 * Helper class for accessing environment information.
 */
public class Environment {

  static {
    Package pkg = Package.class.getPackage();
    packageSpecificationVersion = pkg.getSpecificationVersion();
    jreVersion = SystemUtils.JAVA_VERSION;
  }

  private static String packageSpecificationVersion;
  private static String jreVersion;

  public static String getPackageSpecificationVersion() {
    return packageSpecificationVersion;
  }

  public static String getJreVersion(){
    return jreVersion;
  }

}

