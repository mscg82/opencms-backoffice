package com.mscg.virgilio;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mscg.virgilio.adapters.ProgramsListItemAdapter;
import com.mscg.virgilio.handlers.ProgramsDetailsHandler;
import com.mscg.virgilio.listener.ProgramSelectionClickListener;
import com.mscg.virgilio.programs.Channel;
import com.mscg.virgilio.programs.TVProgram;
import com.mscg.virgilio.util.CacheManager;
import com.mscg.virgilio.util.ListViewScrollerThread;

public class VirgilioGuidaTvPrograms extends GenericActivity implements OnTouchListener, OnKeyListener {

    public static final String CHANNEL = VirgilioGuidaTvPrograms.class.getCanonicalName() + ".channel";
    public static final String PROGRAMS = VirgilioGuidaTvPrograms.class.getCanonicalName() + ".programs";

    private ArrayAdapter<TVProgram> programsAdapter;
    private ListView programsListView;

    private Handler guiHandler;
    private ListViewScrollerThread listViewScrollerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.program_selection);

        programsListView = (ListView) findViewById(R.id.programsList);
        Intent intent = getIntent();

        if (intent != null) {
            Channel channel = null;
            try {
                long programsID = intent.getLongExtra(PROGRAMS, -1);
                long channelID = intent.getLongExtra(CHANNEL, -1);
                if (programsID >= 0 && channelID >= 0) {
                    channel = CacheManager.getInstance().getChannel(programsID, channelID);
                    if (channel == null) {
                        throw new NullPointerException("Cannot find channel info");
                    }
                } else
                    throw new Exception("Invalid intent paramenters");
            } catch (Exception e) {
                Log.e(VirgilioGuidaTvPrograms.class.getCanonicalName(), "Cannot get channel from intent", e);
            }

            if (channel != null) {
                String title = getString(R.string.programs_list_title);
                title = title.replace("${channelName}", channel.getName());
                setTitle(title);

                guiHandler = new ProgramsDetailsHandler(this);

                programsAdapter = new ProgramsListItemAdapter(this, R.layout.program_list_layout, channel.getTVPrograms());
                programsListView.setAdapter(programsAdapter);
                programsListView.setOnItemClickListener(new ProgramSelectionClickListener(this, guiHandler));

                // auto select the program actually playing on TV
                int index = 0;
                Date now = new Date();
                boolean found = false;
                for(TVProgram program : channel.getTVPrograms()) {
                    if(now.compareTo(program.getStartTime()) >= 0 && //now >= program.getStartTime()
                       now.compareTo(program.getEndTime()) <= 0) {   //now <= program.getEndTime()
                        program.setPlaying(true);
                        found = true;
                    }
                    else {
                        program.setPlaying(false);
                        index++;
                    }
                }
                if(found) {
                    listViewScrollerThread = new ListViewScrollerThread(index, guiHandler);
                    programsListView.setOnTouchListener(this);
                    programsListView.setOnKeyListener(this);
                    listViewScrollerThread.start();
                }
                else {
                    listViewScrollerThread = null;
                }
            }
        }
    }

    public ListView getProgramsListView() {
        return programsListView;
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
    public boolean onTouch(View v, MotionEvent event) {
        return interruptScroll();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return interruptScroll();
    }

    private boolean interruptScroll() {
        if(listViewScrollerThread != null && listViewScrollerThread.isAlive()) {
            listViewScrollerThread.interrupt();
            listViewScrollerThread = null;
            return true;
        }
        return false;
    }

}
