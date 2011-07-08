package com.mscg.net.handler;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public interface AsynchResponseHandler<T> extends SyncResponseHandler<T> {

    public void handleException(ClientProtocolException e);

    public void handleException(IOException e);

    public void handleException(Exception e);

    public void handleResponseObject(T response) throws Exception;

}
