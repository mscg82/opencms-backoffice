package com.mscg.virgilio;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.mscg.virgilio.handlers.CacheManagementHandler;
import com.mscg.virgilio.listener.CacheManagementButtonListener;
import com.mscg.virgilio.util.CacheManager;

public class VirgilioGuidaTvCacheManagement extends GenericActivity {

    private TextView memoryPrograms;
    private TextView memoryChannels;
    private TextView dbElements;

    private Button analyzeDBElems;
    private Button emptyCacheButton;
    private Button emptyOlderDBButton;
    private Button emptyDBButton;

    private Handler guiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cache_manager_layout);

        memoryPrograms = (TextView) findViewById(R.id.programsInMemory);
        memoryChannels = (TextView) findViewById(R.id.channelsInMemory);
        dbElements = (TextView) findViewById(R.id.elementsInDB);

        analyzeDBElems = (Button) findViewById(R.id.analyzeDB);
        emptyCacheButton = (Button) findViewById(R.id.emptyCache);
        emptyOlderDBButton = (Button) findViewById(R.id.emptyOlderDB);
        emptyDBButton = (Button) findViewById(R.id.emptyDB);

        guiHandler = new CacheManagementHandler(this);

        new CacheManagementButtonListener(this, analyzeDBElems, emptyCacheButton, emptyOlderDBButton, emptyDBButton);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateCounts();
    }

    public void updateCounts() {
        CacheManager cache = CacheManager.getInstance();
        memoryPrograms.setText(Integer.toString(cache.getProgramsCacheSize()));
        memoryChannels.setText(Integer.toString(cache.getChannelsCacheSize()));
        dbElements.setText(Long.toString(cache.getProgramsCount()));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        cache.setEnabled(false);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public Handler getGuiHandler() {
        return guiHandler;
    }

}
