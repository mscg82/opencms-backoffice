package com.mscg.virgilio;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

import com.mscg.virgilio.adapters.DBAnalyzeListItemAdapter;
import com.mscg.virgilio.database.ProgramsDB;
import com.mscg.virgilio.handlers.DownloadProgressHandler;
import com.mscg.virgilio.listener.AnalyzeDBClickListener;
import com.mscg.virgilio.util.CacheManager;

public class VirgilioGuidaTvDbAnalyze extends GenericActivity implements OnCheckedChangeListener {

    private Handler guiHandler;

    private CheckBox selectAll;
    private ListView programsList;

    private Map<Long, Boolean> checkedElems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.db_analyze_layout);

        guiHandler = new DownloadProgressHandler(this);

        selectAll = (CheckBox) findViewById(R.id.analyze_db_select_all);
        programsList = (ListView) findViewById(R.id.analyze_db_list);

        checkedElems = new HashMap<Long, Boolean>();
        Cursor cur = CacheManager.getInstance().getPrograms();
        startManagingCursor(cur);

        while (cur.moveToNext()) {
            long id = cur.getLong(ProgramsDB.PROGRAMS_CONSTS.ID_COL_INDEX);
            checkedElems.put(id, Boolean.FALSE);
        }

        cur.requery();

        selectAll.setOnCheckedChangeListener(this);
        programsList.setAdapter(new DBAnalyzeListItemAdapter(this, R.layout.db_analyze_element_layout, cur, false));
        programsList.setOnItemClickListener(new AnalyzeDBClickListener(this, guiHandler));
    }

    @Override
    protected ProgressDialog innerOnCreateDialog(int id, ProgressDialog progressDialog) {
        ProgressDialog tmp = super.innerOnCreateDialog(id, progressDialog);
        if (tmp == null) {
            switch (id) {
            case DownloadProgressHandler.START_DELETE:
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage(getString(R.string.db_analyze_start_delete));
                break;
            default:
                return null;
            }
            return progressDialog;
        } else
            return tmp;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public Handler getGuiHandler() {
        return guiHandler;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        boolean selected = false;

        for (Boolean checked : checkedElems.values()) {
            if (checked) {
                selected = checked;
                break;
            }
        }

        cache.setVisible(false);
        delete.setVisible(true);
        delete.setEnabled(selected);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = super.onOptionsItemSelected(item);
        if (!ret) {
            switch (item.getItemId()) {
            case MENU_DELETE:
                new DeleteItemsThread().start();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        for (Long id : checkedElems.keySet()) {
            checkedElems.put(id, isChecked);
        }
        for (int i = 0, l = programsList.getChildCount(); i < l; i++) {
            CheckBox checkbox = (CheckBox) programsList.getChildAt(i).findViewById(R.id.analyze_db_elem_select);
            checkbox.setChecked(isChecked);
        }
    }

    public void setElementChecked(Long id, Boolean checked) {
        checkedElems.put(id, checked);
    }

    public boolean isElementChecked(Long id) {
        Boolean ret = checkedElems.get(id);
        return (ret != null && ret);
    }

    private class DeleteItemsThread extends Thread {
        @Override
        public void run() {
            Message msg = guiHandler.obtainMessage();
            Bundle b = new Bundle();
            b.putInt(DownloadProgressHandler.TYPE, DownloadProgressHandler.START_DELETE);
            msg.setData(b);
            guiHandler.sendMessage(msg);

            int index = 1;
            int total = 0;
            CacheManager cacheManager = CacheManager.getInstance();
            for (Map.Entry<Long, Boolean> entry : checkedElems.entrySet()) {
                if (entry.getValue()) {
                    total++;
                }
            }
            for (Map.Entry<Long, Boolean> entry : checkedElems.entrySet()) {
                if (entry.getValue()) {
                    // the element must be deleted from the DB
                    msg = guiHandler.obtainMessage();
                    b = new Bundle();
                    b.putInt(DownloadProgressHandler.TYPE, DownloadProgressHandler.DELETE_PROGRESS);
                    msg.setData(b);
                    msg.arg1 = index++;
                    msg.arg2 = total;
                    guiHandler.sendMessage(msg);

                    cacheManager.removeProgramById(entry.getKey());
                }
            }

            msg = guiHandler.obtainMessage();
            b = new Bundle();
            b.putInt(DownloadProgressHandler.TYPE, DownloadProgressHandler.END_DELETE);
            msg.setData(b);
            guiHandler.sendMessage(msg);
        }
    }

}
