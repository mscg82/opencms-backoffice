package com.mscg.appstarter.server.util;

public class Constants {
    public static final String RUNNING_FILE = "./running";
    public static final String SETTINGS_FILE_NAME = "./settings/settings.xml";

    public static final String HOST_NAME_PARAM = "server.connector.host";
    public static final String PORT_PARAM = "server.connector.port";

    public static final String MIN_THREADS = "server.thread-pool.min-threads";
    public static final String MAX_THREADS = "server.thread-pool.max-threads";
    public static final String LOW_THREADS = "server.thread-pool.low-threads";
    public static final String SHRINK_THREADS = "server.thread-pool.shrink";
    public static final String SERVER_TEMP_FOLDER = "server.temp-folder";

    public static final String USER_DATA      = "users.${username}";
    public static final String PLAIN_PASSWORD = "users.${username}.password";
    public static final String ENC_PASSWORD   = "users.${username}.password-enc";

    public static final String SESSIONS_TIMEOUT = "sessions.timeout";
    public static final String TEMP_SESSIONS_TIMEOUT = "sessions.temp-timeout";

    public static final String TERMINATE_APP_ONEXIT = "applications.terminate-on-exit";

    public static final String APPLICATION_ID      = "applications.application.id";
    public static final String APPLICATION_NAME    = "applications.application.name";
    public static final String APPLICATION_COMMAND = "applications.application.commandline";

}