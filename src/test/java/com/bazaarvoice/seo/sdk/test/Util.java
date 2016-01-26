package com.bazaarvoice.seo.sdk.test;

import org.testng.Assert;

import java.util.*;

/**
 * Utility methods for munging and comparing data in unit tests.
 */
public class Util {

  /**
   * Convert an array of T to an enumeration of T.
   * @param elements
   * @param <T>
   * @return
   */
  public static <T> Enumeration<T> getEnumeration(final T[] elements){
    return new ArrayEnumeration<T>(elements);
  }

  public static <T> Collection<T> makeCollection(Iterator<T> elements){
    Collection<T> collection = new ArrayList<T>();
    while(elements.hasNext()){
      collection.add(elements.next());
    }
    return collection;
  }

  private static class ArrayEnumeration<T> implements Enumeration<T> {
    int index = 0;
    T[] elements;

    public ArrayEnumeration(T[] elements){
      this.elements = elements;
    }
    /**
     * Tests if this enumeration contains more elements.
     *
     * @return <code>true</code> if and only if this enumeration object
     * contains at least one more element to provide;
     * <code>false</code> otherwise.
     */
    public boolean hasMoreElements() {
      return index < elements.length;
    }

    /**
     * Returns the next element of this enumeration if this enumeration
     * object has at least one more element to provide.
     *
     * @return the next element of this enumeration.
     * @throws java.util.NoSuchElementException if no more elements exist.
     */
    public T nextElement() {
      return elements[index++];
    }
  }
}
