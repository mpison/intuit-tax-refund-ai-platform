package com.refundplatform.policy.service;

import com.refundplatform.policy.exception.PolicyStorageException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class PolicyStorageService {

    private final Path storageDirectory;

    public PolicyStorageService(
            @Value("${policy.storage.directory}")
            String storageDirectory) {

        this.storageDirectory =
                Path.of(storageDirectory)
                        .toAbsolutePath()
                        .normalize();
    }

    public StoredPolicyFile store(
            UUID policyDocumentId,
            MultipartFile file) {

        String originalName =
                Path.of(
                        file.getOriginalFilename()
                )
                .getFileName()
                .toString();

        Path targetPath =
                storageDirectory.resolve(
                        policyDocumentId
                                + "-"
                                + originalName
                );

        try {
            Files.createDirectories(
                    storageDirectory
            );

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }
        catch (Exception exception) {
            throw new PolicyStorageException(
                    "Unable to store policy document",
                    exception
            );
        }

        return new StoredPolicyFile(
                originalName,
                targetPath
        );
    }

    public void delete(
            String storagePath) {

        try {
            Files.deleteIfExists(
                    Path.of(storagePath)
            );
        }
        catch (Exception exception) {
            throw new PolicyStorageException(
                    "Unable to delete stored policy document",
                    exception
            );
        }
    }

    public record StoredPolicyFile(
            String originalFileName,
            Path path
    ) {
    }
}
