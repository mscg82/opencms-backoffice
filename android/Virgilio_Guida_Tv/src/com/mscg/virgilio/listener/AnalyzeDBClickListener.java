package com.mscg.virgilio.listener;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.mscg.virgilio.GenericActivity;
import com.mscg.virgilio.VirgilioGuidaTvDbAnalyze;
import com.mscg.virgilio.adapters.DBAnalyzeListItemAdapter.Holder;
import com.mscg.virgilio.handlers.DownloadProgressHandler;
import com.mscg.virgilio.programs.Programs;
import com.mscg.virgilio.util.CacheManager;
import com.mscg.virgilio.util.ContextAndHandlerAware;

public class AnalyzeDBClickListener extends ContextAndHandlerAware implements OnItemClickListener, OnClickListener,
                                                                  OnCheckedChangeListener {

    private Long programID;

    public AnalyzeDBClickListener(Context context, Handler guiHandler) {
        super(context, guiHandler);
    }

    public Long getProgramID() {
        return programID;
    }

    public void setProgramID(Long programID) {
        this.programID = programID;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onClick(view);
    }

    @Override
    public void onClick(View view) {
        final Holder holder = (Holder) view.getTag();

        ((GenericActivity) context).showDownloadDialog(DownloadProgressHandler.START_LOAD);
        new Thread() {

            @Override
            public void run() {
                Programs programs = CacheManager.getInstance().getProgramsForDay(holder.date);
                Message msg = guiHandler.obtainMessage();
                Bundle b = new Bundle();
                b.putInt(DownloadProgressHandler.TYPE, DownloadProgressHandler.END_SAVE);
                b.putSerializable(DownloadProgressHandler.PROGRAMS, programs);
                msg.setData(b);
                guiHandler.sendMessage(msg);
            }

        }.start();
    }

    @Override
    public void onCheckedChanged(CompoundButton checkbox, boolean isChecked) {
        ((VirgilioGuidaTvDbAnalyze) context).setElementChecked(programID, isChecked);
    }

}
