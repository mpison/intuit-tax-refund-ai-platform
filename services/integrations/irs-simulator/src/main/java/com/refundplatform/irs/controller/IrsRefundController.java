package com.refundplatform.irs.controller;

import com.refundplatform.irs.dto.IrsRefundRecord;
import com.refundplatform.irs.dto.UpdateIrsRefundRequest;
import com.refundplatform.irs.service.IrsRefundStore;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class IrsRefundController {

    private final IrsRefundStore irsRefundStore;

    public IrsRefundController(
            IrsRefundStore irsRefundStore) {

        this.irsRefundStore =
                irsRefundStore;
    }

    @GetMapping(
            "/irs/refunds/{externalRefundId}"
    )
    public IrsRefundRecord getRefund(
            @PathVariable
            String externalRefundId) {

        return irsRefundStore.find(
                externalRefundId
        );
    }

    @PostMapping(
            "/demo/irs/refunds/{externalRefundId}/status"
    )
    public IrsRefundRecord updateRefund(
            @PathVariable
            String externalRefundId,
            @RequestBody
            UpdateIrsRefundRequest request) {

        return irsRefundStore.update(
                externalRefundId,
                request.status(),
                request.officialRefundDate()
        );
    }
}
