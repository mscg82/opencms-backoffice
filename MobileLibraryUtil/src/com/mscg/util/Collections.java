package com.mscg.util;

import java.util.Random;

import com.mscg.util.lang.Comparator;

/**
 * 
 * @author Giuseppe Miscione
 */
public class Collections {

	// Suppresses default constructor, ensuring non-instantiability.
	private Collections() {
	}

	// Algorithms

	/*
	 * Tuning parameters for algorithms - Many of the List algorithms have two
	 * implementations, one of which is appropriate for RandomAccess lists, the
	 * other for "sequential." Often, the random access variant yields better
	 * performance on small sequential access lists. The tuning parameters below
	 * determine the cutoff point for what constitutes a "small" sequential
	 * access list for each algorithm. The values below were empirically
	 * determined to work well for LinkedList. Hopefully they should be
	 * reasonable for other sequential access List implementations. Those doing
	 * performance work on this code would do well to validate the values of
	 * these parameters from time to time. (The first word of each tuning
	 * parameter name is the algorithm to which it applies.)
	 */
	private static final int BINARYSEARCH_THRESHOLD = 5000;
	private static final int REVERSE_THRESHOLD = 18;
	private static final int SHUFFLE_THRESHOLD = 5;
	private static final int FILL_THRESHOLD = 25;
	private static final int ROTATE_THRESHOLD = 100;
	private static final int COPY_THRESHOLD = 10;
	private static final int REPLACEALL_THRESHOLD = 11;
	private static final int INDEXOFSUBLIST_THRESHOLD = 35;

	/**
	 * Sorts the specified list into ascending order, according to the
	 * <i>natural ordering</i> of its elements. All elements in the list must
	 * implement the <tt>Comparable</tt> interface. Furthermore, all elements in
	 * the list must be <i>mutually comparable</i> (that is,
	 * <tt>e1.compareTo(e2)</tt> must not throw a <tt>ClassCastException</tt>
	 * for any elements <tt>e1</tt> and <tt>e2</tt> in the list).
	 * <p>
	 * 
	 * This sort is guaranteed to be <i>stable</i>: equal elements will not be
	 * reordered as a result of the sort.
	 * <p>
	 * 
	 * The specified list must be modifiable, but need not be resizable.
	 * <p>
	 * 
	 * The sorting algorithm is a modified mergesort (in which the merge is
	 * omitted if the highest element in the low sublist is less than the lowest
	 * element in the high sublist). This algorithm offers guaranteed n log(n)
	 * performance.
	 * 
	 * This implementation dumps the specified list into an array, sorts the
	 * array, and iterates over the list resetting each element from the
	 * corresponding position in the array. This avoids the n<sup>2</sup> log(n)
	 * performance that would result from attempting to sort a linked list in
	 * place.
	 * 
	 * @param list
	 *            the list to be sorted.
	 * @throws ClassCastException
	 *             if the list contains elements that are not <i>mutually
	 *             comparable</i> (for example, strings and integers).
	 * @throws UnsupportedOperationException
	 *             if the specified list's list-iterator does not support the
	 *             <tt>set</tt> operation.
	 * @see Comparable
	 */
	public static void sort(List list) {
		Object[] a = list.toArray();
		Arrays.sort(a);
		ListIterator i = list.listIterator();
		for (int j = 0; j < a.length; j++) {
			i.next();
			i.set((Comparable) a[j]);
		}
	}

