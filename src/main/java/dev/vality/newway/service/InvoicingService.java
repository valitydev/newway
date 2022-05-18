package dev.vality.newway.service;

import dev.vality.damsel.payment_processing.EventPayload;
import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.handler.event.stock.impl.invoicing.InvoicingHandler;
import dev.vality.newway.mapper.Mapper;
import dev.vality.newway.model.InvoiceWrapper;
import dev.vality.newway.model.PartyShop;
import dev.vality.newway.model.PaymentWrapper;
import dev.vality.sink.common.parser.impl.MachineEventParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoicingService {

    private final List<InvoicingHandler> otherHandlers;
    private final List<Mapper<InvoiceWrapper>> invoiceMappers;
    private final List<Mapper<PaymentWrapper>> paymentMappers;
    private final PartyShopCacheService partyShopCacheService;
    private final InvoiceWrapperService invoiceWrapperService;
    private final PaymentWrapperService paymentWrapperService;
    private final MachineEventParser<EventPayload> parser;

    @Transactional(propagation = Propagation.REQUIRED)
    public void handleEvents(List<MachineEvent> machineEvents) {
        List<InvoiceWrapper> invoices = new ArrayList<>(machineEvents.size());
        List<PaymentWrapper> payments = new ArrayList<>(machineEvents.size());
        machineEvents.forEach(me -> processMachineEvents(invoices, payments, me));
        if (!invoices.isEmpty()) {
            invoiceWrapperService.save(invoices);
        }
        if (!payments.isEmpty()) {
            paymentWrapperService.save(payments);
        }
    }

    private void processMachineEvents(List<InvoiceWrapper> invoices, List<PaymentWrapper> payments, MachineEvent me) {
        EventPayload payload = parser.parse(me);
        if (payload.isSetInvoiceChanges()) {
            try {
                List<InvoiceChange> invoiceChanges = payload.getInvoiceChanges();
                for (int changeId = 0; changeId < invoiceChanges.size(); changeId++) {
                    InvoiceChange change = invoiceChanges.get(changeId);
                    InvoiceWrapper invoiceWrapper = mapInvoice(change, me, changeId);
                    if (invoiceWrapper != null) {
                        invoices.add(invoiceWrapper);
                        if (invoiceWrapper.getInvoice() != null) {
                            partyShopCacheService.put(
                                    invoiceWrapper.getInvoice().getInvoiceId(),
                                    new PartyShop(
                                            invoiceWrapper.getInvoice().getPartyId(),
                                            invoiceWrapper.getInvoice().getShopId()
                                    )
                            );
                        }
                    }
                    PaymentWrapper paymentWrapper = mapPayment(change, me, changeId);
                    if (paymentWrapper != null) {
                        payments.add(paymentWrapper);
                    }
                    handleOtherEvent(change, me, changeId);
                }
            } catch (Throwable e) {
                log.error("Unexpected error while handling events; machineId: {},  eventId: {}", me.getSourceId(),
                        me.getEventId(), e);
                throw e;
            }
        }
    }

    private void handleOtherEvent(InvoiceChange change, MachineEvent me, int changeId) {
        otherHandlers.stream()
                .filter(m -> m.accept(change))
                .findFirst()
                .ifPresent(m -> m.handle(change, me, changeId));
    }

    private PaymentWrapper mapPayment(InvoiceChange change, MachineEvent me, int changeId) {
        return paymentMappers.stream()
                .filter(m -> m.accept(change))
                .findFirst()
                .map(m -> m.map(change, me, changeId))
                .orElse(null);
    }

    private InvoiceWrapper mapInvoice(InvoiceChange change, MachineEvent me, int changeId) {
        return invoiceMappers.stream()
                .filter(m -> m.accept(change))
                .findFirst()
                .map(m -> m.map(change, me, changeId))
                .orElse(null);
    }
}
