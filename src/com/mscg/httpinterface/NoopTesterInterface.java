package com.mscg.httpinterface;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

public class NoopTesterInterface implements TesterInterface {

    public boolean testIfServerIsRunning() throws HttpException, IOException {
        return true;
    }

    public boolean startServer() throws HttpException, IOException {
        return true;
    }

}