	/**
	 * Sorts the specified list according to the order induced by the specified
	 * comparator. All elements in the list must be <i>mutually comparable</i>
	 * using the specified comparator (that is, <tt>c.compare(e1, e2)</tt> must
	 * not throw a <tt>ClassCastException</tt> for any elements <tt>e1</tt> and
	 * <tt>e2</tt> in the list).
	 * <p>
	 * 
	 * This sort is guaranteed to be <i>stable</i>: equal elements will not be
	 * reordered as a result of the sort.
	 * <p>
	 * 
	 * The sorting algorithm is a modified mergesort (in which the merge is
	 * omitted if the highest element in the low sublist is less than the lowest
	 * element in the high sublist). This algorithm offers guaranteed n log(n)
	 * performance.
	 * 
	 * The specified list must be modifiable, but need not be resizable. This
	 * implementation dumps the specified list into an array, sorts the array,
	 * and iterates over the list resetting each element from the corresponding
	 * position in the array. This avoids the n<sup>2</sup> log(n) performance
	 * that would result from attempting to sort a linked list in place.
	 * 
	 * @param list
	 *            the list to be sorted.
	 * @param c
	 *            the comparator to determine the order of the list. A
	 *            <tt>null</tt> value indicates that the elements' <i>natural
	 *            ordering</i> should be used.
	 * @throws ClassCastException
	 *             if the list contains elements that are not <i>mutually
	 *             comparable</i> using the specified comparator.
	 * @throws UnsupportedOperationException
	 *             if the specified list's list-iterator does not support the
	 *             <tt>set</tt> operation.
	 * @see Comparator
	 */
	public static void sort(List list, Comparator c) {
		Object[] a = list.toArray();
		Arrays.sort(a, (Comparator) c);
		ListIterator i = list.listIterator();
		for (int j = 0; j < a.length; j++) {
			i.next();
			i.set(a[j]);
		}
	}

	/**
	 * Searches the specified list for the specified object using the binary
	 * search algorithm. The list must be sorted into ascending order according
	 * to the {@linkplain Comparable natural ordering} of its elements (as by
	 * the {@link #sort(List)} method) prior to making this call. If it is not
	 * sorted, the results are undefined. If the list contains multiple elements
	 * equal to the specified object, there is no guarantee which one will be
	 * found.
	 * 
	 * <p>
	 * This method runs in log(n) time for a "random access" list (which
	 * provides near-constant-time positional access). If the specified list
	 * does not implement the {@link RandomAccess} interface and is large, this
	 * method will do an iterator-based binary search that performs O(n) link
	 * traversals and O(log n) element comparisons.
	 * 
	 * @param list
	 *            the list to be searched.
	 * @param key
	 *            the key to be searched for.
	 * @return the index of the search key, if it is contained in the list;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The
	 *         <i>insertion point</i> is defined as the point at which the key
	 *         would be inserted into the list: the index of the first element
	 *         greater than the key, or <tt>list.size()</tt> if all elements in
	 *         the list are less than the specified key. Note that this
	 *         guarantees that the return value will be &gt;= 0 if and only if
	 *         the key is found.
	 * @throws ClassCastException
	 *             if the list contains elements that are not <i>mutually
	 *             comparable</i> (for example, strings and integers), or the
	 *             search key is not mutually comparable with the elements of
	 *             the list.
	 */
	public static int binarySearch(List list, Object key) {
		if (list instanceof RandomAccess
				|| list.size() < BINARYSEARCH_THRESHOLD)
			return Collections.indexedBinarySearch(list, key);
		else
			return Collections.iteratorBinarySearch(list, key);
	}

	private static int indexedBinarySearch(List list, Object key) {
		int low = 0;
		int high = list.size() - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			Comparable midVal = (Comparable) list.get(mid);
			int cmp = midVal.compareTo(key);

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found
	}

