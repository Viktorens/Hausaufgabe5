package com.company.Exceptions;

/**
 * NullException class extends Exception
 * used for the com.company.Repository classes, preventing having null as a parameter in the methods
 */
public class NullException extends Exception {
    public NullException(String s) {
        super(s);
    }
}
