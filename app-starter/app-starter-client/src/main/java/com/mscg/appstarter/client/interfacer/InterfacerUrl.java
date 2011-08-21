package com.mscg.appstarter.client.interfacer;

public enum InterfacerUrl {
    LOGIN_URL("/auth/login"),
    LOGOUT_URL("/auth/logout"),
    PING_URL("/auth/ping");

    private String relativeUrl;

    private InterfacerUrl(String relativeUrl) {
        this.relativeUrl = relativeUrl;
    }

    public String getRelativeUrl() {
        return relativeUrl;
    }

}