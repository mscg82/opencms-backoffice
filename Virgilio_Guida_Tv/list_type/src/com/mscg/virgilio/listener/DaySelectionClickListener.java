package com.mscg.virgilio.listener;

import java.util.Calendar;

import org.apache.http.client.methods.HttpGet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.mscg.net.HttpClientManager;
import com.mscg.net.handler.AsynchResponseHandler;
import com.mscg.virgilio.GenericActivity;
import com.mscg.virgilio.VirgilioGuidaTvDaySelection;
import com.mscg.virgilio.handlers.DownloadProgressHandler;
import com.mscg.virgilio.programs.Programs;
import com.mscg.virgilio.util.CacheManager;
import com.mscg.virgilio.util.ContextAndHandlerAware;
import com.mscg.virgilio.util.DayLinearLayout;
import com.mscg.virgilio.util.net.ProgramsDownloadResponseHandler;
import com.mscg.virgilio.util.net.VirgilioURLUtil;

public class DaySelectionClickListener extends ContextAndHandlerAware implements OnItemClickListener {

    public DaySelectionClickListener(VirgilioGuidaTvDaySelection context, Handler guiHandler) {
        super(context, guiHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DayLinearLayout dll = (DayLinearLayout) view;

        final Calendar day = dll.getDay();
        String url = VirgilioURLUtil.URLS.get(day.get(Calendar.DAY_OF_WEEK));

        boolean existsLocally = false;
        try {
            existsLocally = CacheManager.getInstance().containsProgramsForDay(day.getTime());
        } catch (Exception e) {
            Log.e(DaySelectionClickListener.class.getCanonicalName(), "Cannot query DB", e);
        }

        if (!existsLocally) {
            HttpGet get = new HttpGet(url);
            AsynchResponseHandler<String> responseHandler = new ProgramsDownloadResponseHandler(
                                                                                                (VirgilioGuidaTvDaySelection) context,
                                                                                                guiHandler);
            HttpClientManager.executeAsynchMethod(get, responseHandler);
        } else {
            ((GenericActivity) context).showDownloadDialog(DownloadProgressHandler.START_LOAD);
            new Thread() {

                @Override
                public void run() {
                    Programs programs = CacheManager.getInstance().getProgramsForDay(day.getTime());
                    Message msg = guiHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt(DownloadProgressHandler.TYPE, DownloadProgressHandler.END_SAVE);
                    b.putSerializable(DownloadProgressHandler.PROGRAMS, programs);
                    msg.setData(b);
                    guiHandler.sendMessage(msg);
                }

            }.start();
        }
    }

}
