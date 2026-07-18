package com.refundplatform.refund.infrastructure.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tax_returns")
public class TaxReturnEntity {
    @Id
    @Column(name = "tax_return_id")
    private UUID taxReturnId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @Column(name = "tax_year", nullable = false)
    private Integer taxYear;
    @Column(name = "filed_at", nullable = false)
    private Instant filedAt;
    @Column(name = "refund_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal refundAmount;

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
}
