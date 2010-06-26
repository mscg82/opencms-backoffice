/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mscg.util.lang;

import com.mscg.util.lang.comparator.ComparableComparator;
import com.mscg.util.lang.comparator.IntegerComparator;

/**
 *
 * @author Giuseppe Miscione
 */
public class Arrays {

    /**
     * Mergesort algorithm.
     * @param a an array of integer items.
     */
    public static void sort(int[] a) {
        sort(a, new IntegerComparator());
    }

    /**
     * Mergesort algorithm.
     * @param a an array of integer items.
     */
    public static void sort(int[] a, Comparator comparator) {
        Integer []copy = new Integer[a.length];
        for(int i = 0, l = a.length; i < l; i++) {
            copy[i] = new Integer(a[i]);
        }
        sort(copy, comparator);
        for(int i = 0, l = a.length; i < l; i++) {
            a[i] = copy[i].intValue();
        }
    }

    /**
     * Mergesort algorithm.
     * @param a an array of integer items.
     */
    public static void sort(Integer[] a) {
        sort(a, new IntegerComparator());
    }

    /**
     * Mergesort algorithm.
     * @param a an array of integer items.
     * @param comparator a comparator that will be used to compare items.
     */
    public static void sort(Integer[] a, Comparator comparator) {
        Integer[] tmpArray = new Integer[a.length];
        mergeSort(a, tmpArray, 0, a.length - 1, comparator);
    }

    /**
     * Mergesort algorithm.
     * @param a an array of Comparable items.
     */
    public static void sort(Comparable[] a) {
        Comparable[] tmpArray = new Comparable[a.length];
        mergeSort(a, tmpArray, 0, a.length - 1, new ComparableComparator());
    }

    /**
     * Mergesort algorithm.
     * @param a an array of Objects items.
     * @param comparator a comparator that will be used to compare items.
     */
    public static void sort(Object[] a, Comparator comparator) {
        Comparable[] tmpArray = new Comparable[a.length];
        mergeSort(a, tmpArray, 0, a.length - 1, comparator);
    }

    /**
     * Internal method that makes recursive calls.
     * @param a an array of Comparable items.
     * @param tmpArray an array to place the merged result.
     * @param left the left-most index of the subarray.
     * @param right the right-most index of the subarray.
     */
    private static void mergeSort(Object[] a, Object[] tmpArray,
            int left, int right, Comparator comparator) {
        if (left < right) {
            int center = (left + right) / 2;
            mergeSort(a, tmpArray, left, center, comparator);
            mergeSort(a, tmpArray, center + 1, right, comparator);
            merge(a, tmpArray, left, center + 1, right, comparator);
        }
    }

    /**
     * Internal method that merges two sorted halves of a subarray.
     * @param a an array of Comparable items.
     * @param tmpArray an array to place the merged result.
     * @param leftPos the left-most index of the subarray.
     * @param rightPos the index of the start of the second half.
     * @param rightEnd the right-most index of the subarray.
     */
    private static void merge(Object[] a, Object[] tmpArray,
            int leftPos, int rightPos, int rightEnd, Comparator comparator) {
        int leftEnd = rightPos - 1;
        int tmpPos = leftPos;
        int numElements = rightEnd - leftPos + 1;

        // Main loop
        while (leftPos <= leftEnd && rightPos <= rightEnd) {
            if (comparator.compare(a[leftPos], a[rightPos]) <= 0) {
                tmpArray[tmpPos++] = a[leftPos++];
            } else {
                tmpArray[tmpPos++] = a[rightPos++];
            }
        }

        while (leftPos <= leftEnd) { // Copy rest of first half
            tmpArray[tmpPos++] = a[leftPos++];
        }

        while (rightPos <= rightEnd) { // Copy rest of right half
            tmpArray[tmpPos++] = a[rightPos++];
        }

        // Copy tmpArray back
        for (int i = 0; i < numElements; i++, rightEnd--) {
            a[rightEnd] = tmpArray[rightEnd];
        }
    }
}
