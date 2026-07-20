package com.refundplatform.policy.service;

import com.refundplatform.policy.client.PolicyIngestionClient;
import com.refundplatform.policy.dto.PolicyDocumentResponse;
import com.refundplatform.policy.exception.PolicyDocumentNotFoundException;
import com.refundplatform.policy.repository.PolicyDocumentRepository;
import com.refundplatform.policy.util.ExceptionMessageUtil;
import com.refundplatform.policy.util.PolicyFileValidator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class PolicyManagementService {

    private final PolicyDocumentRepository repository;
    private final PolicyStorageService storageService;
    private final PolicyIngestionClient ingestionClient;
    private final PolicyFileValidator fileValidator;

    public PolicyManagementService(
            PolicyDocumentRepository repository,
            PolicyStorageService storageService,
            PolicyIngestionClient ingestionClient,
            PolicyFileValidator fileValidator) {

        this.repository =
                repository;

        this.storageService =
                storageService;

        this.ingestionClient =
                ingestionClient;

        this.fileValidator =
                fileValidator;
    }

    @Transactional(readOnly = true)
    public List<PolicyDocumentResponse> list() {

        return repository.findAll();
    }

    public PolicyDocumentResponse upload(
            MultipartFile file) {

        fileValidator.validate(
                file
        );

        UUID policyDocumentId =
                UUID.randomUUID();

        PolicyStorageService.StoredPolicyFile storedFile =
                storageService.store(
                        policyDocumentId,
                        file
                );

        repository.insert(
                policyDocumentId,
                storedFile.originalFileName(),
                file.getContentType(),
                file.getSize(),
                storedFile.path().toString()
        );

        runIngestion(
                policyDocumentId,
                storedFile.path()
        );

        return findRequired(
                policyDocumentId
        );
    }

    public PolicyDocumentResponse reindex(
            UUID policyDocumentId) {

        String storagePath =
                repository.findStoragePath(
                        policyDocumentId
                )
                .orElseThrow(
                        () ->
                                new PolicyDocumentNotFoundException(
                                        policyDocumentId
                                )
                );

        runIngestion(
                policyDocumentId,
                Path.of(
                        storagePath
                )
        );

        return findRequired(
                policyDocumentId
        );
    }

    public void delete(
            UUID policyDocumentId) {

        String storagePath =
                repository.findStoragePath(
                        policyDocumentId
                )
                .orElseThrow(
                        () ->
                                new PolicyDocumentNotFoundException(
                                        policyDocumentId
                                )
                );

        repository.deleteById(
                policyDocumentId
        );

        storageService.delete(
                storagePath
        );
    }

    private void runIngestion(
            UUID policyDocumentId,
            Path policyPath) {

        repository.markProcessing(
                policyDocumentId
        );

        try {
            PolicyIngestionClient.PolicyIngestionResult result =
                    ingestionClient.ingest(
                            policyDocumentId,
                            policyPath
                    );

            repository.markReady(
                    policyDocumentId,
                    result.chunkCount(),
                    result.embeddingCount()
            );
        }
        catch (Exception exception) {
            repository.markFailed(
                    policyDocumentId,
                    ExceptionMessageUtil.safeMessage(
                            exception
                    )
            );
        }
    }

    private PolicyDocumentResponse findRequired(
            UUID policyDocumentId) {

        return repository.findById(
                policyDocumentId
        )
        .orElseThrow(
                () ->
                        new PolicyDocumentNotFoundException(
                                policyDocumentId
                        )
        );
    }
}
