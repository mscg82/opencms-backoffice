package com.mscg.virgilio;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mscg.virgilio.adapters.ChannelListItemAdapter;
import com.mscg.virgilio.handlers.DownloadProgressHandler;
import com.mscg.virgilio.listener.ChannelSelectionClickListener;
import com.mscg.virgilio.programs.Channel;
import com.mscg.virgilio.programs.Programs;
import com.mscg.virgilio.util.CacheManager;
import com.mscg.virgilio.util.Util;

public class VirgilioGuidaTvChannelSelection extends GenericActivity implements TextWatcher {

    private TextView channelsListIntro;
    private EditText search;
    private ListView channelsList;
    private ArrayAdapter<Channel> channelsListAdapter;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.channels_selection);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        channelsListIntro = (TextView) findViewById(R.id.channelsListIntro);
        search = (EditText)findViewById(R.id.channelListSearch);
        search.addTextChangedListener(this);
        channelsList = (ListView) findViewById(R.id.channelsList);
        channelsList.setOnItemClickListener(new ChannelSelectionClickListener(this));

        Intent intent = getIntent();
        Date day = null;
        try {
            day = (Date) intent.getSerializableExtra(DownloadProgressHandler.PROGRAMS);
        } catch (Exception e) {
            Log.e(VirgilioGuidaTvChannelSelection.class.getCanonicalName(), "Cannot get programs date from intent", e);
        }

        if (day != null) {
            Programs programs = CacheManager.getInstance().getProgramsForDay(day);

            if (programs != null) {
                day = programs.getDate();

                channelsListAdapter = new ChannelListItemAdapter(this, R.layout.channel_list_layout, programs.getChannels());
                channelsList.setAdapter(channelsListAdapter);

                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEEE");
                SimpleDateFormat dateFormat = new SimpleDateFormat(", dd/MM");
                String intro = getString(R.string.select_channel_intro);
                intro = intro.replace("${dayName}", Util.capitalize(dayFormat.format(day)) + dateFormat.format(day));
                channelsListIntro.setText(intro);
            }
        } else {
            channelsListIntro.setText(R.string.select_channel_fail);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(search.getVisibility() == View.VISIBLE) {
                search.setVisibility(View.GONE);
                search.setText("");
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchChannel.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = super.onOptionsItemSelected(item);
        if (!ret) {
            switch (item.getItemId()) {
            case MENU_SEARCHCHANNEL:
                search.setVisibility(View.VISIBLE);
                search.requestFocus();

                new Thread() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            try {
                                Thread.sleep(300l);
                            } catch (InterruptedException e) {}
                            imm.showSoftInput(search, 0);
                        }
                    }
                }.start();

                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        channelsListAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
