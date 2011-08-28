package com.mscg.appstarter.server.util.application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mscg.appstarter.exception.ApplicationAlreadyRunningException;
import com.mscg.appstarter.exception.ApplicationLaunchException;
import com.mscg.appstarter.exception.ApplicationNotConfiguredException;
import com.mscg.appstarter.exception.ApplicationNotRunningException;
import com.mscg.appstarter.server.util.Constants;
import com.mscg.appstarter.server.util.Settings;

public class ApplicationsHolder {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationsHolder.class);

    private static final byte[] EMPTY_INPUT = new byte[]{};

    private long lastRead;
    private Map<Integer, ApplicationInfo> applicationsList;
    private Map<Integer, ApplicationExecutorThread> runningApplications;

    public ApplicationsHolder() {
        lastRead = 0l;
        runningApplications = new HashMap<Integer, ApplicationExecutorThread>();
        initApplicationsList();

        if(Settings.getConfig().getBoolean(Constants.TERMINATE_APP_ONEXIT)) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    terminateAllApplications();
                }
            });
        }
    }

    protected synchronized void initApplicationsList() {
        applicationsList = new TreeMap<Integer, ApplicationInfo>();
        lastRead = System.currentTimeMillis();

        List<String> ids = Settings.getConfig().getList(Constants.APPLICATION_ID);
        List<String> names = Settings.getConfig().getList(Constants.APPLICATION_NAME);
        List<String> commands = Settings.getConfig().getList(Constants.APPLICATION_COMMAND);

        for(Iterator<String> it1 = ids.iterator(), it2 = names.iterator(), it3 = commands.iterator();
            it1.hasNext() && it2.hasNext() && it3.hasNext();) {

            String idStr = it1.next();
            String name = it2.next();
            String command = it3.next();
            Integer id = Integer.parseInt(idStr);

            ApplicationInfo info = new ApplicationInfo(id, name, command);
            applicationsList.put(id, info);
        }
    }

    public synchronized Map<Integer, ApplicationInfo> getApplicationsList() {
        if(lastRead < Settings.getLastConfigRead())
            initApplicationsList();
        return applicationsList;
    }

    public synchronized boolean isApplicationRunning(int applicationID) {
        return runningApplications.containsKey(applicationID);
    }

    public synchronized ApplicationInfo launchApplication(int applicationID) throws ApplicationNotConfiguredException,
                                                                                    ApplicationAlreadyRunningException,
                                                                                    ApplicationLaunchException {
        if(!applicationsList.containsKey(applicationID))
            throw new ApplicationNotConfiguredException("Application with ID " + applicationID + " not configured");

        if(isApplicationRunning(applicationID))
            throw new ApplicationAlreadyRunningException("Application with ID " + applicationID + " is already running");

        ApplicationExecutorThread applicationThread = new ApplicationExecutorThread(applicationID);
        runningApplications.put(applicationID, applicationThread);
        applicationThread.start();

        // wait for a while, then check if the thread generated an exception
        try {
            Thread.sleep(3000l);
            Exception exception = applicationThread.getException();
            if(exception != null)
                throw new ApplicationLaunchException("Cannot launch the application", exception);
        } catch(InterruptedException e){}

        return applicationsList.get(applicationID);
    }

    public ApplicationInfo terminateApplication(int applicationID) throws ApplicationNotConfiguredException,
                                                                                       ApplicationNotRunningException {
        return terminateApplication(applicationID, false);
    }

    public ApplicationInfo terminateApplication(int applicationID, boolean wait)
            throws ApplicationNotConfiguredException, ApplicationNotRunningException {

        ApplicationExecutorThread applicationThread = null;
        synchronized (this) {
            if(!applicationsList.containsKey(applicationID))
                throw new ApplicationNotConfiguredException("Application with ID " + applicationID + " not configured");

            if(!isApplicationRunning(applicationID))
                throw new ApplicationNotRunningException("Application with ID " + applicationID + " is not running");

            applicationThread = runningApplications.get(applicationID);
            applicationThread.interrupt();
        }
        try {
            if(wait)
                applicationThread.join();
        } catch(InterruptedException e) {
            LOG.warn("Wait was interrupted", e);
        }

        return applicationsList.get(applicationID);
    }

    public synchronized void terminateAllApplications() {
        for(Integer applicationID : runningApplications.keySet()) {
            try {
                terminateApplication(applicationID);
            } catch(Exception e){ /* IGNORED */ }
        }
    }

    protected synchronized void removeApplication(int applicationID) {
        if(applicationsList.containsKey(applicationID))
            runningApplications.remove(applicationID);
    }

    private final class ApplicationExecutorThread extends Thread {

        private Exception exc;
        private Integer exitValue;
        private int applicationID;

        public ApplicationExecutorThread(int applicationID) {
            this.applicationID = applicationID;
        }

        @Override
        public void run() {
            ApplicationInfo applicationInfo = applicationsList.get(applicationID);
            CommandLine commandLine = CommandLine.parse(applicationInfo.getCommandLine());
            DefaultExecutor executor = new DefaultExecutor();
            OutputStream out = new ByteArrayOutputStream();
            OutputStream err = new ByteArrayOutputStream();
            InputStream input = new ByteArrayInputStream(EMPTY_INPUT);
            ExecuteStreamHandler streamHandler = new PumpStreamHandler(out, err, input);
            executor.setStreamHandler(streamHandler);

            try {
                if(LOG.isDebugEnabled())
                    LOG.debug("Executing command line \"" + commandLine.toString() + "\"...");
                setExitValue(executor.execute(commandLine));
                if(LOG.isInfoEnabled())
                    LOG.info("Application " + applicationID + " terminated with exit code " + getExitValue());
            } catch (ExecuteException e) {
                LOG.error("Application terminated with incorrect exit code: " + getExitValue(), e);
                setException(e);
            } catch (Exception e) {
                LOG.error("An error occurred while executing the application", e);
                setException(e);
            } finally {
                removeApplication(applicationID);
            }
        }

        public synchronized Integer getExitValue() {
            return exitValue;
        }

        public synchronized void setExitValue(Integer exitValue) {
            this.exitValue = exitValue;
        }

        public synchronized Exception getException() {
            return exc;
        }

        public synchronized void setException(Exception exc) {
            this.exc = exc;
        }
    }
}
