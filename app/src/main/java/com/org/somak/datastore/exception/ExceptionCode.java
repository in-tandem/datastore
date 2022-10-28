package com.org.somak.datastore.exception;

import java.util.HashMap;
import java.util.Map;

public enum ExceptionCode {
    INVALID_REQUEST(400),
    WRITE_TIMEOUT(500),
    CONFIGURATION_EXCEPTION(401),
    INTERNAL_CACHE_NOT_PROVIDED(501),
    INTERNAL_CACHE_COMPARATOR_NOT_PROVIDED(502), WRITE_FAILURE(533);

    private int exceptionCode;

    private static final Map<Integer, ExceptionCode> valueToCode = new HashMap<>(ExceptionCode.values().length);
    static
    {
        for (ExceptionCode code : ExceptionCode.values())
            valueToCode.put(code.exceptionCode, code);
    }
    private ExceptionCode(int exceptionCode){
        this.exceptionCode = exceptionCode;
    }

    public static ExceptionCode getExceptionCode(int fromValue){

        ExceptionCode code = valueToCode.get(fromValue);
        if (code == null)
            throw new RuntimeException(String.format("Unknown error code %d", fromValue));
        return code;
    }

}
