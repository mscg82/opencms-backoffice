package com.mscg.net.handler;

import org.apache.http.client.ResponseHandler;

public interface SyncResponseHandler<T> extends ResponseHandler<T> {

    public void startDownload();

}
