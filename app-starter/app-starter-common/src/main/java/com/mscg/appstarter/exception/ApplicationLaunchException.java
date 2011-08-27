package com.mscg.appstarter.exception;

public class ApplicationLaunchException extends Exception {

    private static final long serialVersionUID = -5773107308500227693L;

    public ApplicationLaunchException() {
        super();
    }

    public ApplicationLaunchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationLaunchException(String message) {
        super(message);
    }

    public ApplicationLaunchException(Throwable cause) {
        super(cause);
    }

}
