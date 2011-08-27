package com.mscg.util;

/**
 * This class implements the <tt>Set</tt> interface, backed by a hash table
 * (actually a <tt>HashMap</tt> instance). It makes no guarantees as to the
 * iteration order of the set; in particular, it does not guarantee that the
 * order will remain constant over time. This class permits the <tt>null</tt>
 * element.
 * 
 * <p>
 * This class offers constant time performance for the basic operations (
 * <tt>add</tt>, <tt>remove</tt>, <tt>contains</tt> and <tt>size</tt>), assuming
 * the hash function disperses the elements properly among the buckets.
 * Iterating over this set requires time proportional to the sum of the
 * <tt>HashSet</tt> instance's size (the number of elements) plus the "capacity"
 * of the backing <tt>HashMap</tt> instance (the number of buckets). Thus, it's
 * very important not to set the initial capacity too high (or the load factor
 * too low) if iteration performance is important.
 * 
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If
 * multiple threads access a hash set concurrently, and at least one of the
 * threads modifies the set, it <i>must</i> be synchronized externally. This is
 * typically accomplished by synchronizing on some object that naturally
 * encapsulates the set.
 * 
 * If no such object exists, the set should be "wrapped" using the
 * {@link Collections#synchronizedSet Collections.synchronizedSet} method. This
 * is best done at creation time, to prevent accidental unsynchronized access to
 * the set:
 * 
 * <pre>
 *   Set s = Collections.synchronizedSet(new HashSet(...));
 * </pre>
 * 
 * <p>
 * The iterators returned by this class's <tt>iterator</tt> method are
 * <i>fail-fast</i>: if the set is modified at any time after the iterator is
 * created, in any way except through the iterator's own <tt>remove</tt> method,
 * the Iterator throws a {@link ConcurrentModificationException}. Thus, in the
 * face of concurrent modification, the iterator fails quickly and cleanly,
 * rather than risking arbitrary, non-deterministic behavior at an undetermined
 * time in the future.
 * 
 * <p>
 * Note that the fail-fast behavior of an iterator cannot be guaranteed as it
 * is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification. Fail-fast iterators throw
 * <tt>ConcurrentModificationException</tt> on a best-effort basis. Therefore,
 * it would be wrong to write a program that depended on this exception for its
 * correctness: <i>the fail-fast behavior of iterators should be used only to
 * detect bugs.</i>
 * 
 * <p>
 * This class is a member of the <a href="{@docRoot}
 * /../technotes/guides/collections/index.html"> Java Collections Framework</a>.
 * 
 * @param <E> the type of elements maintained by this set
 * 
 * @author Josh Bloch
 * @author Neal Gafter
 * @version 1.37, 04/21/06
 * @see Collection
 * @see Set
 * @see TreeSet
 * @see HashMap
 * @since 1.2
 */

public class HashSet extends AbstractSet implements Set {
	static final long serialVersionUID = -5024744406713321676L;

	private transient HashMap map;

	// Dummy value to associate with an Object in the backing Map
	private static final Object PRESENT = new Object();

	/**
	 * Constructs a new, empty set; the backing <tt>HashMap</tt> instance has
	 * default initial capacity (16) and load factor (0.75).
	 */
	public HashSet() {
		map = new HashMap();
	}

	/**
	 * Constructs a new set containing the elements in the specified collection.
	 * The <tt>HashMap</tt> is created with default load factor (0.75) and an
	 * initial capacity sufficient to contain the elements in the specified
	 * collection.
	 * 
	 * @param c
	 *            the collection whose elements are to be placed into this set
	 * @throws NullPointerException
	 *             if the specified collection is null
	 */
	public HashSet(Collection c) {
		map = new HashMap(Math.max((int) (c.size() / .75f) + 1, 16));
		addAll(c);
	}

