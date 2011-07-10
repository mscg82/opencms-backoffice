package com.mscg.virgilio.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mscg.virgilio.handlers.DownloadProgressHandler;

public class ListViewScrollerThread extends Thread {

    public static final String SCROLL_POSITION = ListViewScrollerThread.class.getCanonicalName() + ".SCROLL_POSITION";

    private int targetIndex;
    private int startIndex;
    private Handler guiHandler;

    public ListViewScrollerThread(int targetIndex, int startIndex, Handler guiHandler) {
        super();
        this.targetIndex = targetIndex;
        this.startIndex = startIndex;
        this.guiHandler = guiHandler;
    }

    @Override
    public void run() {
        int increment = (startIndex < targetIndex ? 1 : -1);
        for(int i = startIndex;
            !isInterrupted() && (startIndex < targetIndex ? i <= targetIndex : i >= targetIndex);
            i += increment) {

            Message msg = guiHandler.obtainMessage();
            Bundle b = new Bundle();
            b.putInt(DownloadProgressHandler.TYPE, DownloadProgressHandler.SCROLL_LIST);
            b.putInt(SCROLL_POSITION, i);
            msg.setData(b);
            guiHandler.sendMessage(msg);
            try {
                Thread.sleep(50l);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
