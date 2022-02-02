package dev.vality.newway.mapper.payment;

import dev.vality.geck.common.util.TypeUtil;
import dev.vality.newway.domain.tables.pojos.Payment;
import dev.vality.newway.mapper.AbstractInvoicingMapper;
import dev.vality.newway.model.PaymentWrapper;

public abstract class AbstractInvoicingPaymentMapper extends AbstractInvoicingMapper<PaymentWrapper> {

    protected void setInsertProperties(Payment payment, Long sequenceId, Integer changeId, String eventCreatedAt) {
        payment.setId(null);
        payment.setWtime(null);
        payment.setChangeId(changeId);
        payment.setSequenceId(sequenceId);
        payment.setEventCreatedAt(TypeUtil.stringToLocalDateTime(eventCreatedAt));
        payment.setSessionPayloadTransactionBoundTrxExtraJson(null);
        payment.setTrxAdditionalInfoAcsUrl(null);
        payment.setTrxAdditionalInfoPareq(null);
        payment.setTrxAdditionalInfoMd(null);
        payment.setTrxAdditionalInfoTermUrl(null);
        payment.setTrxAdditionalInfoPares(null);
        payment.setTrxAdditionalInfoCavv(null);
        payment.setTrxAdditionalInfoXid(null);
        payment.setTrxAdditionalInfoCavvAlgorithm(null);
        payment.setTrxAdditionalInfoThreeDsVerification(null);
    }

    protected void setUpdateProperties(Payment payment, String eventCreatedAt) {
        payment.setWtime(null);
        payment.setEventCreatedAt(TypeUtil.stringToLocalDateTime(eventCreatedAt));
    }
}
