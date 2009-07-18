package com.mscg.test;

import java.util.HashMap;
import java.util.Map;

public class MasterStoreService {
	protected static Map<String, Double> prices;

	static{
		prices = new HashMap<String, Double>();
		prices.put("bread", 10.0d);
		prices.put("milk", 3.5d);
		prices.put("default", 20.34d);
	}
}
