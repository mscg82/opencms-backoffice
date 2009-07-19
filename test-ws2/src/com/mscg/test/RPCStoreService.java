package com.mscg.test;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style=SOAPBinding.Style.RPC)
public class RPCStoreService extends MasterStoreService {

	@WebMethod
	public @WebResult(name="result") Double getPrice(@WebParam(name="productName") String product){
		Double price = prices.get(product);
		if(price == null)
			price = prices.get("default");
		return price;
	}
}
