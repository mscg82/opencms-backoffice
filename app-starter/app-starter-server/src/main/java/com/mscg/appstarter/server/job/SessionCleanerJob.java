package com.mscg.appstarter.server.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mscg.appstarter.server.util.Constants;
import com.mscg.appstarter.server.util.SessionsHolder;
import com.mscg.appstarter.server.util.Settings;

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
            long sessionTimeout = Settings.getConfig().getLong(Constants.SESSIONS_TIMEOUT);
            int removed = sessionsHolder.cleanOlderSessions(sessionTimeout);
            LOG.info("Removed " + removed + " sessions older than " + sessionTimeout + " ms");
        } catch(Exception e) {
            LOG.error("An error occurred while removing older sessions", e);
        }
    }

}
