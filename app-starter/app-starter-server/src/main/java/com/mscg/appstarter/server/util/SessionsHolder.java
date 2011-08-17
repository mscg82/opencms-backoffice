package com.mscg.appstarter.server.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SessionsHolder {

    private static Map<String, String> sessions;

    static {
        sessions = Collections.synchronizedMap(new LinkedHashMap<String, String>());
    }

    public static void storeSession(String sessionID, String username) {
        sessions.put(sessionID, username);
    }

    public static String getSessionUser(String sessionID) {
        return sessions.get(sessionID);
    }

    public static void removeSession(String sessionID) {
        sessions.remove(sessionID);
    }
}
