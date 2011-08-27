package com.mscg.appstarter.exception;

import com.mscg.appstarter.util.ResponseCode;

public class InvalidRequestException extends Exception {

    private static final long serialVersionUID = 7078341586902212055L;

    protected ResponseCode errorCode;

    public InvalidRequestException(ResponseCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public InvalidRequestException(String message, Throwable cause, ResponseCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public InvalidRequestException(String message, ResponseCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public InvalidRequestException(Throwable cause, ResponseCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ResponseCode getErrorCode() {
        return errorCode;
    }

}
