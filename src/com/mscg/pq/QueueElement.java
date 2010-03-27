/**
 *
 */
package com.mscg.pq;

/**
 * @author Giuseppe Miscione
 *
 */
public class QueueElement implements Comparable<QueueElement> {

	private float priority;
	private String value;

	public QueueElement(float priority, String value){
		setPriority(priority);
		setValue(value);
	}

	public int compareTo(QueueElement q2) {
		if(priority < q2.priority)
			return -1;
		else if(priority == q2.priority)
			return 0;
		else
			return 1;
	}

	/**
	 * @return the priority
	 */
	public float getPriority() {
		return priority;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(float priority) {
		this.priority = priority;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return "(" + priority + ", " + value + ")";
	}

}
