package com.mscg.virgilio.util.net;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mscg.io.InputStreamDataReadListener;
import com.mscg.io.PositionNotifierInputStream;
import com.mscg.net.handler.AsynchResponseHandler;
import com.mscg.virgilio.R;
import com.mscg.virgilio.handlers.DownloadProgressHandler;
import com.mscg.virgilio.handlers.ProgramsDetailsHandler;
import com.mscg.virgilio.parser.ProgramDetailsXMLParser;
import com.mscg.virgilio.programs.TVProgram;
import com.mscg.virgilio.util.ContextAndHandlerAware;

public class ProgramsDetailsResponseHandler extends ContextAndHandlerAware implements AsynchResponseHandler<String> {

    private String ioErrorMessage;
    private TVProgram program;

    public ProgramsDetailsResponseHandler(Context context, Handler guiHandler, TVProgram program) {
        super(context, guiHandler);
        this.program = program;
        ioErrorMessage = context.getString(R.string.load_failed).trim();
    }

    @Override
    public void handleException(ClientProtocolException e) {
        Log.e(ProgramsDownloadResponseHandler.class.getCanonicalName(), "Cannot connect to URL", e);
        sendError(e.getMessage());
    }

    @Override
    public void handleException(IOException e) {
        Log.e(ProgramsDownloadResponseHandler.class.getCanonicalName(), "Cannot download data from URL", e);
        sendError(ioErrorMessage);
    }

    @Override
    public void handleException(Exception e) {
        Log.e(ProgramsDownloadResponseHandler.class.getCanonicalName(), "Cannot connect to URL", e);
        sendError(e.getMessage());
    }

    @Override
    public void startDownload() {
        Message msg = guiHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putInt(DownloadProgressHandler.TYPE, DownloadProgressHandler.START_DOWNLOAD);
        msg.setData(b);
        guiHandler.sendMessage(msg);
    }

    @Override
    public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        HttpEntity entity = response.getEntity();
        long totalSize = response.getEntity().getContentLength();
        InputStream is = null;
        Message msg = null;
        Bundle b = null;
        try {
            InputStreamDataReadListener listener = new InputStreamDataReadListener() {
                @Override
                public void onStreamEnd() {
                    Message msg = guiHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt(DownloadProgressHandler.TYPE, DownloadProgressHandler.END_DOWNLOAD);
                    msg.setData(b);
                    guiHandler.sendMessage(msg);
                }

                @Override
                public void onDataRead(long actualPosition, long totalSize) {
                    Message msg = guiHandler.obtainMessage();
                    Bundle b = new Bundle();
                    double perc = (actualPosition * 10000.0d) / totalSize;
                    b.putInt(DownloadProgressHandler.TYPE, DownloadProgressHandler.UPDATE_PROGRESS);
                    b.putInt("progress", (int) Math.round(perc));
                    msg.setData(b);
                    guiHandler.sendMessage(msg);
                }
            };

            is = new PositionNotifierInputStream(entity.getContent(), totalSize, listener);

            ProgramDetailsXMLParser parser = new ProgramDetailsXMLParser(is);
            program.setProgramDetails(parser.getProgramDetails());
            if (guiHandler instanceof ProgramsDetailsHandler)
                ((ProgramsDetailsHandler) guiHandler).setProgram(program);

            msg = guiHandler.obtainMessage();
            b = new Bundle();
            b.putInt(DownloadProgressHandler.TYPE, DownloadProgressHandler.END_PARSE);
            msg.setData(b);
            guiHandler.sendMessage(msg);

        } catch (Exception e) {
            Log.e(ProgramsDownloadResponseHandler.class.getCanonicalName(), "An error occurred", e);

            sendError(e.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
        }
        return null;
    }

    private void sendError(String error) {
        Message msg = guiHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putInt(DownloadProgressHandler.TYPE, DownloadProgressHandler.ERROR);
        b.putString(DownloadProgressHandler.MESSAGE, error);
        msg.setData(b);
        guiHandler.sendMessage(msg);
    }

    @Override
    public void handleResponseObject(String response) {

    }

}
