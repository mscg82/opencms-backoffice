package com.mscg.virgilio.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mscg.virgilio.handlers.DownloadProgressHandler;

public class ListViewScrollerThread extends Thread {

    public static final String SCROLL_POSITION = ListViewScrollerThread.class.getCanonicalName() + ".SCROLL_POSITION";

    private int targetIndex;
    private Handler guiHandler;

    public ListViewScrollerThread(int targetIndex, Handler guiHandler) {
        super();
        this.targetIndex = targetIndex;
        this.guiHandler = guiHandler;
    }

    @Override
    public void run() {
        for(int i = 0; !isInterrupted() && i <= targetIndex; i++) {
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
