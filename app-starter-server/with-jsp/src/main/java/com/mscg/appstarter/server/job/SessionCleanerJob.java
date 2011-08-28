package com.mscg.appstarter.server.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mscg.appstarter.server.util.session.SessionsHolder;

public class SessionCleanerJob extends GenericJob {

    private SessionsHolder sessionsHolder;

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

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            LOG.debug("Removing unused sessions...");
            int removed = sessionsHolder.cleanOlderSessions();
            LOG.info("Removed " + removed + " older sessions");
        } catch(Exception e) {
            LOG.error("An error occurred while removing older sessions", e);
        }
    }

}
