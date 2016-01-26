package com.bazaarvoice.seo.sdk.test;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Custom Asserts for Unit Tests
 */
public class Assert {

  /**
   * Assert that two sets of objects contain the same objects without regard to order.
   */
  public static <T> void assertEquals(Iterator<T> actual, Enumeration<T> expected){
    SortedSet<T> actualSet = new TreeSet<T>();
    SortedSet<T> expectedSet = new TreeSet<T>();

    while(expected.hasMoreElements()){
      expectedSet.add(expected.nextElement());
    }

    while(actual.hasNext()){
      actualSet.add(actual.next());
    }

    org.testng.Assert.assertEquals(actualSet, expectedSet);
  }

  public static <T> void assertEmpty(Iterator<T> actual){
    org.testng.Assert.assertFalse(actual.hasNext(), "");
  }

  public static <T> void assertEmpty(Iterable<T> actual){
    assertEmpty(actual.iterator());
  }

}
