package com.refundplatform.refund.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tax_returns")
public class TaxReturnEntity {

    @Id
    @Column(name = "tax_return_id")
    private UUID taxReturnId;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "tax_year")
    private Integer taxYear;

    @Column(name = "filed_at")
    private Instant filedAt;

    @Column(
            name = "refund_amount",
            precision = 12,
            scale = 2
    )
    private BigDecimal refundAmount;

    @Column(name = "external_refund_id")
    private String externalRefundId;

    protected TaxReturnEntity() {
    }

    public UUID getTaxReturnId() {
        return taxReturnId;
    }

    public UserEntity getUser() {
        return user;
    }

    public Integer getTaxYear() {
        return taxYear;
    }

    public Instant getFiledAt() {
        return filedAt;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public String getExternalRefundId() {
        return externalRefundId;
    }
}
