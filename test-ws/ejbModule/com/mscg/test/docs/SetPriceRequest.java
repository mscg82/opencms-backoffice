package com.mscg.test.docs;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType
@XmlType(name="SetPriceRequestType", namespace="http://test.mscg.com/xsd", propOrder={"prices"})
@XmlRootElement(namespace="http://test.mscg.com/xsd")
public class SetPriceRequest {
	private List<PriceBean> pricesValues;

	public void addPrice(PriceBean price){
		if(pricesValues == null)
			pricesValues = new LinkedList<PriceBean>();
		pricesValues.add(price);
	}

	/**
	 * @return the prices
	 */
	@XmlElement(namespace="http://test.mscg.com/xsd",  required = true)
	public List<PriceBean> getPrices() {
		return pricesValues;
	}

	/**
	 * @param pricesValues the prices to set
	 */
	public void setPrices(List<PriceBean> pricesValues) {
		this.pricesValues = pricesValues;
	}
}
