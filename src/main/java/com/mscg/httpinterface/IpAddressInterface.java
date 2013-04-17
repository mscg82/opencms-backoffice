package com.mscg.httpinterface;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

public interface IpAddressInterface {

    public String getRetrievedIpPageContent() throws HttpException, IOException;

}
