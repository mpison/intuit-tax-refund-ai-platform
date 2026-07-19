package com.refundplatform.refund.controller;

import com.refundplatform.refund.dto.CreateFiledReturnRequest;
import com.refundplatform.refund.dto.CreateFiledReturnResponse;
import com.refundplatform.refund.service.CreateFiledReturnService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tax-returns")
public class TaxReturnController {

    private final CreateFiledReturnService createFiledReturnService;

    public TaxReturnController(
            CreateFiledReturnService createFiledReturnService) {
        this.createFiledReturnService = createFiledReturnService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateFiledReturnResponse create(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateFiledReturnRequest request) {
        return createFiledReturnService.create(jwt.getSubject(), request);
    }
}
