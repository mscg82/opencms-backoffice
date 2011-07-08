package com.mscg.virgilio;

import org.apache.commons.io.IOUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mscg.net.HttpClientManager;
import com.mscg.virgilio.adapters.DayListItemAdapter;
import com.mscg.virgilio.handlers.DownloadProgressHandler;
import com.mscg.virgilio.listener.DaySelectionClickListener;
import com.mscg.virgilio.util.CacheManager;

public class VirgilioGuidaTvDaySelection extends GenericActivity {

    private Handler guiHandler;

    private ListView daysList;
    private ArrayAdapter<Integer> daysListAdapter;
    private static final Integer days[] = {0, 1, 2, 3, 4, 5, 6};

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            if ((intent.getFlags() & Intent.FLAG_ACTIVITY_CLEAR_TOP) != 0 && intent.getBooleanExtra(EXIT_PARAM, false)) {

                finish();
                return;
            }
        }

        CacheManager.init(this);
        HttpClientManager.open();
        guiHandler = new DownloadProgressHandler(this);

        setContentView(R.layout.days_selection);

        daysList = (ListView) findViewById(R.id.daysList);
        daysListAdapter = new DayListItemAdapter(this, R.layout.day_list_layout, days);

        daysList.setAdapter(daysListAdapter);
        daysList.setOnItemClickListener(new DaySelectionClickListener(this, guiHandler));

    }

    @Override
    protected ProgressDialog innerOnCreateDialog(int id, ProgressDialog progressDialog) {
        ProgressDialog tmp = super.innerOnCreateDialog(id, progressDialog);
        if (tmp == null) {
            switch (id) {
            case DownloadProgressHandler.START_DOWNLOAD:
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage(getString(R.string.contacting_server));
                break;
            case DownloadProgressHandler.UPDATE_PROGRESS:
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMax(10000);
                progressDialog.setMessage(getString(R.string.analyzing_data));
                break;
            case DownloadProgressHandler.END_PARSE:
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage(getString(R.string.saving_data));
                break;
            default:
                return null;
            }
            return progressDialog;
        } else
            return tmp;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        HttpClientManager.close();

        IOUtils.closeQuietly(CacheManager.getInstance());
    }

}