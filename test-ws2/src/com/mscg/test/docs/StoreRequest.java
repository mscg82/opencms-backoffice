package com.mscg.test.docs;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType
@XmlType(name="StoreRequestType", namespace="http://test.mscg.com/xsd", propOrder={"product"})
@XmlRootElement(namespace="http://test.mscg.com/xsd")
public class StoreRequest {
	private String productValue;

	public StoreRequest(){

	}

	public StoreRequest(String product){
		setProduct(product);
	}

	/**
	 * @return the product
	 */
	@XmlElement(namespace="http://test.mscg.com/xsd",  required = true)
	public String getProduct() {
		return productValue;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.productValue = product;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "The requested product is: " + getProduct();
	}
}
