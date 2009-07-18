package com.mscg.test.docs;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="StoreResponseType", namespace="http://test.mscg.com/xsd")
@XmlRootElement(namespace="http://test.mscg.com/xsd")
public class StoreResponse extends PriceBean{

	public StoreResponse(){
		super();
	}

	public StoreResponse(String product, double price){
		super(product, price);
	}

}
