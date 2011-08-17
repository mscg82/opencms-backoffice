package com.mscg.appstarter.server.util;

public class Constants {
    public static final String HOST_NAME_PARAM = "server.connector.host";
    public static final String PORT_PARAM = "server.connector.port";

    public static final String MIN_THREADS = "server.thread-pool.min-threads";
    public static final String MAX_THREADS = "server.thread-pool.max-threads";
    public static final String LOW_THREADS = "server.thread-pool.low-threads";
    public static final String SHRINK_THREADS = "server.thread-pool.shrink";

    public static final String PLAIN_PASSWORD = "users.${username}.password";
    public static final String ENC_PASSWORD = "users.${username}.password-enc";
}