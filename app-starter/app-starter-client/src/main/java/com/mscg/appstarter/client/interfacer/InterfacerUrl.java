package com.mscg.appstarter.client.interfacer;

public enum InterfacerUrl {
    LOGIN_URL("/auth/login"),
    LOGOUT_URL("/auth/logout"),
    PING_URL("/auth/ping"),
    LIST_APP_URL("/applications/list"),
    LAUNCH_APP_URL("/applications/launch/${appID}"),
    CLOSE_APP_URL("/applications/close/${appID}");

    private String relativeUrl;

    private InterfacerUrl(String relativeUrl) {
        this.relativeUrl = relativeUrl;
    }

    public String getRelativeUrl() {
        return relativeUrl;
    }

}