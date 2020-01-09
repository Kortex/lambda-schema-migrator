package com.ariskourt.lambda.exceptions;

/***
 * An class describing an exception raised in case a require property is missing
 */
public class MissingPropertyException extends RuntimeException {

    public MissingPropertyException(String message) {
        super(message);
    }

}
