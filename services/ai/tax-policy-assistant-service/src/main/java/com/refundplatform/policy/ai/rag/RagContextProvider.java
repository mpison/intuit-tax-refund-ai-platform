package com.refundplatform.policy.ai.rag;

import com.refundplatform.policy.ai.context.AiExecutionContext;

public interface RagContextProvider {

    String retrieveContext(
            AiExecutionContext executionContext);
}
