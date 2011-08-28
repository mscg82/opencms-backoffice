package com.mscg.appstarter.server.util.session;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mscg.appstarter.server.util.Constants;
import com.mscg.appstarter.server.util.Settings;

public class SessionsHolder {

    private Map<String, SessionInfo> sessions;
    private Map<String, SessionInfo> tempSessions;

    public SessionsHolder() {
        sessions = new LinkedHashMap<String, SessionInfo>();
        tempSessions = new LinkedHashMap<String, SessionInfo>();
    }

    public synchronized void storeSession(String sessionID, String username) {
        sessions.put(sessionID, new SessionInfo(System.currentTimeMillis(), username));
    }

    public synchronized String getSessionUser(String sessionID) {
        SessionInfo sessionInfo = sessions.get(sessionID);
        if(sessionInfo == null)
            return null;
        long lastValid = System.currentTimeMillis() - Settings.getConfig().getLong(Constants.SESSIONS_TIMEOUT);
        if(sessionInfo.lastUsage < lastValid)
            return null;
        sessionInfo.lastUsage = System.currentTimeMillis();
        return (String)sessionInfo.payload;
    }

    public synchronized void removeSession(String sessionID) {
        sessions.remove(sessionID);
    }

    public synchronized void storeTempSession(String sessionID, String nonce) {
        tempSessions.put(sessionID, new SessionInfo(System.currentTimeMillis(), nonce));
    }

    public synchronized String getTempSessionData(String sessionID) {
        SessionInfo sessionInfo = tempSessions.get(sessionID);
        if(sessionInfo == null)
            return null;
        long lastValid = System.currentTimeMillis() - Settings.getConfig().getLong(Constants.TEMP_SESSIONS_TIMEOUT);
        if(sessionInfo.lastUsage < lastValid)
            return null;
        sessionInfo.lastUsage = System.currentTimeMillis();
        return (String)sessionInfo.payload;
    }

    public synchronized void removeTempSession(String sessionID) {
        tempSessions.remove(sessionID);
    }

    public synchronized int cleanOlderSessions() {
        int removed = 0;
        long lastValid = System.currentTimeMillis() - Settings.getConfig().getLong(Constants.SESSIONS_TIMEOUT);
        for(Iterator<Map.Entry<String, SessionInfo>> it = sessions.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, SessionInfo> entry = it.next();
            SessionInfo info = entry.getValue();
            if(info.lastUsage < lastValid) {
                it.remove();
                removed++;
            }
        }
        lastValid = System.currentTimeMillis() - Settings.getConfig().getLong(Constants.TEMP_SESSIONS_TIMEOUT);
        for(Iterator<Map.Entry<String, SessionInfo>> it = tempSessions.entrySet().iterator(); it.hasNext();) {
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
        Object payload;

        public SessionInfo(long lastUsage, Object payload) {
            this.lastUsage = lastUsage;
            this.payload = payload;
        }

    }
}
