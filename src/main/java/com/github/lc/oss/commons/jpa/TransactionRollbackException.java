package com.github.lc.oss.commons.jpa;

public class TransactionRollbackException extends RuntimeException {
    private static final long serialVersionUID = -2969406499868046825L;

    public TransactionRollbackException() {
        super();
    }

    public TransactionRollbackException(String message) {
        super(message);
    }

    public TransactionRollbackException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionRollbackException(Throwable cause) {
        super(cause);
    }

    protected TransactionRollbackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
