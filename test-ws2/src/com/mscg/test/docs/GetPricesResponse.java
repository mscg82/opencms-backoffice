package com.mscg.test.docs;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType
@XmlType(name="GetPricesResponseType", namespace="http://test.mscg.com/xsd")
@XmlRootElement(namespace="http://test.mscg.com/xsd")
public class GetPricesResponse extends SetPriceRequest {

	public GetPricesResponse() {
		super();
	}

}
