package com.mscg.appstarter.server.exception;

public class ApplicationNotRunningException extends ApplicationLaunchException {

    private static final long serialVersionUID = -5276845338240013147L;

    public ApplicationNotRunningException() {
        super();
    }

    public ApplicationNotRunningException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationNotRunningException(String message) {
        super(message);
    }

    public ApplicationNotRunningException(Throwable cause) {
        super(cause);
    }

}
