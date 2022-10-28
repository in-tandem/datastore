package com.org.somak.datastore.exception;

public abstract class DatastoreException extends RuntimeException{

    private final ExceptionCode code;

    protected DatastoreException(ExceptionCode code, String msg)
    {
        super(msg);
        this.code = code;
    }

    protected DatastoreException(ExceptionCode code, String msg, Throwable cause)
    {
        super(msg, cause);
        this.code = code;
    }

    public ExceptionCode code()
    {
        return code;
    }
}
