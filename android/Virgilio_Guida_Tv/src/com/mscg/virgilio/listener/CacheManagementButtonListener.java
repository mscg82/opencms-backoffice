package com.mscg.virgilio.listener;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mscg.virgilio.R;
import com.mscg.virgilio.VirgilioGuidaTvCacheManagement;
import com.mscg.virgilio.VirgilioGuidaTvDbAnalyze;
import com.mscg.virgilio.handlers.CacheManagementHandler;
import com.mscg.virgilio.util.CacheManager;

public class CacheManagementButtonListener implements OnClickListener {

    private VirgilioGuidaTvCacheManagement context;
    private Button analyzeDBElems;
    private Button emptyCacheButton;
    private Button emptyOlderDBButton;
    private Button emptyDBButton;

    public CacheManagementButtonListener(VirgilioGuidaTvCacheManagement context, Button analyzeDBElems, Button emptyCacheButton,
                                         Button emptyOlderDBButton, Button emptyDBButton) {

        super();
        this.context = context;
        this.analyzeDBElems = analyzeDBElems;
        this.emptyCacheButton = emptyCacheButton;
        this.emptyOlderDBButton = emptyOlderDBButton;
        this.emptyDBButton = emptyDBButton;

        this.analyzeDBElems.setOnClickListener(this);
        this.emptyCacheButton.setOnClickListener(this);
        this.emptyOlderDBButton.setOnClickListener(this);
        this.emptyDBButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final CacheManager cache = CacheManager.getInstance();
        if (v == this.analyzeDBElems) {
            long dbProgramsCount = CacheManager.getInstance().getProgramsCount();
            if (dbProgramsCount != 0) {
                Intent intent = new Intent(context, VirgilioGuidaTvDbAnalyze.class);
                context.startActivity(intent);
            } else {
                final AlertDialog.Builder ad = new AlertDialog.Builder(context);
                ad.setTitle(context.getString(R.string.db_analyze_empty_title));
                ad.setMessage(context.getString(R.string.db_analyze_empty_text));
                ad.setCancelable(true);
                ad.setNeutralButton(R.string.close, null);
                ad.show();
            }
        } else if (v == emptyCacheButton) {
            cache.clearChannelsCache();
            cache.clearProgramsCache();
            context.updateCounts();
        } else if (v == emptyOlderDBButton) {
            new Thread() {

                @Override
                public void run() {
                    Message m = null;
                    Bundle b = null;
                    try {
                        m = context.getGuiHandler().obtainMessage();
                        b = m.getData();
                        b.putInt(CacheManagementHandler.TYPE, CacheManagementHandler.REMOVE_START);
                        context.getGuiHandler().sendMessage(m);

                        cache.removeOlderPrograms(7);

                        m = context.getGuiHandler().obtainMessage();
                        b = m.getData();
                        b.putInt(CacheManagementHandler.TYPE, CacheManagementHandler.UPDATE_COUNTS);
                        context.getGuiHandler().sendMessage(m);
                    } catch (Exception e) {
                        Log.e(CacheManagementButtonListener.class.getCanonicalName(), "Cannot delete elements", e);
                    } finally {
                        m = context.getGuiHandler().obtainMessage();
                        b = m.getData();
                        b.putInt(CacheManagementHandler.TYPE, CacheManagementHandler.REMOVE_END);
                        context.getGuiHandler().sendMessage(m);
                    }
                }

            }.start();
        } else if (v == emptyDBButton) {
            new Thread() {

                @Override
                public void run() {
                    Message m = null;
                    Bundle b = null;
                    try {
                        m = context.getGuiHandler().obtainMessage();
                        b = m.getData();
                        b.putInt(CacheManagementHandler.TYPE, CacheManagementHandler.REMOVE_START);
                        context.getGuiHandler().sendMessage(m);

                        cache.removeAllPrograms();

                        m = context.getGuiHandler().obtainMessage();
                        b = m.getData();
                        b.putInt(CacheManagementHandler.TYPE, CacheManagementHandler.UPDATE_COUNTS);
                        context.getGuiHandler().sendMessage(m);
                    } catch (Exception e) {
                        Log.e(CacheManagementButtonListener.class.getCanonicalName(), "Cannot delete elements", e);
                    } finally {
                        m = context.getGuiHandler().obtainMessage();
                        b = m.getData();
                        b.putInt(CacheManagementHandler.TYPE, CacheManagementHandler.REMOVE_END);
                        b.putInt(CacheManagementHandler.OPERATION, CacheManagementHandler.GOTO_HOME);
                        context.getGuiHandler().sendMessage(m);
                    }
                }

            }.start();
        }
    }

}
