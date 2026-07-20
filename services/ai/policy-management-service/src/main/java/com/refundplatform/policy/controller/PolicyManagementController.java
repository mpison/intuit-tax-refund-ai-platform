package com.refundplatform.policy.controller;

import com.refundplatform.policy.dto.PolicyDocumentResponse;
import com.refundplatform.policy.service.PolicyManagementService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/policies")
public class PolicyManagementController {

    private final PolicyManagementService service;

    public PolicyManagementController(
            PolicyManagementService service) {

        this.service =
                service;
    }

    @GetMapping
    public List<PolicyDocumentResponse> list() {

        return service.list();
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public PolicyDocumentResponse upload(
            @RequestPart("file")
            MultipartFile file) {

        return service.upload(
                file
        );
    }

    @PostMapping(
            "/{policyDocumentId}/reindex"
    )
    public PolicyDocumentResponse reindex(
            @PathVariable
            UUID policyDocumentId) {

        return service.reindex(
                policyDocumentId
        );
    }

    @DeleteMapping(
            "/{policyDocumentId}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable
            UUID policyDocumentId) {

        service.delete(
                policyDocumentId
        );
    }
}
