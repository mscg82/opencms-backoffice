package com.mscg.appstarter.client.interfacer.exception;

import com.mscg.appstarter.util.ResponseCode;

public class InvalidRequestException extends Exception {

    private static final long serialVersionUID = -8597102568930490605L;

    private ResponseCode responseCode;

    public InvalidRequestException(ResponseCode responseCode) {
        super();
        this.responseCode = responseCode;
    }

    public InvalidRequestException(String message, Throwable cause, ResponseCode responseCode) {
        super(message, cause);
        this.responseCode = responseCode;
    }

    public InvalidRequestException(String message, ResponseCode responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public InvalidRequestException(Throwable cause, ResponseCode responseCode) {
        super(cause);
        this.responseCode = responseCode;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

}
