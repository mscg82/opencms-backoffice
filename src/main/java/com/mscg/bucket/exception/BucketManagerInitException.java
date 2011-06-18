package com.mscg.bucket.exception;

public class BucketManagerInitException extends Exception {

    private static final long serialVersionUID = 7634760902204879564L;

    public BucketManagerInitException() {
        super();
    }

    public BucketManagerInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public BucketManagerInitException(String message) {
        super(message);
    }

    public BucketManagerInitException(Throwable cause) {
        super(cause);
    }

}
