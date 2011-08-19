package com.mscg.appstarter.util;

public enum ResponseCode {
    OK(200),

    ERR_MISSING_LOGIN_DATA(400),
    ERR_UNAUTHORIZED_ACCESS(401),
    ERR_INVALID_USERNAME(402),
    ERR_MISSING_LOGIN_SESSION(403),
    ERR_INVALID_USER_SESSION(404),
    ERR_INVALID_LOGIN_SESSION(405),
    ERR_APPLICATION_NOT_CONFIGURED(410),
    ERR_APPLICATION_ALREADY_RUNNING(420),
    ERR_APPLICATION_NOT_RUNNING(430),
    ERR_APPLICATION_LAUNCH_ERROR(440),

    ERR_APPLICATION_ERROR(500);

    private int status;

    private ResponseCode(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static ResponseCode fromStatus(int status) {
        for(ResponseCode code : values()) {
            if(code.status == status)
                return code;
        }
        throw new IllegalArgumentException("" + status + " is not a valid status code");
    }

}