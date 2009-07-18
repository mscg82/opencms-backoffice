package com.mscg.test;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.mscg.test.docs.GetPricesResponse;
import com.mscg.test.docs.PriceBean;
import com.mscg.test.docs.SetPriceRequest;
import com.mscg.test.docs.SetPriceResponse;
import com.mscg.test.docs.StoreRequest;
import com.mscg.test.docs.StoreResponse;

@Stateless
@WebService
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class StoreService {

	private static Map<String, Double> prices;

	static{
		prices = new HashMap<String, Double>();
		prices.put("bread", 10.0d);
		prices.put("milk", 3.5d);
		prices.put("default", 20.34d);
	}

	public StoreService(){

	}

	@WebMethod
	public StoreResponse getPrice(StoreRequest request){
		StoreResponse resp = new StoreResponse();
		resp.setProduct(request.getProduct());
		Double price = prices.get(request.getProduct());
		if(price != null){
			resp.setPrice(price);
		}
		else{
			resp.setPrice(prices.get("default"));
		}
		return resp;
	}

	@WebMethod
	public SetPriceResponse setPrices(SetPriceRequest request){
		SetPriceResponse resp = new SetPriceResponse("OK");
		for(PriceBean price : request.getPrices()){
			prices.put(price.getProduct(), price.getPrice());
		}
		return resp;
	}

	@WebMethod
	public GetPricesResponse getPrices(){
		GetPricesResponse resp = new GetPricesResponse();
		for(Map.Entry<String, Double> entry : prices.entrySet()){
			PriceBean price = new PriceBean(entry.getKey(), entry.getValue());
			resp.addPrice(price);
		}
		return resp;
	}
}
