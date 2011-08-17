package com.mscg.appstarter.server.exception;

public class InvalidRequestException extends Exception {

    private static final long serialVersionUID = 7078341586902212055L;

    protected Integer errorCode;

    public InvalidRequestException(Integer errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public InvalidRequestException(String message, Throwable cause, Integer errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public InvalidRequestException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public InvalidRequestException(Throwable cause, Integer errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

}
