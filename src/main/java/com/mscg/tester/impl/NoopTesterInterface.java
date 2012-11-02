package com.mscg.tester.impl;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.mscg.tester.TesterInterface;

public class NoopTesterInterface implements TesterInterface {

    public boolean testIfServerIsRunning() throws HttpException, IOException {
        return true;
    }

    public boolean startServer() throws HttpException, IOException {
        return true;
    }

}
