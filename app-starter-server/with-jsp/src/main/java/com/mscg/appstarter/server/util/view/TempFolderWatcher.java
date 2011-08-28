package com.mscg.appstarter.server.util.view;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mscg.appstarter.server.util.Constants;
import com.mscg.appstarter.server.util.Settings;

public class TempFolderWatcher {

    private static final Logger LOG = LoggerFactory.getLogger(TempFolderWatcher.class);

    public void deleteTempFolder() {

        String tempFolderName = Settings.getConfig().getString(Constants.SERVER_TEMP_FOLDER);
        File tempFolder = new File(tempFolderName);
        if(tempFolder.exists()) {
            if(!tempFolder.isDirectory()) {
                LOG.warn("\"" + tempFolderName + "\" is not a folder and won't be deleted.");
                return;
            }
            recursivelyDeleteFolder(tempFolder);
        }

    }

    protected void recursivelyDeleteFolder(File folder) {
        File children[] = folder.listFiles();
        for(File child : children) {
            if(child.isDirectory())
                recursivelyDeleteFolder(child);
            else
                child.delete();
        }
        folder.delete();
    }

}
