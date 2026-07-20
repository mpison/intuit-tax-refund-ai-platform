package com.refundplatform.policy.service;

import com.refundplatform.policy.dto.PolicyIngestionResponse;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PolicyIngestionService {

    private final PolicyTextExtractionService extractionService;
    private final VectorStore vectorStore;
    private final TokenTextSplitter textSplitter;

    public PolicyIngestionService(
            PolicyTextExtractionService extractionService,
            VectorStore vectorStore) {

        this.extractionService =
                extractionService;

        this.vectorStore =
                vectorStore;

        this.textSplitter =
                TokenTextSplitter.builder()
                        .withChunkSize(800)
                        .withMinChunkSizeChars(250)
                        .withMinChunkLengthToEmbed(20)
                        .withMaxNumChunks(5000)
                        .withKeepSeparator(true)
                        .build();
    }

    public PolicyIngestionResponse ingest(
            String policyDocumentId,
            MultipartFile file) {

        validate(
                policyDocumentId,
                file
        );

        String extractedText =
                extractionService.extract(
                        file
                );

        Map<String, Object> metadata =
                new HashMap<>();

        metadata.put(
                "policyDocumentId",
                policyDocumentId
        );

        metadata.put(
                "fileName",
                safeFileName(
                        file
                )
        );

        metadata.put(
                "contentType",
                file.getContentType() == null
                        ? "application/octet-stream"
                        : file.getContentType()
        );

        metadata.put(
                "ingestedAt",
                Instant.now().toString()
        );

        Document sourceDocument =
                new Document(
                        extractedText,
                        metadata
                );

        List<Document> chunks =
                textSplitter.apply(
                        List.of(
                                sourceDocument
                        )
                );

        if (chunks.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No policy chunks were generated"
            );
        }

        vectorStore.add(
                chunks
        );

        return new PolicyIngestionResponse(
                policyDocumentId,
                safeFileName(
                        file
                ),
                chunks.size(),
                chunks.size()
        );
    }

    private void validate(
            String policyDocumentId,
            MultipartFile file) {

        if (
                policyDocumentId == null
                || policyDocumentId.isBlank()
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "policyDocumentId is required"
            );
        }

        if (
                file == null
                || file.isEmpty()
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Policy file is required"
            );
        }
    }

    private String safeFileName(
            MultipartFile file) {

        String fileName =
                file.getOriginalFilename();

        return fileName == null
                || fileName.isBlank()
                ? "policy-document"
                : fileName;
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
