package com.refundplatform.policy.exception;

public class PolicyStorageException
        extends RuntimeException {

    public PolicyStorageException(
            String message,
            Throwable cause) {

        super(
                message,
                cause
        );
    }
}
