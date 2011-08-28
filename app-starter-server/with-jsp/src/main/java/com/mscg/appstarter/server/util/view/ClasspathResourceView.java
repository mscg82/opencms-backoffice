package com.mscg.appstarter.server.util.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.view.InternalResourceView;

import com.mscg.appstarter.server.util.Constants;
import com.mscg.appstarter.server.util.Settings;

public class ClasspathResourceView extends InternalResourceView {

    private static final Logger LOG = LoggerFactory.getLogger(ClasspathResourceView.class);

    @Override
    protected RequestDispatcher getRequestDispatcher(HttpServletRequest request, String path) {
        if(!path.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX))
            return super.getRequestDispatcher(request, path);
        else {
            // extract the classpath resource to a temporary file
            RequestDispatcher ret = null;
            InputStream is = null;
            OutputStream os = null;
            try {
                URL resourceURL = ResourceUtils.getURL(path);
                is = resourceURL.openStream();

                String tempFolderName = Settings.getConfig().getString(Constants.SERVER_TEMP_FOLDER);
                File tempFolder = new File(tempFolderName);
                if(tempFolder.exists() && !tempFolder.isDirectory())
                    throw new FileNotFoundException("\"" + tempFolderName + "\" is not a directory");
                if(!tempFolder.exists())
                    tempFolder.mkdirs();

                String fileName = path.substring(ResourceUtils.CLASSPATH_URL_PREFIX.length()).replace('\\', '/');
                if(fileName.startsWith("/"))
                    fileName = fileName.substring(1);
                File tempFile = new File(tempFolder, fileName);
                if(tempFile.exists() && ! tempFile.isFile())
                    throw new FileNotFoundException("\"" + tempFile.getAbsolutePath() + "\" is a directory");
                if(!tempFile.exists()) {
                    tempFile.getParentFile().mkdirs();
                    tempFile.createNewFile();
                    os = new FileOutputStream(tempFile);
                    IOUtils.copy(is, os);
                    os.flush();
                    os.close();
                    os = null;
                }
                String resourcePath = tempFile.getCanonicalPath();
                String tempParentPath = tempFolder.getParentFile().getCanonicalPath();
                resourcePath = resourcePath.substring(tempParentPath.length()).replace('\\', '/');

                ret = super.getRequestDispatcher(request, resourcePath);

            } catch (Exception e) {
                LOG.error("Cannot resolve URL for path \"" + path + "\"", e);
                ret = null;
            } finally {
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(os);
            }
            return ret;
        }
    }

}
