package com.mscg.appstarter.server.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mscg.appstarter.server.util.SessionsHolder;

public class SessionCleanerJob extends GenericJob {

    private SessionsHolder sessionsHolder;
    private long sessionTimeout;

    /**
     * @return the sessionsHolder
     */
    public SessionsHolder getSessionsHolder() {
        return sessionsHolder;
    }

    /**
     * @param sessionsHolder the sessionsHolder to set
     */
    public void setSessionsHolder(SessionsHolder sessionsHolder) {
        this.sessionsHolder = sessionsHolder;
    }

    /**
     * @return the sessionTimeout
     */
    public long getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * @param sessionTimeout the sessionTimeout to set
     */
    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            LOG.debug("Removing unused sessions...");
            int removed = sessionsHolder.cleanOlderSessions(sessionTimeout);
            LOG.info("Removed " + removed + " sessions older than " + sessionTimeout + " ms");
        } catch(Exception e) {
            LOG.error("An error occurred while removing older sessions", e);
        }
    }

}