	private static int iteratorBinarySearch(List list, Object key) {
		int low = 0;
		int high = list.size() - 1;
		ListIterator i = list.listIterator();

		while (low <= high) {
			int mid = (low + high) >>> 1;
			Comparable midVal = get(i, mid);
			int cmp = midVal.compareTo(key);

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found
	}

	/**
	 * Gets the ith element from the given list by repositioning the specified
	 * list listIterator.
	 */
	private static Comparable get(ListIterator i, int index) {
		Comparable obj = null;
		int pos = i.nextIndex();
		if (pos <= index) {
			do {
				obj = (Comparable) i.next();
			} while (pos++ < index);
		} else {
			do {
				obj = (Comparable) i.previous();
			} while (--pos > index);
		}
		return obj;
	}

	/**
	 * Reverses the order of the elements in the specified list.
	 * <p>
	 * 
	 * This method runs in linear time.
	 * 
	 * @param list
	 *            the list whose elements are to be reversed.
	 * @throws UnsupportedOperationException
	 *             if the specified list or its list-iterator does not support
	 *             the <tt>set</tt> operation.
	 */
	public static void reverse(List list) {
		int size = list.size();
		if (size < REVERSE_THRESHOLD || list instanceof RandomAccess) {
			for (int i = 0, mid = size >> 1, j = size - 1; i < mid; i++, j--)
				swap(list, i, j);
		} else {
			ListIterator fwd = list.listIterator();
			ListIterator rev = list.listIterator(size);
			for (int i = 0, mid = list.size() >> 1; i < mid; i++) {
				Object tmp = fwd.next();
				fwd.set(rev.previous());
				rev.set(tmp);
			}
		}
	}

	/**
	 * Randomly permutes the specified list using a default source of
	 * randomness. All permutations occur with approximately equal likelihood.
	 * <p>
	 * 
	 * The hedge "approximately" is used in the foregoing description because
	 * default source of randomness is only approximately an unbiased source of
	 * independently chosen bits. If it were a perfect source of randomly chosen
	 * bits, then the algorithm would choose permutations with perfect
	 * uniformity.
	 * <p>
	 * 
	 * This implementation traverses the list backwards, from the last element
	 * up to the second, repeatedly swapping a randomly selected element into
	 * the "current position". Elements are randomly selected from the portion
	 * of the list that runs from the first element to the current position,
	 * inclusive.
	 * <p>
	 * 
	 * This method runs in linear time. If the specified list does not implement
	 * the {@link RandomAccess} interface and is large, this implementation
	 * dumps the specified list into an array before shuffling it, and dumps the
	 * shuffled array back into the list. This avoids the quadratic behavior
	 * that would result from shuffling a "sequential access" list in place.
	 * 
	 * @param list
	 *            the list to be shuffled.
	 * @throws UnsupportedOperationException
	 *             if the specified list or its list-iterator does not support
	 *             the <tt>set</tt> operation.
	 */
	public static void shuffle(List list) {
		if (r == null) {
			r = new Random();
		}
		shuffle(list, r);
	}

	private static Random r;

	/**
	 * Randomly permute the specified list using the specified source of
	 * randomness. All permutations occur with equal likelihood assuming that
	 * the source of randomness is fair.
	 * <p>
	 * 
	 * This implementation traverses the list backwards, from the last element
	 * up to the second, repeatedly swapping a randomly selected element into
	 * the "current position". Elements are randomly selected from the portion
	 * of the list that runs from the first element to the current position,
	 * inclusive.
	 * <p>
	 * 
	 * This method runs in linear time. If the specified list does not implement
	 * the {@link RandomAccess} interface and is large, this implementation
	 * dumps the specified list into an array before shuffling it, and dumps the
	 * shuffled array back into the list. This avoids the quadratic behavior
	 * that would result from shuffling a "sequential access" list in place.
	 * 
	 * @param list
	 *            the list to be shuffled.
	 * @param rnd
	 *            the source of randomness to use to shuffle the list.
	 * @throws UnsupportedOperationException
	 *             if the specified list or its list-iterator does not support
	 *             the <tt>set</tt> operation.
	 */
	public static void shuffle(List list, Random rnd) {
		int size = list.size();
		if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
			for (int i = size; i > 1; i--)
				swap(list, i - 1, rnd.nextInt(i));
		} else {
			Object arr[] = list.toArray();

			// Shuffle array
			for (int i = size; i > 1; i--)
				swap(arr, i - 1, rnd.nextInt(i));

			// Dump array back into list
			ListIterator it = list.listIterator();
			for (int i = 0; i < arr.length; i++) {
				it.next();
				it.set(arr[i]);
			}
		}
	}

	/**
	 * Swaps the elements at the specified positions in the specified list. (If
	 * the specified positions are equal, invoking this method leaves the list
	 * unchanged.)
	 * 
	 * @param list
	 *            The list in which to swap elements.
	 * @param i
	 *            the index of one element to be swapped.
	 * @param j
	 *            the index of the other element to be swapped.
	 * @throws IndexOutOfBoundsException
	 *             if either <tt>i</tt> or <tt>j</tt> is out of range (i &lt; 0
	 *             || i &gt;= list.size() || j &lt; 0 || j &gt;= list.size()).
	 * @since 1.4
	 */
	public static void swap(List list, int i, int j) {
		final List l = list;
		l.set(i, l.set(j, l.get(i)));
	}

