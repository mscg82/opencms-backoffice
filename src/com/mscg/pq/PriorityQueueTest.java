/**
 *
 */
package com.mscg.pq;

import java.util.Iterator;


/**
 * @author Giuseppe Miscione
 *
 */
public class PriorityQueueTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		QueueElement el1 = new QueueElement(3.0f, "el1");
		QueueElement el2 = new QueueElement(1.0f, "el2");
		QueueElement el3 = new QueueElement(2.1f, "el3");
		QueueElement el4 = new QueueElement(4.6f, "el4");
		QueueElement el5 = new QueueElement(1.2f, "el5");
		QueueElement el6 = new QueueElement(5.7f, "el6");

		ModifiablePriorityQueue<QueueElement> pq = new ModifiablePriorityQueue<QueueElement>();
		pq.add(el1);
		pq.add(el2);
		pq.add(el3);
		pq.add(el4);
		pq.add(el5);
		pq.add(el6);

		System.out.println("Priority queue:\n" + pq.toString());

		System.out.print("Priority queue:\n[");
		for(Iterator<QueueElement> it = pq.iterator(); it.hasNext(); ){
			QueueElement el = it.next();
			System.out.print(el.toString());
			if(it.hasNext())
				System.out.print(", ");
		}
		System.out.println("]");

		el5.setPriority(0.8f);

		pq.heapify();

		System.out.println("Priority queue:\n" + pq.toString());

	}

}
