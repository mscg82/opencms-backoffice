/**
 *
 */
package com.mscg.pq;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.SortedSet;

/**
 * @author Giuseppe Miscione
 *
 */
public class ModifiablePriorityQueue<E> extends PriorityQueue<E> {

	private static final long serialVersionUID = -6364996675092941970L;

	public ModifiablePriorityQueue() {
		super();
	}

	public ModifiablePriorityQueue(Collection<? extends E> c) {
		super(c);
	}

	public ModifiablePriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
		super(initialCapacity, comparator);
	}

	public ModifiablePriorityQueue(int initialCapacity) {
		super(initialCapacity);
	}

	public ModifiablePriorityQueue(PriorityQueue<? extends E> c) {
		super(c);
	}

	public ModifiablePriorityQueue(SortedSet<? extends E> c) {
		super(c);
	}

	public void heapify(){
		try {
			Method heapify = getClass().getSuperclass().getDeclaredMethod("heapify");
			heapify.setAccessible(true);
			heapify.invoke(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
