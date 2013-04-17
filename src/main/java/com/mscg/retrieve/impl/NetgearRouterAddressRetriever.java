package com.mscg.retrieve.impl;

import java.util.regex.Pattern;

import com.mscg.httpinterface.NetgearRouterAddressInterface;


public class NetgearRouterAddressRetriever extends DelegatingAddressRetriever {

    @Override
    protected void initPattern() {
        pattern = Pattern.compile(
            "<tr>\\s*<td.*>\\s*<b>\\s*IP Address\\s*</b>\\s*</td>\\s*" +
        	"<td.*>\\s*(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\s*</td>", Pattern.MULTILINE);
    }

    @Override
    protected int getPatternGroupIndex() {
        return 1;
    }

    @Override
    protected void initIPAddressInterface() throws Exception {
        ipaddrintrf = new NetgearRouterAddressInterface();
    }

}
