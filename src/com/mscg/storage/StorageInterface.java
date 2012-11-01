package com.mscg.storage;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;

public interface StorageInterface {

    public void storeIP(String service, List<String> IPs) throws HttpException, IOException;

}
