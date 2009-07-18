package com.mscg.test;

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
public class StoreService extends MasterStoreService{

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
