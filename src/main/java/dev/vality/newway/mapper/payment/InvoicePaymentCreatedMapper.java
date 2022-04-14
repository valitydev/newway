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
import dev.vality.newway.domain.tables.pojos.CashFlow;
import dev.vality.newway.domain.tables.pojos.Payment;
import dev.vality.newway.handler.event.stock.LocalStorage;
import dev.vality.newway.model.InvoiceWrapper;
import dev.vality.newway.model.InvoicingKey;
import dev.vality.newway.model.PartyShop;
import dev.vality.newway.model.PaymentWrapper;
import dev.vality.newway.service.InvoiceWrapperService;
import dev.vality.newway.util.CashFlowType;
import dev.vality.newway.util.CashFlowUtil;
import dev.vality.newway.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoicePaymentCreatedMapper extends AbstractInvoicingPaymentMapper {

    private final InvoiceWrapperService invoiceWrapperService;

    private Filter filter = new PathConditionFilter(new PathConditionRule(
            "invoice_payment_change.payload.invoice_payment_started",
            new IsNullCondition().not()));

    @Override
    public PaymentWrapper map(InvoiceChange invoiceChange, MachineEvent event, Integer changeId, LocalStorage storage) {
        InvoicePaymentStarted invoicePaymentStarted = invoiceChange
                .getInvoicePaymentChange()
                .getPayload()
                .getInvoicePaymentStarted();

        PaymentWrapper paymentWrapper = new PaymentWrapper();
        paymentWrapper.setShouldInsert(true);
        Payment payment = new Payment();
        paymentWrapper.setPayment(payment);
        InvoicePayment invoicePayment = invoicePaymentStarted.getPayment();

        long sequenceId = event.getEventId();
        String invoiceId = event.getSourceId();

        String paymentId = invoicePayment.getId();
        log.info("Start payment created mapping, sequenceId={}, invoiceId={}, paymentId={}", sequenceId, invoiceId,
                paymentId);
        paymentWrapper.setKey(InvoicingKey.buildKey(invoiceId, paymentId));
        setInsertProperties(payment, sequenceId, changeId, event.getCreatedAt());
        payment.setPaymentId(paymentId);
        payment.setCreatedAt(TypeUtil.stringToLocalDateTime(invoicePayment.getCreatedAt()));
        payment.setInvoiceId(invoiceId);
        payment.setExternalId(invoicePayment.getExternalId());


        PartyShop partyShop = Optional.ofNullable((PartyShop) storage.get(InvoicingKey.buildKey(invoiceId)))
                .orElseGet(() -> {
                    InvoiceWrapper invoiceWrapper = invoiceWrapperService.get(invoiceId);
                    if (invoiceWrapper == null || invoiceWrapper.getInvoice() == null) {
                        return null;
                    }
                    return new PartyShop(
                            invoiceWrapper.getInvoice().getPartyId(),
                            invoiceWrapper.getInvoice().getShopId()
                    );
                });

        if (partyShop == null) {
            return null;
        }

        payment.setPartyId(partyShop.getPartyId());
        payment.setShopId(partyShop.getShopId());
        payment.setDomainRevision(invoicePayment.getDomainRevision());
        if (invoicePayment.isSetPartyRevision()) {
            payment.setPartyRevision(invoicePayment.getPartyRevision());
        }
        payment.setStatus(TBaseUtil.unionFieldToEnum(invoicePayment.getStatus(), PaymentStatus.class));
        if (invoicePayment.getStatus().isSetCancelled()) {
            payment.setStatusCancelledReason(invoicePayment.getStatus().getCancelled().getReason());
        } else if (invoicePayment.getStatus().isSetCaptured()) {
            payment.setStatusCapturedReason(invoicePayment.getStatus().getCaptured().getReason());
        } else if (invoicePayment.getStatus().isSetFailed()) {
            payment.setStatusFailedFailure(JsonUtil.thriftBaseToJsonString(invoicePayment.getStatus().getFailed()));
        }
        payment.setAmount(invoicePayment.getCost().getAmount());
        payment.setCurrencyCode(invoicePayment.getCost().getCurrency().getSymbolicCode());
        Payer payer = invoicePayment.getPayer();
        payment.setPayerType(TBaseUtil.unionFieldToEnum(payer, PayerType.class));
        if (payer.isSetPaymentResource()) {
            PaymentResourcePayer paymentResource = payer.getPaymentResource();
            fillPaymentTool(payment, paymentResource.getResource().getPaymentTool());
            fillContactInfo(payment, paymentResource.getContactInfo());
            if (paymentResource.getResource().isSetClientInfo()) {
                payment.setPayerIpAddress(paymentResource.getResource().getClientInfo().getIpAddress());
                payment.setPayerFingerprint(paymentResource.getResource().getClientInfo().getFingerprint());
            }
        } else if (payer.isSetCustomer()) {
            CustomerPayer customer = payer.getCustomer();
            payment.setPayerCustomerId(customer.getCustomerId());
            payment.setPayerCustomerBindingId(customer.getCustomerBindingId());
            payment.setPayerCustomerRecPaymentToolId(customer.getRecPaymentToolId());
            fillPaymentTool(payment, customer.getPaymentTool());
            fillContactInfo(payment, customer.getContactInfo());
        } else if (payer.isSetRecurrent()) {
            payment.setPayerRecurrentParentInvoiceId(payer.getRecurrent().getRecurrentParent().getInvoiceId());
            payment.setPayerRecurrentParentPaymentId(payer.getRecurrent().getRecurrentParent().getPaymentId());
            fillPaymentTool(payment, payer.getRecurrent().getPaymentTool());
            fillContactInfo(payment, payer.getRecurrent().getContactInfo());
        }
        payment.setPaymentFlowType(TBaseUtil.unionFieldToEnum(invoicePayment.getFlow(), PaymentFlowType.class));
        if (invoicePayment.getFlow().isSetHold()) {
            payment.setPaymentFlowHeldUntil(
                    TypeUtil.stringToLocalDateTime(invoicePayment.getFlow().getHold().getHeldUntil()));
            payment.setPaymentFlowOnHoldExpiration(invoicePayment.getFlow().getHold().getOnHoldExpiration().name());
        }
        if (invoicePaymentStarted.isSetRoute()) {
            payment.setRouteProviderId(invoicePaymentStarted.getRoute().getProvider().getId());
            payment.setRouteTerminalId(invoicePaymentStarted.getRoute().getTerminal().getId());
        }
        if (invoicePayment.isSetMakeRecurrent()) {
            payment.setMakeRecurrent(invoicePayment.isMakeRecurrent());
        }

        if (invoicePaymentStarted.isSetCashFlow()) {
            List<CashFlow> cashFlowList =
                    CashFlowUtil.convertCashFlows(invoicePaymentStarted.getCashFlow(), null, PaymentChangeType.payment);
            paymentWrapper.setCashFlows(cashFlowList);
            Map<CashFlowType, Long> parsedCashFlow = CashFlowUtil.parseCashFlow(invoicePaymentStarted.getCashFlow());
            payment.setFee(parsedCashFlow.getOrDefault(CashFlowType.FEE, 0L));
            payment.setProviderFee(parsedCashFlow.getOrDefault(CashFlowType.PROVIDER_FEE, 0L));
            payment.setExternalFee(parsedCashFlow.getOrDefault(CashFlowType.EXTERNAL_FEE, 0L));
            payment.setGuaranteeDeposit(parsedCashFlow.getOrDefault(CashFlowType.GUARANTEE_DEPOSIT, 0L));
        }
        log.info("Payment has been mapped, sequenceId={}, invoiceId={}, paymentId={}", sequenceId, invoiceId,
                paymentId);
        return paymentWrapper;
    }

    private void fillContactInfo(Payment payment, ContactInfo contactInfo) {
        payment.setPayerPhoneNumber(contactInfo.getPhoneNumber());
        payment.setPayerEmail(contactInfo.getEmail());
    }

    private void fillPaymentTool(Payment payment, PaymentTool paymentTool) {
        payment.setPayerPaymentToolType(TBaseUtil.unionFieldToEnum(paymentTool, PaymentToolType.class));
        if (paymentTool.isSetBankCard()) {
            BankCard bankCard = paymentTool.getBankCard();
            payment.setPayerBankCardToken(bankCard.getToken());
            payment.setPayerBankCardPaymentSystem(PaymentSystemUtil.getPaymentSystemName(bankCard));
            payment.setPayerBankCardBin(bankCard.getBin());
            payment.setPayerBankCardMaskedPan(bankCard.getLastDigits());
            payment.setPayerBankName(bankCard.getBankName());
            payment.setPayerBankCardCardholderName(bankCard.getCardholderName());
            if (bankCard.isSetIssuerCountry()) {
                payment.setPayerIssuerCountry(bankCard.getIssuerCountry().name());
            }
            payment.setPayerBankCardTokenProvider(TokenProviderUtil.getTokenProviderName(bankCard));
        } else if (paymentTool.isSetPaymentTerminal()) {
            payment.setPayerPaymentTerminalType(
                    TerminalPaymentUtil.getTerminalPaymentProviderName(paymentTool.getPaymentTerminal()));
        } else if (paymentTool.isSetDigitalWallet()) {
            payment.setPayerDigitalWalletId(paymentTool.getDigitalWallet().getId());
            payment.setPayerDigitalWalletProvider(
                    DigitalWalletUtil.getDigitalWalletName(paymentTool.getDigitalWallet()));
        } else if (CryptoCurrencyUtil.isSetCryptoCurrency(paymentTool)) {
            payment.setPayerCryptoCurrencyType(CryptoCurrencyUtil.getCryptoCurrencyName(paymentTool));
        } else if (paymentTool.isSetMobileCommerce()) {
            payment.setPayerMobileOperator(MobileOperatorUtil.getMobileOperatorName(paymentTool.getMobileCommerce()));
            payment.setPayerMobilePhoneCc(paymentTool.getMobileCommerce().getPhone().getCc());
            payment.setPayerMobilePhoneCtn(paymentTool.getMobileCommerce().getPhone().getCtn());
        }
    }

    @Override
    public Filter<InvoiceChange> getFilter() {
        return filter;
    }
}
