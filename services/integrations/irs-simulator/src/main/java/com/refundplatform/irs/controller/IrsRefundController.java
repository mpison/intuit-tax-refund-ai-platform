package com.refundplatform.irs.controller;

import com.refundplatform.irs.dto.CreateIrsRefundRequest;
import com.refundplatform.irs.dto.IrsRefundRecord;
import com.refundplatform.irs.dto.UpdateIrsRefundRequest;
import com.refundplatform.irs.service.IrsRefundStore;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(
            "/demo/irs/refunds/{externalRefundId}"
    )
    public IrsRefundRecord getDemoRefund(
            @PathVariable
            String externalRefundId) {

        return irsRefundStore.find(
                externalRefundId
        );
    }

    @GetMapping(
            "/demo/irs/refunds"
    )
    public List<IrsRefundRecord> getDemoRefunds() {

        return irsRefundStore.findAll();
    }

    @PostMapping(
            "/demo/irs/refunds"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public IrsRefundRecord createRefund(
            @RequestBody
            CreateIrsRefundRequest request) {

        return irsRefundStore.create(
                request.externalRefundId(),
                request.status(),
                request.officialRefundDate()
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

    @DeleteMapping(
            "/demo/irs/refunds/{externalRefundId}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRefund(
            @PathVariable
            String externalRefundId) {

        irsRefundStore.delete(
                externalRefundId
        );
    }
}