	/**
	 * Swaps the two specified elements in the specified array.
	 */
	private static void swap(Object[] arr, int i, int j) {
		Object tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	/**
	 * Replaces all of the elements of the specified list with the specified
	 * element.
	 * <p>
	 * 
	 * This method runs in linear time.
	 * 
	 * @param list
	 *            the list to be filled with the specified element.
	 * @param obj
	 *            The element with which to fill the specified list.
	 * @throws UnsupportedOperationException
	 *             if the specified list or its list-iterator does not support
	 *             the <tt>set</tt> operation.
	 */
	public static void fill(List list, Object obj) {
		int size = list.size();

		if (size < FILL_THRESHOLD || list instanceof RandomAccess) {
			for (int i = 0; i < size; i++)
				list.set(i, obj);
		} else {
			ListIterator itr = list.listIterator();
			for (int i = 0; i < size; i++) {
				itr.next();
				itr.set(obj);
			}
		}
	}

	/**
	 * Copies all of the elements from one list into another. After the
	 * operation, the index of each copied element in the destination list will
	 * be identical to its index in the source list. The destination list must
	 * be at least as long as the source list. If it is longer, the remaining
	 * elements in the destination list are unaffected.
	 * <p>
	 * 
	 * This method runs in linear time.
	 * 
	 * @param dest
	 *            The destination list.
	 * @param src
	 *            The source list.
	 * @throws IndexOutOfBoundsException
	 *             if the destination list is too small to contain the entire
	 *             source List.
	 * @throws UnsupportedOperationException
	 *             if the destination list's list-iterator does not support the
	 *             <tt>set</tt> operation.
	 */
	public static void copy(List dest, List src) {
		int srcSize = src.size();
		if (srcSize > dest.size())
			throw new IndexOutOfBoundsException("Source does not fit in dest");

		if (srcSize < COPY_THRESHOLD
				|| (src instanceof RandomAccess && dest instanceof RandomAccess)) {
			for (int i = 0; i < srcSize; i++)
				dest.set(i, src.get(i));
		} else {
			ListIterator di = dest.listIterator();
			ListIterator si = src.listIterator();
			for (int i = 0; i < srcSize; i++) {
				di.next();
				di.set(si.next());
			}
		}
	}

	/**
	 * Returns the minimum element of the given collection, according to the
	 * <i>natural ordering</i> of its elements. All elements in the collection
	 * must implement the <tt>Comparable</tt> interface. Furthermore, all
	 * elements in the collection must be <i>mutually comparable</i> (that is,
	 * <tt>e1.compareTo(e2)</tt> must not throw a <tt>ClassCastException</tt>
	 * for any elements <tt>e1</tt> and <tt>e2</tt> in the collection).
	 * <p>
	 * 
	 * This method iterates over the entire collection, hence it requires time
	 * proportional to the size of the collection.
	 * 
	 * @param coll
	 *            the collection whose minimum element is to be determined.
	 * @return the minimum element of the given collection, according to the
	 *         <i>natural ordering</i> of its elements.
	 * @throws ClassCastException
	 *             if the collection contains elements that are not <i>mutually
	 *             comparable</i> (for example, strings and integers).
	 * @throws NoSuchElementException
	 *             if the collection is empty.
	 * @see Comparable
	 */
	public static Object min(Collection coll) {
		Iterator i = coll.iterator();
		Comparable candidate = (Comparable) i.next();

		while (i.hasNext()) {
			Comparable next = (Comparable) i.next();
			if (next.compareTo(candidate) < 0)
				candidate = next;
		}
		return candidate;
	}

	/**
	 * Returns the minimum element of the given collection, according to the
	 * order induced by the specified comparator. All elements in the collection
	 * must be <i>mutually comparable</i> by the specified comparator (that is,
	 * <tt>comp.compare(e1, e2)</tt> must not throw a
	 * <tt>ClassCastException</tt> for any elements <tt>e1</tt> and <tt>e2</tt>
	 * in the collection).
	 * <p>
	 * 
	 * This method iterates over the entire collection, hence it requires time
	 * proportional to the size of the collection.
	 * 
	 * @param coll
	 *            the collection whose minimum element is to be determined.
	 * @param comp
	 *            the comparator with which to determine the minimum element. A
	 *            <tt>null</tt> value indicates that the elements' <i>natural
	 *            ordering</i> should be used.
	 * @return the minimum element of the given collection, according to the
	 *         specified comparator.
	 * @throws ClassCastException
	 *             if the collection contains elements that are not <i>mutually
	 *             comparable</i> using the specified comparator.
	 * @throws NoSuchElementException
	 *             if the collection is empty.
	 * @see Comparable
	 */
	public static Object min(Collection coll, Comparator comp) {
		if (comp == null)
			return min(coll);

		Iterator i = coll.iterator();
		Object candidate = i.next();

		while (i.hasNext()) {
			Object next = i.next();
			if (comp.compare(next, candidate) < 0)
				candidate = next;
		}
		return candidate;
	}

	/**
	 * Returns the maximum element of the given collection, according to the
	 * <i>natural ordering</i> of its elements. All elements in the collection
	 * must implement the <tt>Comparable</tt> interface. Furthermore, all
	 * elements in the collection must be <i>mutually comparable</i> (that is,
	 * <tt>e1.compareTo(e2)</tt> must not throw a <tt>ClassCastException</tt>
	 * for any elements <tt>e1</tt> and <tt>e2</tt> in the collection).
	 * <p>
	 * 
	 * This method iterates over the entire collection, hence it requires time
	 * proportional to the size of the collection.
	 * 
	 * @param coll
	 *            the collection whose maximum element is to be determined.
	 * @return the maximum element of the given collection, according to the
	 *         <i>natural ordering</i> of its elements.
	 * @throws ClassCastException
	 *             if the collection contains elements that are not <i>mutually
	 *             comparable</i> (for example, strings and integers).
	 * @throws NoSuchElementException
	 *             if the collection is empty.
	 * @see Comparable
	 */
	public static Object max(Collection coll) {
		Iterator i = coll.iterator();
		Comparable candidate = (Comparable) i.next();

		while (i.hasNext()) {
			Comparable next = (Comparable) i.next();
			if (next.compareTo(candidate) > 0)
				candidate = next;
		}
		return candidate;
	}

	/**
	 * Returns the maximum element of the given collection, according to the
	 * order induced by the specified comparator. All elements in the collection
	 * must be <i>mutually comparable</i> by the specified comparator (that is,
	 * <tt>comp.compare(e1, e2)</tt> must not throw a
	 * <tt>ClassCastException</tt> for any elements <tt>e1</tt> and <tt>e2</tt>
	 * in the collection).
	 * <p>
	 * 
	 * This method iterates over the entire collection, hence it requires time
	 * proportional to the size of the collection.
	 * 
	 * @param coll
	 *            the collection whose maximum element is to be determined.
	 * @param comp
	 *            the comparator with which to determine the maximum element. A
	 *            <tt>null</tt> value indicates that the elements' <i>natural
	 *            ordering</i> should be used.
	 * @return the maximum element of the given collection, according to the
	 *         specified comparator.
	 * @throws ClassCastException
	 *             if the collection contains elements that are not <i>mutually
	 *             comparable</i> using the specified comparator.
	 * @throws NoSuchElementException
	 *             if the collection is empty.
	 * @see Comparable
	 */
	public static Object max(Collection coll, Comparator comp) {
		if (comp == null)
			return (max(coll));

		Iterator i = coll.iterator();
		Object candidate = i.next();

		while (i.hasNext()) {
			Object next = i.next();
			if (comp.compare(next, candidate) > 0)
				candidate = next;
		}
		return candidate;
	}

	/**
	 * Rotates the elements in the specified list by the specified distance.
	 * After calling this method, the element at index <tt>i</tt> will be the
	 * element previously at index <tt>(i - distance)</tt> mod
	 * <tt>list.size()</tt>, for all values of <tt>i</tt> between <tt>0</tt> and
	 * <tt>list.size()-1</tt>, inclusive. (This method has no effect on the size
	 * of the list.)
	 * 
	 * <p>
	 * For example, suppose <tt>list</tt> comprises<tt> [t, a, n, k, s]</tt>.
	 * After invoking <tt>Collections.rotate(list, 1)</tt> (or
	 * <tt>Collections.rotate(list, -4)</tt>), <tt>list</tt> will comprise
	 * <tt>[s, t, a, n, k]</tt>.
	 * 
	 * <p>
	 * Note that this method can usefully be applied to sublists to move one or
	 * more elements within a list while preserving the order of the remaining
	 * elements. For example, the following idiom moves the element at index
	 * <tt>j</tt> forward to position <tt>k</tt> (which must be greater than or
	 * equal to <tt>j</tt>):
	 * 
	 * <pre>
	 * Collections.rotate(list.subList(j, k + 1), -1);
	 * </pre>
	 * 
	 * To make this concrete, suppose <tt>list</tt> comprises
	 * <tt>[a, b, c, d, e]</tt>. To move the element at index <tt>1</tt> (
	 * <tt>b</tt>) forward two positions, perform the following invocation:
	 * 
	 * <pre>
	 * Collections.rotate(l.subList(1, 4), -1);
	 * </pre>
	 * 
	 * The resulting list is <tt>[a, c, d, b, e]</tt>.
	 * 
	 * <p>
	 * To move more than one element forward, increase the absolute value of the
	 * rotation distance. To move elements backward, use a positive shift
	 * distance.
	 * 
	 * <p>
	 * If the specified list is small or implements the {@link RandomAccess}
	 * interface, this implementation exchanges the first element into the
	 * location it should go, and then repeatedly exchanges the displaced
	 * element into the location it should go until a displaced element is
	 * swapped into the first element. If necessary, the process is repeated on
	 * the second and successive elements, until the rotation is complete. If
	 * the specified list is large and doesn't implement the
	 * <tt>RandomAccess</tt> interface, this implementation breaks the list into
	 * two sublist views around index <tt>-distance mod size</tt>. Then the
	 * {@link #reverse(List)} method is invoked on each sublist view, and
	 * finally it is invoked on the entire list. For a more complete description
	 * of both algorithms, see Section 2.3 of Jon Bentley's <i>Programming
	 * Pearls</i> (Addison-Wesley, 1986).
	 * 
	 * @param list
	 *            the list to be rotated.
	 * @param distance
	 *            the distance to rotate the list. There are no constraints on
	 *            this value; it may be zero, negative, or greater than
	 *            <tt>list.size()</tt>.
	 * @throws UnsupportedOperationException
	 *             if the specified list or its list-iterator does not support
	 *             the <tt>set</tt> operation.
	 * @since 1.4
	 */
	public static void rotate(List list, int distance) {
		if (list instanceof RandomAccess || list.size() < ROTATE_THRESHOLD)
			rotate1((List) list, distance);
		else
			rotate2((List) list, distance);
	}

	private static void rotate1(List list, int distance) {
		int size = list.size();
		if (size == 0)
			return;
		distance = distance % size;
		if (distance < 0)
			distance += size;
		if (distance == 0)
			return;

		for (int cycleStart = 0, nMoved = 0; nMoved != size; cycleStart++) {
			Object displaced = list.get(cycleStart);
			int i = cycleStart;
			do {
				i += distance;
				if (i >= size)
					i -= size;
				displaced = list.set(i, displaced);
				nMoved++;
			} while (i != cycleStart);
		}
	}

	private static void rotate2(List list, int distance) {
		int size = list.size();
		if (size == 0)
			return;
		int mid = -distance % size;
		if (mid < 0)
			mid += size;
		if (mid == 0)
			return;

		reverse(list.subList(0, mid));
		reverse(list.subList(mid, size));
		reverse(list);
	}

	/**
	 * Replaces all occurrences of one specified value in a list with another.
	 * More formally, replaces with <tt>newVal</tt> each element <tt>e</tt> in
	 * <tt>list</tt> such that
	 * <tt>(oldVal==null ? e==null : oldVal.equals(e))</tt>. (This method has no
	 * effect on the size of the list.)
	 * 
	 * @param list
	 *            the list in which replacement is to occur.
	 * @param oldVal
	 *            the old value to be replaced.
	 * @param newVal
	 *            the new value with which <tt>oldVal</tt> is to be replaced.
	 * @return <tt>true</tt> if <tt>list</tt> contained one or more elements
	 *         <tt>e</tt> such that
	 *         <tt>(oldVal==null ?  e==null : oldVal.equals(e))</tt>.
	 * @throws UnsupportedOperationException
	 *             if the specified list or its list-iterator does not support
	 *             the <tt>set</tt> operation.
	 * @since 1.4
	 */
	public static boolean replaceAll(List list, Object oldVal, Object newVal) {
		boolean result = false;
		int size = list.size();
		if (size < REPLACEALL_THRESHOLD || list instanceof RandomAccess) {
			if (oldVal == null) {
				for (int i = 0; i < size; i++) {
					if (list.get(i) == null) {
						list.set(i, newVal);
						result = true;
					}
				}
			} else {
				for (int i = 0; i < size; i++) {
					if (oldVal.equals(list.get(i))) {
						list.set(i, newVal);
						result = true;
					}
				}
			}
		} else {
			ListIterator itr = list.listIterator();
			if (oldVal == null) {
				for (int i = 0; i < size; i++) {
					if (itr.next() == null) {
						itr.set(newVal);
						result = true;
					}
				}
			} else {
				for (int i = 0; i < size; i++) {
					if (oldVal.equals(itr.next())) {
						itr.set(newVal);
						result = true;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Returns the starting position of the first occurrence of the specified
	 * target list within the specified source list, or -1 if there is no such
	 * occurrence. More formally, returns the lowest index <tt>i</tt> such that
	 * <tt>source.subList(i, i+target.size()).equals(target)</tt>, or -1 if
	 * there is no such index. (Returns -1 if
	 * <tt>target.size() > source.size()</tt>.)
	 * 
	 * <p>
	 * This implementation uses the "brute force" technique of scanning over the
	 * source list, looking for a match with the target at each location in
	 * turn.
	 * 
	 * @param source
	 *            the list in which to search for the first occurrence of
	 *            <tt>target</tt>.
	 * @param target
	 *            the list to search for as a subList of <tt>source</tt>.
	 * @return the starting position of the first occurrence of the specified
	 *         target list within the specified source list, or -1 if there is
	 *         no such occurrence.
	 * @since 1.4
	 */
	public static int indexOfSubList(List source, List target) {
		int sourceSize = source.size();
		int targetSize = target.size();
		int maxCandidate = sourceSize - targetSize;

		if (sourceSize < INDEXOFSUBLIST_THRESHOLD
				|| (source instanceof RandomAccess && target instanceof RandomAccess)) {
			nextCand: for (int candidate = 0; candidate <= maxCandidate; candidate++) {
				for (int i = 0, j = candidate; i < targetSize; i++, j++)
					if (!eq(target.get(i), source.get(j)))
						continue nextCand; // Element mismatch, try next cand
				return candidate; // All elements of candidate matched target
			}
		} else { // Iterator version of above algorithm
			ListIterator si = source.listIterator();
			nextCand: for (int candidate = 0; candidate <= maxCandidate; candidate++) {
				ListIterator ti = target.listIterator();
				for (int i = 0; i < targetSize; i++) {
					if (!eq(ti.next(), si.next())) {
						// Back up source iterator to next candidate
						for (int j = 0; j < i; j++)
							si.previous();
						continue nextCand;
					}
				}
				return candidate;
			}
		}
		return -1; // No candidate matched the target
	}

	/**
	 * Returns the starting position of the last occurrence of the specified
	 * target list within the specified source list, or -1 if there is no such
	 * occurrence. More formally, returns the highest index <tt>i</tt> such that
	 * <tt>source.subList(i, i+target.size()).equals(target)</tt>, or -1 if
	 * there is no such index. (Returns -1 if
	 * <tt>target.size() > source.size()</tt>.)
	 * 
	 * <p>
	 * This implementation uses the "brute force" technique of iterating over
	 * the source list, looking for a match with the target at each location in
	 * turn.
	 * 
	 * @param source
	 *            the list in which to search for the last occurrence of
	 *            <tt>target</tt>.
	 * @param target
	 *            the list to search for as a subList of <tt>source</tt>.
	 * @return the starting position of the last occurrence of the specified
	 *         target list within the specified source list, or -1 if there is
	 *         no such occurrence.
	 * @since 1.4
	 */
	public static int lastIndexOfSubList(List source, List target) {
		int sourceSize = source.size();
		int targetSize = target.size();
		int maxCandidate = sourceSize - targetSize;

		if (sourceSize < INDEXOFSUBLIST_THRESHOLD
				|| source instanceof RandomAccess) { // Index access version
			nextCand: for (int candidate = maxCandidate; candidate >= 0; candidate--) {
				for (int i = 0, j = candidate; i < targetSize; i++, j++)
					if (!eq(target.get(i), source.get(j)))
						continue nextCand; // Element mismatch, try next cand
				return candidate; // All elements of candidate matched target
			}
		} else { // Iterator version of above algorithm
			if (maxCandidate < 0)
				return -1;
			ListIterator si = source.listIterator(maxCandidate);
			nextCand: for (int candidate = maxCandidate; candidate >= 0; candidate--) {
				ListIterator ti = target.listIterator();
				for (int i = 0; i < targetSize; i++) {
					if (!eq(ti.next(), si.next())) {
						if (candidate != 0) {
							// Back up source iterator to next candidate
							for (int j = 0; j <= i + 1; j++)
								si.previous();
						}
						continue nextCand;
					}
				}
				return candidate;
			}
		}
		return -1; // No candidate matched the target
	}

	// Unmodifiable Wrappers

	/**
	 * Returns an unmodifiable view of the specified collection. This method
	 * allows modules to provide users with "read-only" access to internal
	 * collections. Query operations on the returned collection "read through"
	 * to the specified collection, and attempts to modify the returned
	 * collection, whether direct or via its iterator, result in an
	 * <tt>UnsupportedOperationException</tt>.
	 * <p>
	 * 
	 * The returned collection does <i>not</i> pass the hashCode and equals
	 * operations through to the backing collection, but relies on
	 * <tt>Object</tt>'s <tt>equals</tt> and <tt>hashCode</tt> methods. This is
	 * necessary to preserve the contracts of these operations in the case that
	 * the backing collection is a set or a list.
	 * <p>
	 * 
	 * The returned collection will be serializable if the specified collection
	 * is serializable.
	 * 
	 * @param c
	 *            the collection for which an unmodifiable view is to be
	 *            returned.
	 * @return an unmodifiable view of the specified collection.
	 */
	public static Collection unmodifiableCollection(Collection c) {
		return new UnmodifiableCollection(c);
	}

	/**
	 * @serial include
	 */
	static class UnmodifiableCollection implements Collection {
		// use serialVersionUID from JDK 1.2.2 for interoperability
		private static final long serialVersionUID = 1820017752578914078L;

		final Collection c;

		UnmodifiableCollection(Collection c) {
			if (c == null)
				throw new NullPointerException();
			this.c = c;
		}

		public int size() {
			return c.size();
		}

		public boolean isEmpty() {
			return c.isEmpty();
		}

		public boolean contains(Object o) {
			return c.contains(o);
		}

		public Object[] toArray() {
			return c.toArray();
		}

		public String toString() {
			return c.toString();
		}

		public Iterator iterator() {
			return new Iterator() {
				Iterator i = c.iterator();

				public boolean hasNext() {
					return i.hasNext();
				}

				public Object next() {
					return i.next();
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}

		public boolean add(Object e) {
			throw new UnsupportedOperationException();
		}

		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		public boolean containsAll(Collection coll) {
			return c.containsAll(coll);
		}

		public boolean addAll(Collection coll) {
			throw new UnsupportedOperationException();
		}

		public boolean removeAll(Collection coll) {
			throw new UnsupportedOperationException();
		}

		public boolean retainAll(Collection coll) {
			throw new UnsupportedOperationException();
		}

		public void clear() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Returns true if the specified arguments are equal, or both null.
	 */
	private static boolean eq(Object o1, Object o2) {
		return (o1 == null ? o2 == null : o1.equals(o2));
	}
}
