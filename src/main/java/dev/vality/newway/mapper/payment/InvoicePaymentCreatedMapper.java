package dev.vality.newway.mapper.payment;

import dev.vality.damsel.domain.*;
import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.damsel.payment_processing.InvoicePaymentStarted;
import dev.vality.geck.common.util.TBaseUtil;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.mamsel.*;
import dev.vality.newway.domain.enums.*;
import dev.vality.newway.domain.tables.pojos.Payment;
import dev.vality.newway.domain.tables.pojos.PaymentPayerInfo;
import dev.vality.newway.mapper.Mapper;
import dev.vality.newway.model.InvoicingKey;
import dev.vality.newway.model.PartyShop;
import dev.vality.newway.model.PaymentWrapper;
import dev.vality.newway.service.PartyShopService;
import dev.vality.newway.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoicePaymentCreatedMapper implements Mapper<PaymentWrapper> {

    private final PartyShopService partyShopService;

    private Filter filter = new PathConditionFilter(new PathConditionRule(
            "invoice_payment_change.payload.invoice_payment_started",
            new IsNullCondition().not()));

    @Override
    public PaymentWrapper map(InvoiceChange invoiceChange, MachineEvent event, Integer changeId) {
        InvoicePaymentStarted invoicePaymentStarted = invoiceChange
                .getInvoicePaymentChange()
                .getPayload()
                .getInvoicePaymentStarted();

        InvoicePayment invoicePayment = invoicePaymentStarted.getPayment();

        long sequenceId = event.getEventId();
        String invoiceId = event.getSourceId();
        String paymentId = invoicePayment.getId();
        log.info("Start payment created mapping, sequenceId={}, invoiceId={}, paymentId={}", sequenceId, invoiceId,
                paymentId);

        PartyShop partyShop = partyShopService.get(invoiceId);
        if (partyShop == null) {
            log.info("PartyShop not found for invoiceId = {}", invoiceId);
            return null;
        }

        PaymentWrapper paymentWrapper = new PaymentWrapper();
        paymentWrapper.setKey(InvoicingKey.buildKey(invoiceId, paymentId));
        LocalDateTime eventCreatedAt = TypeUtil.stringToLocalDateTime(event.getCreatedAt());
        paymentWrapper.setPayment(getPayment(
                invoicePayment,
                invoiceId,
                paymentId,
                partyShop,
                eventCreatedAt,
                changeId,
                sequenceId
        ));
        paymentWrapper.setPaymentStatusInfo(PaymentStatusInfoUtil.getPaymentStatusInfo(
                invoicePayment.getStatus(),
                invoiceId,
                paymentId,
                eventCreatedAt,
                changeId,
                sequenceId
        ));
        paymentWrapper.setPaymentPayerInfo(getPayerPaymentInfo(
                invoicePayment,
                invoiceId,
                paymentId,
                eventCreatedAt,
                changeId,
                sequenceId
        ));
        if (invoicePaymentStarted.isSetRoute()) {
            paymentWrapper.setPaymentRoute(PaymentRouteUtil.getPaymentRoute(
                    invoicePaymentStarted.getRoute(),
                    invoiceId,
                    paymentId,
                    TypeUtil.stringToLocalDateTime(event.getCreatedAt()),
                    changeId,
                    sequenceId
            ));
        }
        if (invoicePaymentStarted.isSetCashFlow()) {
            paymentWrapper.setCashFlows(CashFlowUtil.convertCashFlows(
                    invoicePaymentStarted.getCashFlow(),
                    null,
                    PaymentChangeType.payment
            ));
            paymentWrapper.setPaymentFee(PaymentFeeUtil.getPaymentFee(
                    invoicePaymentStarted.getCashFlow(),
                    invoiceId,
                    paymentId,
                    eventCreatedAt,
                    changeId,
                    sequenceId
            ));
        }
        log.info("Payment has been mapped, sequenceId={}, invoiceId={}, paymentId={}", sequenceId, invoiceId,
                paymentId);
        return paymentWrapper;
    }

    private Payment getPayment(InvoicePayment invoicePayment,
                               String invoiceId,
                               String paymentId,
                               PartyShop partyShop,
                               LocalDateTime eventCreatedAt,
                               Integer changeId,
                               Long sequenceId) {
        Payment payment = new Payment();
        payment.setEventCreatedAt(eventCreatedAt);
        payment.setPaymentId(paymentId);
        payment.setInvoiceId(invoiceId);
        payment.setCreatedAt(TypeUtil.stringToLocalDateTime(invoicePayment.getCreatedAt()));
        payment.setPartyId(partyShop.getPartyId());
        payment.setShopId(partyShop.getShopId());
        payment.setDomainRevision(invoicePayment.getDomainRevision());
        if (invoicePayment.isSetPartyRevision()) {
            payment.setPartyRevision(invoicePayment.getPartyRevision());
        }
        payment.setAmount(invoicePayment.getCost().getAmount());
        payment.setCurrencyCode(invoicePayment.getCost().getCurrency().getSymbolicCode());
        if (invoicePayment.isSetMakeRecurrent()) {
            payment.setMakeRecurrent(invoicePayment.isMakeRecurrent());
        }
        payment.setExternalId(invoicePayment.getExternalId());
        payment.setPaymentFlowType(TBaseUtil.unionFieldToEnum(invoicePayment.getFlow(), PaymentFlowType.class));
        if (invoicePayment.getFlow().isSetHold()) {
            payment.setPaymentFlowHeldUntil(
                    TypeUtil.stringToLocalDateTime(invoicePayment.getFlow().getHold().getHeldUntil()));
            payment.setPaymentFlowOnHoldExpiration(invoicePayment.getFlow().getHold().getOnHoldExpiration().name());
        }
        payment.setSequenceId(sequenceId);
        payment.setChangeId(changeId);
        return payment;
    }

    private PaymentPayerInfo getPayerPaymentInfo(InvoicePayment invoicePayment,
                                                 String invoiceId,
                                                 String paymentId,
                                                 LocalDateTime eventCreatedAt,
                                                 Integer changeId,
                                                 Long sequenceId) {
        Payer payer = invoicePayment.getPayer();
        PaymentPayerInfo payerInfo = new PaymentPayerInfo();
        payerInfo.setInvoiceId(invoiceId);
        payerInfo.setPaymentId(paymentId);
        payerInfo.setEventCreatedAt(eventCreatedAt);
        payerInfo.setPayerType(TBaseUtil.unionFieldToEnum(payer, PayerType.class));
        if (payer.isSetPaymentResource()) {
            PaymentResourcePayer paymentResource = payer.getPaymentResource();
            fillPaymentTool(payerInfo, paymentResource.getResource().getPaymentTool());
            fillContactInfo(payerInfo, paymentResource.getContactInfo());
            if (paymentResource.getResource().isSetClientInfo()) {
                payerInfo.setIpAddress(paymentResource.getResource().getClientInfo().getIpAddress());
                payerInfo.setFingerprint(paymentResource.getResource().getClientInfo().getFingerprint());
            }
        } else if (payer.isSetCustomer()) {
            CustomerPayer customer = payer.getCustomer();
            payerInfo.setCustomerId(customer.getCustomerId());
            payerInfo.setCustomerBindingId(customer.getCustomerBindingId());
            payerInfo.setCustomerRecPaymentToolId(customer.getRecPaymentToolId());
            fillPaymentTool(payerInfo, customer.getPaymentTool());
            fillContactInfo(payerInfo, customer.getContactInfo());
        } else if (payer.isSetRecurrent()) {
            payerInfo.setRecurrentParentInvoiceId(payer.getRecurrent().getRecurrentParent().getInvoiceId());
            payerInfo.setRecurrentParentPaymentId(payer.getRecurrent().getRecurrentParent().getPaymentId());
            fillPaymentTool(payerInfo, payer.getRecurrent().getPaymentTool());
            fillContactInfo(payerInfo, payer.getRecurrent().getContactInfo());
        }
        payerInfo.setSequenceId(sequenceId);
        payerInfo.setChangeId(changeId);
        return payerInfo;
    }

    private void fillContactInfo(PaymentPayerInfo payerInfo, ContactInfo contactInfo) {
        payerInfo.setPhoneNumber(contactInfo.getPhoneNumber());
        payerInfo.setEmail(contactInfo.getEmail());
    }

    private void fillPaymentTool(PaymentPayerInfo payerInfo, PaymentTool paymentTool) {
        payerInfo.setPaymentToolType(TBaseUtil.unionFieldToEnum(paymentTool, PaymentToolType.class));
        if (paymentTool.isSetBankCard()) {
            BankCard bankCard = paymentTool.getBankCard();
            payerInfo.setBankCardToken(bankCard.getToken());
            payerInfo.setBankCardPaymentSystem(PaymentSystemUtil.getPaymentSystemName(bankCard));
            payerInfo.setBankCardBin(bankCard.getBin());
            payerInfo.setBankCardMaskedPan(bankCard.getLastDigits());
            payerInfo.setBankName(bankCard.getBankName());
            payerInfo.setBankCardCardholderName(bankCard.getCardholderName());
            if (bankCard.isSetIssuerCountry()) {
                payerInfo.setIssuerCountry(bankCard.getIssuerCountry().name());
            }
            payerInfo.setBankCardTokenProvider(TokenProviderUtil.getTokenProviderName(bankCard));
        } else if (paymentTool.isSetPaymentTerminal()) {
            payerInfo.setPaymentTerminalType(
                    TerminalPaymentUtil.getTerminalPaymentProviderName(paymentTool.getPaymentTerminal()));
        } else if (paymentTool.isSetDigitalWallet()) {
            payerInfo.setDigitalWalletId(paymentTool.getDigitalWallet().getId());
            payerInfo.setDigitalWalletProvider(
                    DigitalWalletUtil.getDigitalWalletName(paymentTool.getDigitalWallet()));
        } else if (CryptoCurrencyUtil.isSetCryptoCurrency(paymentTool)) {
            payerInfo.setCryptoCurrencyType(CryptoCurrencyUtil.getCryptoCurrencyName(paymentTool));
        } else if (paymentTool.isSetMobileCommerce()) {
            payerInfo.setMobileOperator(MobileOperatorUtil.getMobileOperatorName(paymentTool.getMobileCommerce()));
            payerInfo.setMobilePhoneCc(paymentTool.getMobileCommerce().getPhone().getCc());
            payerInfo.setMobilePhoneCtn(paymentTool.getMobileCommerce().getPhone().getCtn());
        }
    }

    @Override
    public Filter<InvoiceChange> getFilter() {
        return filter;
    }
}
