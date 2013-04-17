package com.mscg.retrieve.impl;

import java.util.regex.Pattern;

import com.mscg.httpinterface.MyIpAddressInterface;

public class MyIpAddressRetriever extends DelegatingAddressRetriever {

    @Override
    protected void initPattern() {
        pattern = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})");
    }

    @Override
    protected int getPatternGroupIndex() {
        return 1;
    }

    @Override
    protected void initIPAddressInterface() throws Exception {
        ipaddrintrf = new MyIpAddressInterface();
    }

}
