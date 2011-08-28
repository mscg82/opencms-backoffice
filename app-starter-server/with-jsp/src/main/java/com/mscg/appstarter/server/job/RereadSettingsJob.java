package com.mscg.appstarter.server.job;

import java.io.File;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mscg.appstarter.server.util.Constants;
import com.mscg.appstarter.server.util.Settings;

public class RereadSettingsJob extends GenericJob {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        File settingsFile = new File(Constants.SETTINGS_FILE_NAME);
        if(settingsFile.exists()) {
            if(settingsFile.lastModified() > Settings.getLastConfigRead()) {
                // settings in memory are older than the one on file
                LOG.info("Settings file is changed, re-reading configuration");
                Settings.initConfig();
            }
        }
    }

}
