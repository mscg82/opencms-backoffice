package com.mscg.appstarter.exception;

import com.mscg.appstarter.util.ResponseCode;

public class InvalidResponseException extends Exception {

    private static final long serialVersionUID = 6987098189821362573L;

    private ResponseCode responseCode;

    public InvalidResponseException(ResponseCode responseCode) {
        super();
        this.responseCode = responseCode;
    }

    public InvalidResponseException(String message, Throwable cause, ResponseCode responseCode) {
        super(message, cause);
        this.responseCode = responseCode;
    }

    public InvalidResponseException(String message, ResponseCode responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public InvalidResponseException(Throwable cause, ResponseCode responseCode) {
        super(cause);
        this.responseCode = responseCode;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }
}
