package com.mscg.util;

/**
 * Linked list implementation of the <tt>List</tt> interface.  Implements all
 * optional list operations, and permits all elements (including
 * <tt>null</tt>).  In addition to implementing the <tt>List</tt> interface,
 * the <tt>LinkedList</tt> class provides uniformly named methods to
 * <tt>get</tt>, <tt>remove</tt> and <tt>insert</tt> an element at the
 * beginning and end of the list.  These operations allow linked lists to be
 * used as a stack, {@linkplain Queue queue}, or {@linkplain Deque
 * double-ended queue}. <p>
 *
 * The class implements the <tt>Deque</tt> interface, providing
 * first-in-first-out queue operations for <tt>add</tt>,
 * <tt>poll</tt>, along with other stack and deque operations.<p>
 *
 * All of the operations perform as could be expected for a doubly-linked
 * list.  Operations that index into the list will traverse the list from
 * the beginning or the end, whichever is closer to the specified index.<p>
 *
 * <p><strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access a linked list concurrently, and at least
 * one of the threads modifies the list structurally, it <i>must</i> be
 * synchronized externally.  (A structural modification is any operation
 * that adds or deletes one or more elements; merely setting the value of
 * an element is not a structural modification.)  This is typically
 * accomplished by synchronizing on some object that naturally
 * encapsulates the list.
 *
 * If no such object exists, the list should be "wrapped" using the
 * {@link Collections#synchronizedList Collections.synchronizedList}
 * method.  This is best done at creation time, to prevent accidental
 * unsynchronized access to the list:<pre>
 *   List list = Collections.synchronizedList(new LinkedList(...));</pre>
 *
 * <p>The iterators returned by this class's <tt>iterator</tt> and
 * <tt>listIterator</tt> methods are <i>fail-fast</i>: if the list is
 * structurally modified at any time after the iterator is created, in
 * any way except through the Iterator's own <tt>remove</tt> or
 * <tt>add</tt> methods, the iterator will throw a {@link
 * ConcurrentModificationException}.  Thus, in the face of concurrent
 * modification, the iterator fails quickly and cleanly, rather than
 * risking arbitrary, non-deterministic behavior at an undetermined
 * time in the future.
 *
 * <p>Note that the fail-fast behavior of an iterator cannot be guaranteed
 * as it is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification.  Fail-fast iterators
 * throw <tt>ConcurrentModificationException</tt> on a best-effort basis.
 * Therefore, it would be wrong to write a program that depended on this
 * exception for its correctness:   <i>the fail-fast behavior of iterators
 * should be used only to detect bugs.</i>
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @author  Josh Bloch
 * @version 1.67, 04/21/06
 * @see	    List
 * @see	    ArrayList
 * @see	    Vector
 * @since 1.2
 * @param <E> the type of elements held in this collection
 */

