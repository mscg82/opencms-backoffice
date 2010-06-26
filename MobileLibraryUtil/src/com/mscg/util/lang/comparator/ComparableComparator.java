package com.mscg.util.lang.comparator;

import com.mscg.util.lang.*;

public class ComparableComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        Comparable c1 = (Comparable) o1;
        Comparable c2 = (Comparable) o2;
        return c1.compareTo(c2);
    }
}
