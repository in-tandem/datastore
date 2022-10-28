package com.org.somak.datastore.exception;

public class ConfigurationException extends  DatastoreException{
    public ConfigurationException(ExceptionCode code, String msg) {
        super(code, msg);
    }

    public ConfigurationException(ExceptionCode code, String msg, Throwable cause) {
        super(code, msg, cause);
    }
}