public class LinkedList extends AbstractSequentialList
    implements List, Deque
{
    private transient Entry header = new Entry(null, null, null);
    private transient int size = 0;

    /**
     * Constructs an empty list.
     */
    public LinkedList() {
        header.next = header.previous = header;
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param  c the collection whose elements are to be placed into this list
     * @throws NullPointerException if the specified collection is null
     */
    public LinkedList(Collection c) {
	this();
	addAll(c);
    }

    /**
     * Returns the first element in this list.
     *
     * @return the first element in this list
     * @throws NoSuchElementException if this list is empty
     */
    public Object getFirst() {
	if (size==0)
	    throw new NoSuchElementException();

	return header.next.element;
    }

    /**
     * Returns the last element in this list.
     *
     * @return the last element in this list
     * @throws NoSuchElementException if this list is empty
     */
    public Object getLast()  {
	if (size==0)
	    throw new NoSuchElementException();

	return header.previous.element;
    }

    /**
     * Removes and returns the first element from this list.
     *
     * @return the first element from this list
     * @throws NoSuchElementException if this list is empty
     */
    public Object removeFirst() {
	return remove(header.next);
    }

    /**
     * Removes and returns the last element from this list.
     *
     * @return the last element from this list
     * @throws NoSuchElementException if this list is empty
     */
    public Object removeLast() {
	return remove(header.previous);
    }

    /**
     * Inserts the specified element at the beginning of this list.
     *
     * @param e the element to add
     */
    public void addFirst(Object e) {
	addBefore(e, header.next);
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * <p>This method is equivalent to {@link #add}.
     *
     * @param e the element to add
     */
    public void addLast(Object e) {
	addBefore(e, header);
    }

    /**
     * Returns <tt>true</tt> if this list contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this list contains
     * at least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param o element whose presence in this list is to be tested
     * @return <tt>true</tt> if this list contains the specified element
     */
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size() {
	return size;
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * <p>This method is equivalent to {@link #addLast}.
     *
     * @param e element to be appended to this list
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public boolean add(Object e) {
	addBefore(e, header);
        return true;
    }

    /**
     * Removes the first occurrence of the specified element from this list,
     * if it is present.  If this list does not contain the element, it is
     * unchanged.  More formally, removes the element with the lowest index
     * <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
     * (if such an element exists).  Returns <tt>true</tt> if this list
     * contained the specified element (or equivalently, if this list
     * changed as a result of the call).
     *
     * @param o element to be removed from this list, if present
     * @return <tt>true</tt> if this list contained the specified element
     */
    public boolean remove(Object o) {
        if (o==null) {
            for (Entry e = header.next; e != header; e = e.next) {
                if (e.element==null) {
                    remove(e);
                    return true;
                }
            }
        } else {
            for (Entry e = header.next; e != header; e = e.next) {
                if (o.equals(e.element)) {
                    remove(e);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the specified
     * collection's iterator.  The behavior of this operation is undefined if
     * the specified collection is modified while the operation is in
     * progress.  (Note that this will occur if the specified collection is
     * this list, and it's nonempty.)
     *
     * @param c collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     */
    public boolean addAll(Collection c) {
        return addAll(size, c);
    }

    /**
     * Inserts all of the elements in the specified collection into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).  The new elements will appear
     * in the list in the order that they are returned by the
     * specified collection's iterator.
     *
     * @param index index at which to insert the first element
     *              from the specified collection
     * @param c collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws NullPointerException if the specified collection is null
     */
    public boolean addAll(int index, Collection c) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: "+index+
                                                ", Size: "+size);
        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew==0)
            return false;
	modCount++;

        Entry successor = (index==size ? header : entry(index));
        Entry predecessor = successor.previous;
	for (int i=0; i<numNew; i++) {
            Entry e = new Entry(a[i], successor, predecessor);
            predecessor.next = e;
            predecessor = e;
        }
        successor.previous = predecessor;

        size += numNew;
        return true;
    }

    /**
     * Removes all of the elements from this list.
     */
    public void clear() {
        Entry e = header.next;
        while (e != header) {
            Entry next = e.next;
            e.next = e.previous = null;
            e.element = null;
            e = next;
        }
        header.next = header.previous = header;
        size = 0;
	modCount++;
    }


    // Positional Access Operations

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public Object get(int index) {
        return entry(index).element;
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     *
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public Object set(int index, Object element) {
        Entry e = entry(index);
        Object oldVal = e.element;
        e.element = element;
        return oldVal;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any
     * subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public void add(int index, Object element) {
        addBefore(element, (index==size ? header : entry(index)));
    }

    /**
     * Removes the element at the specified position in this list.  Shifts any
     * subsequent elements to the left (subtracts one from their indices).
     * Returns the element that was removed from the list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public Object remove(int index) {
        return remove(entry(index));
    }

    /**
     * Returns the indexed entry.
     */
    private Entry entry(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: "+index+
                                                ", Size: "+size);
        Entry e = header;
        if (index < (size >> 1)) {
            for (int i = 0; i <= index; i++)
                e = e.next;
        } else {
            for (int i = size; i > index; i--)
                e = e.previous;
        }
        return e;
    }


    // Search Operations

    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     *
     * @param o element to search for
     * @return the index of the first occurrence of the specified element in
     *         this list, or -1 if this list does not contain the element
     */
    public int indexOf(Object o) {
        int index = 0;
        if (o==null) {
            for (Entry e = header.next; e != header; e = e.next) {
                if (e.element==null)
                    return index;
                index++;
            }
        } else {
            for (Entry e = header.next; e != header; e = e.next) {
                if (o.equals(e.element))
                    return index;
                index++;
            }
        }
        return -1;
    }

    /**
     * Returns the index of the last occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the highest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     *
     * @param o element to search for
     * @return the index of the last occurrence of the specified element in
     *         this list, or -1 if this list does not contain the element
     */
    public int lastIndexOf(Object o) {
        int index = size;
        if (o==null) {
            for (Entry e = header.previous; e != header; e = e.previous) {
                index--;
                if (e.element==null)
                    return index;
            }
        } else {
            for (Entry e = header.previous; e != header; e = e.previous) {
                index--;
                if (o.equals(e.element))
                    return index;
            }
        }
        return -1;
    }

    // Queue operations.

    /**
     * Retrieves, but does not remove, the head (first element) of this list.
     * @return the head of this list, or <tt>null</tt> if this list is empty
     * @since 1.5
     */
    public Object peek() {
        if (size==0)
            return null;
        return getFirst();
    }

    /**
     * Retrieves, but does not remove, the head (first element) of this list.
     * @return the head of this list
     * @throws NoSuchElementException if this list is empty
     * @since 1.5
     */
    public Object element() {
        return getFirst();
    }

    /**
     * Retrieves and removes the head (first element) of this list
     * @return the head of this list, or <tt>null</tt> if this list is empty
     * @since 1.5
     */
    public Object poll() {
        if (size==0)
            return null;
        return removeFirst();
    }

    /**
     * Retrieves and removes the head (first element) of this list.
     *
     * @return the head of this list
     * @throws NoSuchElementException if this list is empty
     * @since 1.5
     */
    public Object remove() {
        return removeFirst();
    }

    /**
     * Adds the specified element as the tail (last element) of this list.
     *
     * @param e the element to add
     * @return <tt>true</tt> (as specified by {@link Queue#offer})
     * @since 1.5
     */
    public boolean offer(Object e) {
        return add(e);
    }

    // Deque operations
    /**
     * Inserts the specified element at the front of this list.
     *
     * @param e the element to insert
     * @return <tt>true</tt> (as specified by {@link Deque#offerFirst})
     * @since 1.6
     */
    public boolean offerFirst(Object e) {
        addFirst(e);
        return true;
    }

    /**
     * Inserts the specified element at the end of this list.
     *
     * @param e the element to insert
     * @return <tt>true</tt> (as specified by {@link Deque#offerLast})
     * @since 1.6
     */
    public boolean offerLast(Object e) {
        addLast(e);
        return true;
    }

    /**
     * Retrieves, but does not remove, the first element of this list,
     * or returns <tt>null</tt> if this list is empty.
     *
     * @return the first element of this list, or <tt>null</tt>
     *         if this list is empty
     * @since 1.6
     */
    public Object peekFirst() {
        if (size==0)
            return null;
        return getFirst();
    }

    /**
     * Retrieves, but does not remove, the last element of this list,
     * or returns <tt>null</tt> if this list is empty.
     *
     * @return the last element of this list, or <tt>null</tt>
     *         if this list is empty
     * @since 1.6
     */
    public Object peekLast() {
        if (size==0)
            return null;
        return getLast();
    }

    /**
     * Retrieves and removes the first element of this list,
     * or returns <tt>null</tt> if this list is empty.
     *
     * @return the first element of this list, or <tt>null</tt> if
     *     this list is empty
     * @since 1.6
     */
    public Object pollFirst() {
        if (size==0)
            return null;
        return removeFirst();
    }

    /**
     * Retrieves and removes the last element of this list,
     * or returns <tt>null</tt> if this list is empty.
     *
     * @return the last element of this list, or <tt>null</tt> if
     *     this list is empty
     * @since 1.6
     */
    public Object pollLast() {
        if (size==0)
            return null;
        return removeLast();
    }

    /**
     * Pushes an element onto the stack represented by this list.  In other
     * words, inserts the element at the front of this list.
     *
     * <p>This method is equivalent to {@link #addFirst}.
     *
     * @param e the element to push
     * @since 1.6
     */
    public void push(Object e) {
        addFirst(e);
    }

    /**
     * Pops an element from the stack represented by this list.  In other
     * words, removes and returns the first element of this list.
     *
     * <p>This method is equivalent to {@link #removeFirst()}.
     *
     * @return the element at the front of this list (which is the top
     *         of the stack represented by this list)
     * @throws NoSuchElementException if this list is empty
     * @since 1.6
     */
    public Object pop() {
        return removeFirst();
    }

    /**
     * Removes the first occurrence of the specified element in this
     * list (when traversing the list from head to tail).  If the list
     * does not contain the element, it is unchanged.
     *
     * @param o element to be removed from this list, if present
     * @return <tt>true</tt> if the list contained the specified element
     * @since 1.6
     */
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    /**
     * Removes the last occurrence of the specified element in this
     * list (when traversing the list from head to tail).  If the list
     * does not contain the element, it is unchanged.
     *
     * @param o element to be removed from this list, if present
     * @return <tt>true</tt> if the list contained the specified element
     * @since 1.6
     */
    public boolean removeLastOccurrence(Object o) {
        if (o==null) {
            for (Entry e = header.previous; e != header; e = e.previous) {
                if (e.element==null) {
                    remove(e);
                    return true;
                }
            }
        } else {
            for (Entry e = header.previous; e != header; e = e.previous) {
                if (o.equals(e.element)) {
                    remove(e);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a list-iterator of the elements in this list (in proper
     * sequence), starting at the specified position in the list.
     * Obeys the general contract of <tt>List.listIterator(int)</tt>.<p>
     *
     * The list-iterator is <i>fail-fast</i>: if the list is structurally
     * modified at any time after the Iterator is created, in any way except
     * through the list-iterator's own <tt>remove</tt> or <tt>add</tt>
     * methods, the list-iterator will throw a
     * <tt>ConcurrentModificationException</tt>.  Thus, in the face of
     * concurrent modification, the iterator fails quickly and cleanly, rather
     * than risking arbitrary, non-deterministic behavior at an undetermined
     * time in the future.
     *
     * @param index index of the first element to be returned from the
     *              list-iterator (by a call to <tt>next</tt>)
     * @return a ListIterator of the elements in this list (in proper
     *         sequence), starting at the specified position in the list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @see List#listIterator(int)
     */
    public ListIterator listIterator(int index) {
	return new ListItr(index);
    }

    private class ListItr implements ListIterator {
	private Entry lastReturned = header;
	private Entry next;
	private int nextIndex;
	private int expectedModCount = modCount;

	ListItr(int index) {
	    if (index < 0 || index > size)
		throw new IndexOutOfBoundsException("Index: "+index+
						    ", Size: "+size);
	    if (index < (size >> 1)) {
		next = header.next;
		for (nextIndex=0; nextIndex<index; nextIndex++)
		    next = next.next;
	    } else {
		next = header;
		for (nextIndex=size; nextIndex>index; nextIndex--)
		    next = next.previous;
	    }
	}

	public boolean hasNext() {
	    return nextIndex != size;
	}

	public Object next() {
	    checkForComodification();
	    if (nextIndex == size)
		throw new NoSuchElementException();

	    lastReturned = next;
	    next = next.next;
	    nextIndex++;
	    return lastReturned.element;
	}

	public boolean hasPrevious() {
	    return nextIndex != 0;
	}

	public Object previous() {
	    if (nextIndex == 0)
		throw new NoSuchElementException();

	    lastReturned = next = next.previous;
	    nextIndex--;
	    checkForComodification();
	    return lastReturned.element;
	}

	public int nextIndex() {
	    return nextIndex;
	}

	public int previousIndex() {
	    return nextIndex-1;
	}

	public void remove() {
            checkForComodification();
            Entry lastNext = lastReturned.next;
            try {
                LinkedList.this.remove(lastReturned);
            } catch (NoSuchElementException e) {
                throw new IllegalStateException();
            }
	    if (next==lastReturned)
                next = lastNext;
            else
		nextIndex--;
	    lastReturned = header;
	    expectedModCount++;
	}

	public void set(Object e) {
	    if (lastReturned == header)
		throw new IllegalStateException();
	    checkForComodification();
	    lastReturned.element = e;
	}

	public void add(Object e) {
	    checkForComodification();
	    lastReturned = header;
	    addBefore(e, next);
	    nextIndex++;
	    expectedModCount++;
	}

	final void checkForComodification() {
	    if (modCount != expectedModCount)
		throw new ConcurrentModificationException();
	}
    }

    private static class Entry {
	Object element;
	Entry next;
	Entry previous;

	Entry(Object element, Entry next, Entry previous) {
	    this.element = element;
	    this.next = next;
	    this.previous = previous;
	}
    }

    private Entry addBefore(Object e, Entry entry) {
	Entry newEntry = new Entry(e, entry, entry.previous);
	newEntry.previous.next = newEntry;
	newEntry.next.previous = newEntry;
	size++;
	modCount++;
	return newEntry;
    }

    private Object remove(Entry e) {
	if (e == header)
	    throw new NoSuchElementException();

        Object result = e.element;
	e.previous.next = e.next;
	e.next.previous = e.previous;
        e.next = e.previous = null;
        e.element = null;
	size--;
	modCount++;
        return result;
    }

    /**
     * @since 1.6
     */
    public Iterator descendingIterator() {
        return new DescendingIterator();
    }

    /** Adapter to provide descending iterators via ListItr.previous */
    private class DescendingIterator implements Iterator {
        final ListItr itr = new ListItr(size());
	public boolean hasNext() {
	    return itr.hasPrevious();
	}
	public Object next() {
            return itr.previous();
        }
	public void remove() {
            itr.remove();
        }
    }

    /**
     * Returns an array containing all of the elements in this list
     * in proper sequence (from first to last element).
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this list.  (In other words, this method must allocate
     * a new array).  The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this list
     *         in proper sequence
     */
    public Object[] toArray() {
	Object[] result = new Object[size];
        int i = 0;
        for (Entry e = header.next; e != header; e = e.next)
            result[i++] = e.element;
	return result;
    }

    private static final long serialVersionUID = 876323262645176354L;
}
