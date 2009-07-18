package com.mscg.test.docs;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType
@XmlType(name="PriceBeanType", namespace="http://test.mscg.com/xsd", propOrder={"product", "price"})
public class PriceBean {
	private double priceValue;
	private String productValue;

	public PriceBean(){

	}

	public PriceBean(String product, double price){
		setPrice(price);
		setProduct(product);
	}

	/**
	 * @return the price
	 */
	@XmlElement(namespace="http://test.mscg.com/xsd",  required = true)
	public double getPrice() {
		return priceValue;
	}

	/**
	 * @return the productValue
	 */
	@XmlElement(namespace="http://test.mscg.com/xsd",  required = true)
	public String getProduct() {
		return productValue;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.priceValue = price;
	}

	/**
	 * @param productValue the productValue to set
	 */
	public void setProduct(String productValue) {
		this.productValue = productValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "The price for " + getProduct() + " is: " + getPrice();
	}
}
