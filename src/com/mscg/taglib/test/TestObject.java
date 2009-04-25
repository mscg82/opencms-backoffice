/**
 *
 */
package com.mscg.taglib.test;

/**
 * @author Giuseppe Miscione
 *
 */
public class TestObject {
	/**
	 * @return the doubleVal
	 */
	public static Double getDoubleVal() {
		return doubleVal;
	}
	/**
	 * @param doubleVal the doubleVal to set
	 */
	public static void setDoubleVal(Double doubleVal) {
		TestObject.doubleVal = doubleVal;
	}
	private Integer intero;

	private String stringa;

	private TestObject innerObject;

	private static Double doubleVal;

	public TestObject(){

	}

	public TestObject(int intero){
		init(intero, null, null);
	}

	public TestObject(int intero, String stringa){
		init(intero, stringa, null);
	}

	public TestObject(int intero, String stringa, TestObject innerObject){
		init(intero, stringa, innerObject);
	}

	/**
	 * @return the innerObject
	 */
	public TestObject getInnerObject() {
		return innerObject;
	}

	/**
	 * @return the intero
	 */
	public Integer getIntero() {
		return intero;
	}

	/**
	 * @return the stringa
	 */
	public String getStringa() {
		return stringa;
	}

	public void init(int intero, String stringa, TestObject innerObject) {
		setIntero(intero);
		setStringa(stringa);
		setInnerObject(innerObject);
	}

	/**
	 * @param innerObject the innerObject to set
	 */
	public void setInnerObject(TestObject innerObject) {
		this.innerObject = innerObject;
	}

	/**
	 * @param intero the intero to set
	 */
	public void setIntero(Integer intero) {
		this.intero = intero;
	}

	/**
	 * @param stringa the stringa to set
	 */
	public void setStringa(String stringa) {
		this.stringa = stringa;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "" + intero + " " + stringa + " [" + (innerObject != null ? innerObject.toString() : "") + "]";
	}
}
