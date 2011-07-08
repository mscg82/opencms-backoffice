package com.mscg.net;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import android.net.http.AndroidHttpClient;

import com.mscg.net.handler.AsynchResponseHandler;
import com.mscg.net.handler.SyncResponseHandler;

public class HttpClientManager {

    private static HttpClient httpClient;

    public static class HEADER_CONSTANTS {
        // Acting like Chrome
        public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.215 Safari/534.10";
        public static final Map<String, String> BROWSER_HEADERS = new LinkedHashMap<String, String>();

        static {
            BROWSER_HEADERS.put("Accept",
                                "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
            BROWSER_HEADERS.put("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
        }
    }

    public static void open() {
        if (httpClient == null) {
            httpClient = AndroidHttpClient.newInstance(HEADER_CONSTANTS.USER_AGENT);
        }
    }

    public static void close() {
        if (httpClient instanceof AndroidHttpClient) {
            ((AndroidHttpClient) httpClient).close();
        }
        httpClient = null;
    }

    public static Thread executeAsynchMethod(HttpUriRequest request, AsynchResponseHandler<?> handler) {
        return executeAsynchMethod(request, null, handler);
    }

    public static Thread executeAsynchMethod(HttpUriRequest request, Map<String, String> headers, AsynchResponseHandler<?> handler) {
        Thread executor = new Thread(new AsynchMethodExecutor(request, headers, handler));
        executor.start();
        return executor;
    }

    public static Object executeSynchMethod(HttpUriRequest request, SyncResponseHandler<?> handler)
                                                                                                   throws ClientProtocolException,
                                                                                                   IOException {
        return executeSynchMethod(request, null, handler);
    }

    public static Object executeSynchMethod(HttpUriRequest request, Map<String, String> headers, SyncResponseHandler<?> handler)
                                                                                                                                throws ClientProtocolException,
                                                                                                                                IOException {
        Map<String, String> totalHeaders = new LinkedHashMap<String, String>();
        totalHeaders.putAll(HEADER_CONSTANTS.BROWSER_HEADERS);
        if (headers != null)
            totalHeaders.putAll(headers);

        for (Map.Entry<String, String> entry : totalHeaders.entrySet()) {
            request.addHeader(entry.getKey(), entry.getValue());
        }
        handler.startDownload();
        return httpClient.execute(request, handler);
    }

    private static class AsynchMethodExecutor<T> implements Runnable {
        private HttpUriRequest request;
        private Map<String, String> headers;
        private AsynchResponseHandler<T> handler;

        public AsynchMethodExecutor(HttpUriRequest request, Map<String, String> headers, AsynchResponseHandler<T> handler) {
            super();
            this.request = request;
            this.headers = headers;
            this.handler = handler;
        }

        @Override
        public void run() {
            try {
                T response = (T) executeSynchMethod(request, headers, handler);
                handler.handleResponseObject(response);
            } catch (ClientProtocolException e) {
                handler.handleException(e);
            } catch (IOException e) {
                handler.handleException(e);
            } catch (Exception e) {
                handler.handleException(e);
            }
        }

    }
}
