package com.mscg.util;

/** Implementing this interface allows an object to be the target of
 *  the "foreach" statement.
 * @since 1.5
 */
public interface Iterable {

    /**
     * Returns an iterator over a set of elements.
     *
     * @return an Iterator.
     */
    Iterator iterator();
}
