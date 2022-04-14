package dev.vality.newway.service;

import dev.vality.damsel.payment_processing.EventPayload;
import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.handler.event.stock.LocalStorage;
import dev.vality.newway.handler.event.stock.impl.invoicing.InvoicingHandler;
import dev.vality.newway.mapper.AbstractInvoicingMapper;
import dev.vality.newway.model.InvoiceWrapper;
import dev.vality.newway.model.InvoicingKey;
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
    private final List<AbstractInvoicingMapper<InvoiceWrapper>> invoiceMappers;
    private final List<AbstractInvoicingMapper<PaymentWrapper>> paymentMappers;
    private final InvoiceBatchService invoiceBatchService;
    private final PaymentBatchService paymentBatchService;
    private final MachineEventParser<EventPayload> parser;

    @Transactional(propagation = Propagation.REQUIRED)
    public void handleEvents(List<MachineEvent> machineEvents) {
        LocalStorage storage = new LocalStorage();
        List<InvoiceWrapper> invoices = new ArrayList<>(machineEvents.size());
        List<PaymentWrapper> payments = new ArrayList<>(machineEvents.size());
        machineEvents.forEach(me -> {
            EventPayload payload = parser.parse(me);
            if (payload.isSetInvoiceChanges()) {
                try {
                    List<InvoiceChange> invoiceChanges = payload.getInvoiceChanges();
                    for (int i = 0; i < invoiceChanges.size(); i++) {
                        InvoiceChange change = invoiceChanges.get(i);
                        InvoiceWrapper invoiceWrapper = mapInvoice(change, me, i, storage);
                        PaymentWrapper paymentWrapper = mapPayment(change, me, i, storage);
                        if (invoiceWrapper != null) {
                            invoices.add(invoiceWrapper);
                            if (invoiceWrapper.getInvoice() != null) {
                                storage.put(
                                        InvoicingKey.buildKey(invoiceWrapper.getInvoice().getInvoiceId()),
                                        new PartyShop(
                                                invoiceWrapper.getInvoice().getPartyId(),
                                                invoiceWrapper.getInvoice().getShopId()
                                        ));
                            }
                        }
                        if (paymentWrapper != null) {
                            payments.add(paymentWrapper);
                            storage.put(InvoicingKey.buildKey(paymentWrapper), paymentWrapper);
                        }
                        handleOtherEvent(change, me, i);
                    }
                } catch (Throwable e) {
                    log.error("Unexpected error while handling events; machineId: {},  eventId: {}", me.getSourceId(),
                            me.getEventId(), e);
                    throw e;
                }
            }
        });
        if (!invoices.isEmpty()) {
            invoiceBatchService.process(invoices);
        }
        if (!payments.isEmpty()) {
            paymentBatchService.process(payments);
        }
    }

    private void handleOtherEvent(InvoiceChange change, MachineEvent me, int i) {
        otherHandlers.stream()
                .filter(m -> m.accept(change))
                .findFirst()
                .ifPresent(m -> m.handle(change, me, i));
    }

    private PaymentWrapper mapPayment(InvoiceChange change, MachineEvent me, int i, LocalStorage storage) {
        return paymentMappers.stream()
                .filter(m -> m.accept(change))
                .findFirst()
                .map(m -> m.map(change, me, i, storage))
                .orElse(null);
    }

    private InvoiceWrapper mapInvoice(InvoiceChange change, MachineEvent me, int i, LocalStorage storage) {
        return invoiceMappers.stream()
                .filter(m -> m.accept(change))
                .findFirst()
                .map(m -> m.map(change, me, i, storage))
                .orElse(null);
    }
}
