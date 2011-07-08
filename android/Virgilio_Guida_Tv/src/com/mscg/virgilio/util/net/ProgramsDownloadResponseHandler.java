package com.mscg.virgilio.util.net;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mscg.io.InputStreamDataReadListener;
import com.mscg.io.PositionNotifierInputStream;
import com.mscg.net.handler.AsynchResponseHandler;
import com.mscg.virgilio.R;
import com.mscg.virgilio.VirgilioGuidaTvDaySelection;
import com.mscg.virgilio.handlers.DownloadProgressHandler;
import com.mscg.virgilio.parser.ProgramXMLParser;
import com.mscg.virgilio.programs.Programs;
import com.mscg.virgilio.util.CacheManager;
import com.mscg.virgilio.util.ContextAndHandlerAware;

public class ProgramsDownloadResponseHandler extends ContextAndHandlerAware implements AsynchResponseHandler<String> {

    private String ioErrorMessage;

    public ProgramsDownloadResponseHandler(VirgilioGuidaTvDaySelection context, Handler guiHandler) {
        super(context, guiHandler);
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
        Log.e(ProgramsDownloadResponseHandler.class.getCanonicalName(), "An error occurred", e);
        sendError(e.getMessage());
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

            // ByteArrayOutputStream bos = new ByteArrayOutputStream();
            // IOUtils.copy(is, bos);
            // byte buffer[] = bos.toByteArray();
            // String tmp = new String(buffer);
            // Log.d(ProgramsDownloadResponseHandler.class.getCanonicalName(),
            // tmp);
            // is = new PositionNotifierInputStream(new
            // ByteArrayInputStream(buffer), buffer.length, listener);

            ProgramXMLParser parser = new ProgramXMLParser(is);
            Programs programs = parser.getPrograms();

            msg = guiHandler.obtainMessage();
            b = new Bundle();
            b.putInt(DownloadProgressHandler.TYPE, DownloadProgressHandler.END_PARSE);
            msg.setData(b);
            guiHandler.sendMessage(msg);

            programs = CacheManager.getInstance().savePrograms(programs);

            msg = guiHandler.obtainMessage();
            b = new Bundle();
            b.putInt(DownloadProgressHandler.TYPE, DownloadProgressHandler.END_SAVE);
            b.putSerializable(DownloadProgressHandler.PROGRAMS, programs);
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
    public void startDownload() {
        Message msg = guiHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putInt(DownloadProgressHandler.TYPE, DownloadProgressHandler.START_DOWNLOAD);
        msg.setData(b);
        guiHandler.sendMessage(msg);
    }

    @Override
    public void handleResponseObject(String response) {

    }

}
