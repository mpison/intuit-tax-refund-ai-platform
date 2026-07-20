package com.refundplatform.policy.util;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.Set;

@Component
public class PolicyFileValidator {

    private static final long MAX_FILE_SIZE =
            20L * 1024L * 1024L;

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of(
                    "pdf",
                    "docx",
                    "txt"
            );

    public void validate(
            MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw badRequest(
                    "A policy file is required"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw badRequest(
                    "Policy file cannot exceed 20 MB"
            );
        }

        String fileName =
                file.getOriginalFilename();

        if (fileName == null || fileName.isBlank()) {
            throw badRequest(
                    "Policy file name is required"
            );
        }

        int extensionIndex =
                fileName.lastIndexOf('.');

        String extension =
                extensionIndex < 0
                        ? ""
                        : fileName
                                .substring(
                                        extensionIndex + 1
                                )
                                .toLowerCase(
                                        Locale.ROOT
                                );

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw badRequest(
                    "Only PDF, DOCX, and TXT files are supported"
            );
        }
    }

    private ResponseStatusException badRequest(
            String message) {

        return new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                message
        );
    }
}
