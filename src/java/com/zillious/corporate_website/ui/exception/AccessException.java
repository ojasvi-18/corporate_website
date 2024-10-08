package com.zillious.corporate_website.ui.exception;

public class AccessException extends Exception {
    private static final long serialVersionUID = 1L;

    public AccessException(AccessExceptionType typ) {
        this(typ, null);
    }

    public AccessException(AccessExceptionType typ, String msg) {

    }
}
