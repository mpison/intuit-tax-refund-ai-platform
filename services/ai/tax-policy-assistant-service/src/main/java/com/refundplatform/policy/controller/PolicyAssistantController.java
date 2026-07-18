package com.refundplatform.policy.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.refundplatform.policy.dto.AssistantRequest;
import com.refundplatform.policy.dto.AssistantResponse;
import com.refundplatform.policy.service.PolicyIngestionService;
import com.refundplatform.policy.service.TaxPolicyAssistantService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class PolicyAssistantController {

    private final TaxPolicyAssistantService
            taxPolicyAssistantService;

    private final PolicyIngestionService
            policyIngestionService;

    public PolicyAssistantController(
            TaxPolicyAssistantService taxPolicyAssistantService,
            PolicyIngestionService policyIngestionService) {

        this.taxPolicyAssistantService =
                taxPolicyAssistantService;

        this.policyIngestionService =
                policyIngestionService;
    }

    @PostMapping("/assistant/chat")
    public AssistantResponse chat(
            @Valid
            @RequestBody
            AssistantRequest request) {

        return taxPolicyAssistantService.answer(
                request
        );
    }

    @PostMapping("/admin/policies/ingest-defaults")
    public Map<String, Object> ingestDefaults() {

        int documentCount =
                policyIngestionService
                        .ingestDefaults();

        return Map.of(
                "status",
                "INGESTED",

                "documentCount",
                documentCount
        );
    }
}
