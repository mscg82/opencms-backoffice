package com.mscg.virgilio;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mscg.virgilio.handlers.DownloadProgressHandler;

public class GenericActivity extends Activity {

    public static final String EXIT_PARAM = GenericActivity.class.getCanonicalName() + ".exit";

    public static final int MENU_GROUP = 0;
    public static final int MENU_CACHE = Menu.FIRST;
    public static final int MENU_DELETE = Menu.FIRST + 1;
    public static final int MENU_SEARCHCHANNEL = Menu.FIRST + 2;
    public static final int MENU_EXIT = Menu.FIRST + 3;

    protected MenuItem cache;
    protected MenuItem delete;
    protected MenuItem searchChannel;
    protected MenuItem exit;

    protected ProgressDialog progressDialog;
    protected Map<Integer, ProgressDialog> progressDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialogs = new HashMap<Integer, ProgressDialog>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        cache = menu.add(MENU_GROUP, MENU_CACHE, Menu.NONE, R.string.cache_settings);
        cache.setIcon(android.R.drawable.ic_menu_save);
        cache.setShortcut('0', 'c');

        delete = menu.add(MENU_GROUP, MENU_DELETE, Menu.NONE, R.string.delete_items);
        delete.setIcon(android.R.drawable.ic_menu_delete);
        delete.setShortcut('1', 'd');

        searchChannel = menu.add(MENU_GROUP, MENU_SEARCHCHANNEL, Menu.NONE, R.string.search_channels);
        searchChannel.setIcon(android.R.drawable.ic_menu_search);
        searchChannel.setShortcut('2', 's');

        exit = menu.add(MENU_GROUP, MENU_EXIT, Menu.NONE, R.string.exit);
        exit.setIcon(android.R.drawable.ic_lock_power_off);
        exit.setShortcut('3', 'e');

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        cache.setVisible(true);
        cache.setEnabled(true);

        delete.setVisible(false);
        searchChannel.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
        case MENU_CACHE:
            Intent cacheIntent = new Intent(this, VirgilioGuidaTvCacheManagement.class);
            startActivity(cacheIntent);
            return true;
        case MENU_EXIT:
            Intent closeIntent = getHomeIntent(this);
            closeIntent.putExtra(EXIT_PARAM, true);
            startActivity(closeIntent);
            return true;
        }
        return false;
    }

    protected ProgressDialog innerOnCreateDialog(int id, ProgressDialog progressDialog) {
        switch (id) {
        case DownloadProgressHandler.START_LOAD:
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getString(R.string.loading_data));
            break;
        default:
            return null;
        }
        return progressDialog;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog = innerOnCreateDialog(id, progressDialog);
        if (progressDialog != null)
            progressDialogs.put(id, progressDialog);
        return progressDialog;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public static Intent getHomeIntent(Context context) {
        Intent homeIntent = new Intent(context, VirgilioGuidaTvDaySelection.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return homeIntent;
    }

    public void showDownloadDialog(int id) {
        // showDialog(id);
        onCreateDialog(id);
        progressDialog = progressDialogs.get(id);
        progressDialog.show();
    }

}
