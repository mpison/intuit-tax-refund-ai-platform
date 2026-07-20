package com.refundplatform.policy.service;

import com.refundplatform.policy.extractor.PolicyTextExtractor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PolicyTextExtractionService {

    private final List<PolicyTextExtractor> extractors;

    public PolicyTextExtractionService(
            List<PolicyTextExtractor> extractors) {

        this.extractors =
                extractors;
    }

    public String extract(
            MultipartFile file) {

        PolicyTextExtractor extractor =
                extractors.stream()
                        .filter(
                                candidate ->
                                        candidate.supports(
                                                file
                                        )
                        )
                        .findFirst()
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.BAD_REQUEST,
                                                "Unsupported policy file type"
                                        )
                        );

        String text =
                extractor.extract(
                        file
                );

        if (
                text == null
                || text.isBlank()
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No readable text was found in the policy document"
            );
        }

        return text;
    }
}
