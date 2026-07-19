package com.refundplatform.irs.service;

import com.refundplatform.irs.dto.IrsRefundRecord;
import com.refundplatform.irs.model.IrsRefundStatus;
import com.refundplatform.irs.repository.IrsRefundRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class IrsRefundStore {

    private final IrsRefundRepository irsRefundRepository;

    public IrsRefundStore(
            IrsRefundRepository irsRefundRepository) {

        this.irsRefundRepository =
                irsRefundRepository;
    }

    public IrsRefundRecord find(
            String externalRefundId) {

        return irsRefundRepository
                .findById(
                        externalRefundId
                )
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "IRS refund record was not found: "
                                                + externalRefundId
                                )
                );
    }

    public List<IrsRefundRecord> findAll() {

        return irsRefundRepository.findAll();
    }

    public IrsRefundRecord create(
            String externalRefundId,
            IrsRefundStatus status,
            LocalDate officialRefundDate) {

        if (
                externalRefundId == null
                || externalRefundId.isBlank()
        ) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "externalRefundId is required"
            );
        }

        if (
                irsRefundRepository.findById(
                        externalRefundId.trim()
                )
                .isPresent()
        ) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "IRS refund record already exists: "
                            + externalRefundId
            );
        }

        return irsRefundRepository.save(
                externalRefundId.trim(),
                status == null
                        ? IrsRefundStatus.FILED
                        : status,
                officialRefundDate
        );
    }

    public IrsRefundRecord update(
            String externalRefundId,
            IrsRefundStatus status,
            LocalDate officialRefundDate) {

        if (status == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "status is required"
            );
        }

        return irsRefundRepository.save(
                externalRefundId,
                status,
                officialRefundDate
        );
    }

    public void delete(
            String externalRefundId) {

        if (
                !irsRefundRepository.deleteById(
                        externalRefundId
                )
        ) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "IRS refund record was not found: "
                            + externalRefundId
            );
        }
    }
}
