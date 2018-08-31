/*
 * This file is generated by jOOQ.
*/
package com.rbkmoney.newway.domain.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ContractAdjustment implements Serializable {

    private static final long serialVersionUID = -21844886;

    private Long          id;
    private Long          cntrctId;
    private String        contractAdjustmentId;
    private LocalDateTime createdAt;
    private LocalDateTime validSince;
    private LocalDateTime validUntil;
    private Integer       termsId;

    public ContractAdjustment() {}

    public ContractAdjustment(ContractAdjustment value) {
        this.id = value.id;
        this.cntrctId = value.cntrctId;
        this.contractAdjustmentId = value.contractAdjustmentId;
        this.createdAt = value.createdAt;
        this.validSince = value.validSince;
        this.validUntil = value.validUntil;
        this.termsId = value.termsId;
    }

    public ContractAdjustment(
        Long          id,
        Long          cntrctId,
        String        contractAdjustmentId,
        LocalDateTime createdAt,
        LocalDateTime validSince,
        LocalDateTime validUntil,
        Integer       termsId
    ) {
        this.id = id;
        this.cntrctId = cntrctId;
        this.contractAdjustmentId = contractAdjustmentId;
        this.createdAt = createdAt;
        this.validSince = validSince;
        this.validUntil = validUntil;
        this.termsId = termsId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCntrctId() {
        return this.cntrctId;
    }

    public void setCntrctId(Long cntrctId) {
        this.cntrctId = cntrctId;
    }

    public String getContractAdjustmentId() {
        return this.contractAdjustmentId;
    }

    public void setContractAdjustmentId(String contractAdjustmentId) {
        this.contractAdjustmentId = contractAdjustmentId;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getValidSince() {
        return this.validSince;
    }

    public void setValidSince(LocalDateTime validSince) {
        this.validSince = validSince;
    }

    public LocalDateTime getValidUntil() {
        return this.validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public Integer getTermsId() {
        return this.termsId;
    }

    public void setTermsId(Integer termsId) {
        this.termsId = termsId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ContractAdjustment other = (ContractAdjustment) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (cntrctId == null) {
            if (other.cntrctId != null)
                return false;
        }
        else if (!cntrctId.equals(other.cntrctId))
            return false;
        if (contractAdjustmentId == null) {
            if (other.contractAdjustmentId != null)
                return false;
        }
        else if (!contractAdjustmentId.equals(other.contractAdjustmentId))
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        }
        else if (!createdAt.equals(other.createdAt))
            return false;
        if (validSince == null) {
            if (other.validSince != null)
                return false;
        }
        else if (!validSince.equals(other.validSince))
            return false;
        if (validUntil == null) {
            if (other.validUntil != null)
                return false;
        }
        else if (!validUntil.equals(other.validUntil))
            return false;
        if (termsId == null) {
            if (other.termsId != null)
                return false;
        }
        else if (!termsId.equals(other.termsId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.cntrctId == null) ? 0 : this.cntrctId.hashCode());
        result = prime * result + ((this.contractAdjustmentId == null) ? 0 : this.contractAdjustmentId.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.validSince == null) ? 0 : this.validSince.hashCode());
        result = prime * result + ((this.validUntil == null) ? 0 : this.validUntil.hashCode());
        result = prime * result + ((this.termsId == null) ? 0 : this.termsId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ContractAdjustment (");

        sb.append(id);
        sb.append(", ").append(cntrctId);
        sb.append(", ").append(contractAdjustmentId);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(validSince);
        sb.append(", ").append(validUntil);
        sb.append(", ").append(termsId);

        sb.append(")");
        return sb.toString();
    }
}