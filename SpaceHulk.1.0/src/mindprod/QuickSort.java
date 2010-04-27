// QuickSort.java

//package com.mindprod.quicksort;
package mindprod;

//import java.util.Comparator;
import util.Comparator;

/*
Source code for Hoare's QuickSort

copyright (c) 1996-2008

  Roedy Green
  Canadian Mind Products
  #101 - 2536 Wark Street
  Victoria, BC Canada V8T 4G8
  tel: (250) 361-9093
  mailto:roedyg@mindprod.com
  http://mindprod.com

May be freely distributed for any purpose but military.

Version 1.0
        1.1 1998-11-10 - add name and address.
        1.2 1998-12-28 - JDK 1.2 style Comparator
        1.3 2002-02-19 - java.util.Comparator by default.
        1.4 2002-03-30 - tidy code.
        1.5 2003-05-30 - add dummy private constructor
        1.6 2008-01-01 - add generics to comparator

Eric van Bezooijen <eric@logrus.berkeley.edu> was the
original author of this version of QuickSort.  I modified
the version he posted to comp.lang.java to use a callback
delegate object. I also made a few optimisations.

I have also posted source for HeapSort and RadixSort both
of which run faster than QuickSort.

*/

// author:Eric van Bezooijen <eric@logrus.berkeley.edu>
// modifed by Roedy Green <roedyg@mindprod.com>

// to a use a Comparator callback delegate
/**
 * QuickSort for objects
 */
public class QuickSort
    {
// ------------------------------ FIELDS ------------------------------

    private static final boolean DEBUGGING = false;

    private static final String EmbeddedCopyright =
            "copyright (c) 1996-2008 Roedy Green, Canadian Mind Products, http://mindprod.com";

    // callback object we are passed that has
    // a Comparator( <? super T> a, <? super T> b ) method.
    private Comparator comparator;

    // pointer to the array of user's objects we are sorting
    private Object[] userArray;
// -------------------------- PUBLIC STATIC METHODS --------------------------

    /**
     * sort the user's array
     *
     * @param userArray  Array of Objects to be sorted.
     * @param comparator Comparator delegate that can compare two Objects and tell which should come first.
     */
    public static void sort( Object[] userArray, Comparator comparator )
        {
        QuickSort h = new QuickSort();
        h.comparator = comparator;
        h.userArray = userArray;
        if ( h.isAlreadySorted() )
            {
            return;
            }
        h.quicksort( 0, userArray.length - 1 );
        if ( h.isAlreadySorted() )
            {
            return;
            }
        if ( DEBUGGING )
            {
            // debug ensure sort is working
            if ( !h.isAlreadySorted() )
                {
                System.out.println( "Sort failed" );
                }
            }
        return;
        }// end sort

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * dummy constructor to prevent its use. Use static method sort.
     */
    private QuickSort()
        {
        }

// -------------------------- OTHER METHODS --------------------------

    // check if user's array is already sorted
    private boolean isAlreadySorted()
        {
        for ( int i = 1; i < userArray.length; i++ )
            {
            if ( comparator.compare( userArray[ i ], userArray[ i - 1 ] ) < 0 )
                {
                return false;
                }
            }

        return true;
        }// end isAlreadySorted

    // Partition by splitting this chunk to sort in two and
    // get all big elements on one side of the pivot and all
    // the small elements on the other.
    private int partition( int lo, int hi )
        {
        Object pivot = userArray[ lo ];
        while ( true )
            {
            while ( comparator.compare( userArray[ hi ], pivot ) >= 0 &&
                    lo < hi )
                {
                hi--;
                }
            while ( comparator.compare( userArray[ lo ], pivot ) < 0 &&
                    lo < hi )
                {
                lo++;
                }
            if ( lo < hi )
                {
                // exchange objects on either side of the pivot
                Object temp = userArray[ lo ];
                userArray[ lo ] = userArray[ hi ];
                userArray[ hi ] = temp;
                }
            else
                {
                return hi;
                }
            }// end while
        }// end partition

    // recursive quicksort that breaks array up into sub
    // arrays and sorts each of them.
    private void quicksort( int p, int r )
        {
        if ( p < r )
            {
            int q = partition( p, r );
            if ( q == r )
                {
                q--;
                }
            quicksort( p, q );
            quicksort( q + 1, r );
            }// end if
        }// end quicksort
    }// end class QuickSort