	/**
	 * Constructs a new, empty set; the backing <tt>HashMap</tt> instance has
	 * the specified initial capacity and the specified load factor.
	 * 
	 * @param initialCapacity
	 *            the initial capacity of the hash map
	 * @param loadFactor
	 *            the load factor of the hash map
	 * @throws IllegalArgumentException
	 *             if the initial capacity is less than zero, or if the load
	 *             factor is nonpositive
	 */
	public HashSet(int initialCapacity, float loadFactor) {
		map = new HashMap(initialCapacity, loadFactor);
	}

	/**
	 * Constructs a new, empty set; the backing <tt>HashMap</tt> instance has
	 * the specified initial capacity and default load factor (0.75).
	 * 
	 * @param initialCapacity
	 *            the initial capacity of the hash table
	 * @throws IllegalArgumentException
	 *             if the initial capacity is less than zero
	 */
	public HashSet(int initialCapacity) {
		map = new HashMap(initialCapacity);
	}

	/**
	 * Constructs a new, empty linked hash set. (This package private
	 * constructor is only used by LinkedHashSet.) The backing HashMap instance
	 * is a LinkedHashMap with the specified initial capacity and the specified
	 * load factor.
	 * 
	 * @param initialCapacity
	 *            the initial capacity of the hash map
	 * @param loadFactor
	 *            the load factor of the hash map
	 * @param dummy
	 *            ignored (distinguishes this constructor from other int, float
	 *            constructor.)
	 * @throws IllegalArgumentException
	 *             if the initial capacity is less than zero, or if the load
	 *             factor is nonpositive
	 */
	HashSet(int initialCapacity, float loadFactor, boolean dummy) {
		map = new LinkedHashMap(initialCapacity, loadFactor);
	}

	/**
	 * Returns an iterator over the elements in this set. The elements are
	 * returned in no particular order.
	 * 
	 * @return an Iterator over the elements in this set
	 * @see ConcurrentModificationException
	 */
	public Iterator iterator() {
		return map.keySet().iterator();
	}

	/**
	 * Returns the number of elements in this set (its cardinality).
	 * 
	 * @return the number of elements in this set (its cardinality)
	 */
	public int size() {
		return map.size();
	}

	/**
	 * Returns <tt>true</tt> if this set contains no elements.
	 * 
	 * @return <tt>true</tt> if this set contains no elements
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	 * Returns <tt>true</tt> if this set contains the specified element. More
	 * formally, returns <tt>true</tt> if and only if this set contains an
	 * element <tt>e</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
	 * 
	 * @param o
	 *            element whose presence in this set is to be tested
	 * @return <tt>true</tt> if this set contains the specified element
	 */
	public boolean contains(Object o) {
		return map.containsKey(o);
	}

	/**
	 * Adds the specified element to this set if it is not already present. More
	 * formally, adds the specified element <tt>e</tt> to this set if this set
	 * contains no element <tt>e2</tt> such that
	 * <tt>(e==null&nbsp;?&nbsp;e2==null&nbsp;:&nbsp;e.equals(e2))</tt>. If this
	 * set already contains the element, the call leaves the set unchanged and
	 * returns <tt>false</tt>.
	 * 
	 * @param e
	 *            element to be added to this set
	 * @return <tt>true</tt> if this set did not already contain the specified
	 *         element
	 */
	public boolean add(Object e) {
		return map.put(e, PRESENT) == null;
	}

	/**
	 * Removes the specified element from this set if it is present. More
	 * formally, removes an element <tt>e</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>, if this
	 * set contains such an element. Returns <tt>true</tt> if this set contained
	 * the element (or equivalently, if this set changed as a result of the
	 * call). (This set will not contain the element once the call returns.)
	 * 
	 * @param o
	 *            object to be removed from this set, if present
	 * @return <tt>true</tt> if the set contained the specified element
	 */
	public boolean remove(Object o) {
		return map.remove(o) == PRESENT;
	}

	/**
	 * Removes all of the elements from this set. The set will be empty after
	 * this call returns.
	 */
	public void clear() {
		map.clear();
	}
}