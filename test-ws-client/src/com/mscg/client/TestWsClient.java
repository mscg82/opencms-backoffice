/**
 *
 */
package com.mscg.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import com.mscg.test.GetPriceDocument;
import com.mscg.test.SetPricesDocument;
import com.mscg.test.xsd.GetPricesResponseType;
import com.mscg.test.xsd.PriceBeanType;
import com.mscg.test.xsd.SetPriceRequestType;
import com.mscg.test.xsd.SetPriceResponseType;
import com.mscg.test.xsd.StoreResponseType;

/**
 * @author Giuseppe Miscione
 *
 */
public class TestWsClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			StoreServiceServiceStub client = new StoreServiceServiceStub();

			GetPriceDocument req = GetPriceDocument.Factory.newInstance();
			req.addNewGetPrice().setProduct("milk");

			StoreResponseType resp = client.getPrice(req).getGetPriceResponse();
			System.out.println("Price for " + resp.getProduct() + " is " + resp.getPrice());

			SetPricesDocument req2 = SetPricesDocument.Factory.newInstance();
			SetPriceRequestType prices = req2.addNewSetPrices();
			PriceBeanType price = prices.addNewPrices();
			price.setProduct("milk");
			price.setPrice(1.0d);
			price = prices.addNewPrices();
			price.setProduct("bread");
			price.setPrice(4.0d);
			price = prices.addNewPrices();
			price.setProduct("water");
			price.setPrice(0.12d);
			SetPriceResponseType resp2 = client.setPrices(req2).getSetPricesResponse();
			System.out.println("Response for set price: " + resp2.getMessage());

			req = GetPriceDocument.Factory.newInstance();
			req.addNewGetPrice().setProduct("milk");
			resp = client.getPrice(req).getGetPriceResponse();
			System.out.println("Price for " + resp.getProduct() + " is " + resp.getPrice());

			req = GetPriceDocument.Factory.newInstance();
			req.addNewGetPrice().setProduct("bread");
			resp = client.getPrice(req).getGetPriceResponse();
			System.out.println("Price for " + resp.getProduct() + " is " + resp.getPrice());

			System.out.println("Retrieving all prices in the store...");
			GetPricesResponseType resp3 = client.getPrices().getGetPricesResponse();
			PriceBeanType allPrices[] = resp3.getPricesArray();
			for(int i = 0, l = allPrices.length; i < l; i++){
				System.out.println("Price for " + allPrices[i].getProduct() + " is " + allPrices[i].getPrice());
			}

		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

}
