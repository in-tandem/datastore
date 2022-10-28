package com.org.somak.datastore.exception;

public class WriteFailureException extends  DatastoreException{

    public WriteFailureException(ExceptionCode code, String msg) {
        super(code, msg);
    }

    public WriteFailureException(ExceptionCode code, String msg, Throwable cause) {
        super(code, msg, cause);
    }
}
