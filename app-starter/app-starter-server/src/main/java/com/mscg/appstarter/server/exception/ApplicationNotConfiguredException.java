package com.mscg.appstarter.server.exception;

public class ApplicationNotConfiguredException extends ApplicationLaunchException {

    private static final long serialVersionUID = 7272162733079914523L;

    public ApplicationNotConfiguredException() {
        super();
    }

    public ApplicationNotConfiguredException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationNotConfiguredException(String message) {
        super(message);
    }

    public ApplicationNotConfiguredException(Throwable cause) {
        super(cause);
    }

}
