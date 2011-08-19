package com.mscg.appstarter.server.util.session;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class SessionsHolder {

    private Map<String, SessionInfo> sessions;

    public SessionsHolder() {
        sessions = new LinkedHashMap<String, SessionInfo>();
    }

    public synchronized void storeSession(String sessionID, String username) {
        sessions.put(sessionID, new SessionInfo(System.currentTimeMillis(), username));
    }

    public synchronized String getSessionUser(String sessionID) {
        SessionInfo sessionInfo = sessions.get(sessionID);
        if(sessionInfo == null)
            return null;
        sessionInfo.lastUsage = System.currentTimeMillis();
        return sessionInfo.username;
    }

    public synchronized void removeSession(String sessionID) {
        sessions.remove(sessionID);
    }

    public synchronized int cleanOlderSessions(long timeout) {
        int removed = 0;
        long lastValid = System.currentTimeMillis() - timeout;
        for(Iterator<Map.Entry<String, SessionInfo>> it = sessions.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, SessionInfo> entry = it.next();
            SessionInfo info = entry.getValue();
            if(info.lastUsage < lastValid) {
                it.remove();
                removed++;
            }
        }
        return removed;
    }

    private static class SessionInfo {
        long lastUsage;
        String username;

        public SessionInfo(long lastUsage, String username) {
            this.lastUsage = lastUsage;
            this.username = username;
        }

    }
}
