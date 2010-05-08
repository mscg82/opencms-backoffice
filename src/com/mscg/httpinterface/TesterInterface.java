package com.mscg.httpinterface;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

public interface TesterInterface {

	public abstract boolean testIfServerIsRunning() throws HttpException,
			IOException;

	public abstract boolean startServer() throws HttpException, IOException;

}