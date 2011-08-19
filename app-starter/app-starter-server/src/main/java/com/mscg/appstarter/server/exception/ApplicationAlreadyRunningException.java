package com.mscg.appstarter.server.exception;

public class ApplicationAlreadyRunningException extends ApplicationLaunchException {

    private static final long serialVersionUID = -8516246062113656654L;

    public ApplicationAlreadyRunningException() {
        super();
    }

    public ApplicationAlreadyRunningException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationAlreadyRunningException(String message) {
        super(message);
    }

    public ApplicationAlreadyRunningException(Throwable cause) {
        super(cause);
    }

}
