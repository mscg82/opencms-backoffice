package com.mscg.virgilio.listener;

import org.apache.http.client.methods.HttpGet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.mscg.net.HttpClientManager;
import com.mscg.net.handler.AsynchResponseHandler;
import com.mscg.virgilio.VirgilioGuidaTvPrograms;
import com.mscg.virgilio.handlers.DownloadProgressHandler;
import com.mscg.virgilio.handlers.ProgramsDetailsHandler;
import com.mscg.virgilio.programs.TVProgram;
import com.mscg.virgilio.util.ContextAndHandlerAware;
import com.mscg.virgilio.util.ProgramLinearLayout;
import com.mscg.virgilio.util.net.ProgramsDetailsResponseHandler;
import com.mscg.virgilio.util.net.VirgilioURLUtil;

public class ProgramSelectionClickListener extends ContextAndHandlerAware implements OnItemClickListener {

    public ProgramSelectionClickListener(VirgilioGuidaTvPrograms context, Handler guiHandler) {
        super(context, guiHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProgramLinearLayout pll = (ProgramLinearLayout) view;
        TVProgram program = pll.getTvProgram();

        if (program.getProgramDetails() == null) {
            String url = VirgilioURLUtil.DETAILS_URL.replace("${programID}", program.getStrId());
            HttpGet get = new HttpGet(url);
            AsynchResponseHandler<String> responseHandler = new ProgramsDetailsResponseHandler(context, guiHandler, program);
            HttpClientManager.executeAsynchMethod(get, responseHandler);
        } else {
            if (guiHandler instanceof ProgramsDetailsHandler)
                ((ProgramsDetailsHandler) guiHandler).setProgram(program);

            Message msg = guiHandler.obtainMessage();
            Bundle b = new Bundle();
            b.putInt(DownloadProgressHandler.TYPE, DownloadProgressHandler.END_PARSE);
            msg.setData(b);
            guiHandler.sendMessage(msg);
        }
    }

}
