package com.refundplatform.policy.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PolicyIngestionService {

    private final VectorStore vectorStore;

    public PolicyIngestionService(
            VectorStore vectorStore) {

        this.vectorStore =
                vectorStore;
    }

    public int ingestDefaults() {

        List<Document> documents =
                List.of(
                        policy(
                                "refund-processing-time",
                                "Standard Refund Processing",
                                """
                                Most electronically filed tax returns are processed
                                faster than paper returns. A refund can remain in
                                processing while identity, income, withholding, or
                                filing information is validated. Processing estimates
                                are not guarantees.
                                """
                        ),
                        policy(
                                "additional-review",
                                "Additional Review Policy",
                                """
                                A return may require additional review when data does
                                not match external records, when identity validation is
                                incomplete, or when credits require verification.
                                Additional review does not necessarily mean the return
                                is incorrect.
                                """
                        ),
                        policy(
                                "action-required",
                                "Action Required Policy",
                                """
                                Action Required means the taxpayer may need to provide
                                information or complete identity verification. The user
                                should review official notices and account messages.
                                The assistant must not invent a notice or claim that a
                                specific document was requested.
                                """
                        ),
                        policy(
                                "refund-sent",
                                "Refund Sent Policy",
                                """
                                After a refund is sent, financial institutions may
                                require additional time to post the deposit. The user
                                should verify the selected payment method and contact
                                the financial institution when appropriate.
                                """
                        )
                );

        vectorStore.add(
                documents
        );

        return documents.size();
    }

    private Document policy(
            String policyId,
            String title,
            String text) {

        return new Document(
                text,
                Map.of(
                        "policyId",
                        policyId,

                        "title",
                        title,

                        "type",
                        "tax-refund-policy"
                )
        );
    }
}
