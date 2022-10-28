package com.org.somak.datastore.exception;

import java.util.Optional;

public class InvalidRequestException extends  DatastoreException{

    private static String ERROR_MESSAGE = "Incorrect key/value pair provided to data store engine";

    public InvalidRequestException(final ExceptionCode code, final String msg) {

        super(Optional.ofNullable(code).orElseGet(()->ExceptionCode.INVALID_REQUEST),
                Optional.ofNullable(msg).orElseGet(()->InvalidRequestException.ERROR_MESSAGE));
    }

    public InvalidRequestException(final ExceptionCode code, final String msg, Throwable cause) {

        super(Optional.ofNullable(code).orElseGet(()->ExceptionCode.INVALID_REQUEST),
                Optional.ofNullable(msg).orElseGet(()->InvalidRequestException.ERROR_MESSAGE),
                cause);
    }
}
