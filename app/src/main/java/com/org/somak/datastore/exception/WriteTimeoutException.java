package com.org.somak.datastore.exception;

public class WriteTimeoutException extends DatastoreException{

    protected WriteTimeoutException(ExceptionCode code, String msg) {
        super(code, msg);
    }

    protected WriteTimeoutException(ExceptionCode code, String msg, Throwable cause) {
        super(code, msg, cause);
    }
}
