package com.refundplatform.policy.exception;

import java.util.UUID;

public class PolicyDocumentNotFoundException
        extends RuntimeException {

    public PolicyDocumentNotFoundException(
            UUID policyDocumentId) {

        super(
                "Policy document not found: "
                        + policyDocumentId
        );
    }
}
