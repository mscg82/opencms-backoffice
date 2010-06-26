/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mscg.util.lang.comparator;

import com.mscg.util.lang.Comparator;

/**
 *
 * @author Giuseppe Miscione
 */
public class ReverseComparator implements Comparator {

    private Comparator comp;

    public ReverseComparator(Comparator comp) {
        this.comp = comp;
    }

    public int compare(Object o1, Object o2) {
        return this.comp.compare(o2, o1);
    }

}
