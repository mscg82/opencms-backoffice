package com.mscg.test.docs;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType
@XmlType(name="SetPriceResponseType", namespace="http://test.mscg.com/xsd", propOrder={"message"})
@XmlRootElement(namespace="http://test.mscg.com/xsd")
public class SetPriceResponse {
	private String messageValue;

	public SetPriceResponse(){

	}

	public SetPriceResponse(String message){
		setMessage(message);
	}

	/**
	 * @return the message
	 */
	@XmlElement(namespace="http://test.mscg.com/xsd",  required = true)
	public String getMessage() {
		return messageValue;
	}

	/**
	 * @param messageValue the message to set
	 */
	public void setMessage(String messageValue) {
		this.messageValue = messageValue;
	}
}
