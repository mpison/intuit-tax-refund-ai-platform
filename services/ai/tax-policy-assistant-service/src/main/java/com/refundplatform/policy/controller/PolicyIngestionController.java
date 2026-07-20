package com.refundplatform.policy.controller;

import com.refundplatform.policy.dto.PolicyIngestionResponse;
import com.refundplatform.policy.service.PolicyIngestionService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/policies")
public class PolicyIngestionController {

    private final PolicyIngestionService service;

    public PolicyIngestionController(
            PolicyIngestionService service) {

        this.service =
                service;
    }

    @PostMapping(
            value = "/ingest",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public PolicyIngestionResponse ingest(
            @RequestPart("policyDocumentId")
            String policyDocumentId,

            @RequestPart("file")
            MultipartFile file) {

        return service.ingest(
                policyDocumentId,
                file
        );
    }
}
