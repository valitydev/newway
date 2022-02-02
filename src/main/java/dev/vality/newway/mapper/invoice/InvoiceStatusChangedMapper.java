package dev.vality.newway.mapper.invoice;

import dev.vality.damsel.domain.InvoiceStatus;
import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.geck.common.util.TBaseUtil;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.domain.tables.pojos.Invoice;
import dev.vality.newway.exception.DaoException;
import dev.vality.newway.handler.event.stock.LocalStorage;
import dev.vality.newway.model.InvoiceWrapper;
import dev.vality.newway.service.InvoiceWrapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvoiceStatusChangedMapper extends AbstractInvoicingInvoiceMapper {

    private final InvoiceWrapperService invoiceWrapperService;

    private Filter filter = new PathConditionFilter(
            new PathConditionRule("invoice_status_changed", new IsNullCondition().not()));

    @Override
    public InvoiceWrapper map(InvoiceChange invoiceChange, MachineEvent event, Integer changeId, LocalStorage storage)
            throws DaoException {
        InvoiceStatus invoiceStatus = invoiceChange.getInvoiceStatusChanged().getStatus();
        long sequenceId = event.getEventId();
        String invoiceId = event.getSourceId();

        InvoiceWrapper invoiceWrapper = invoiceWrapperService.get(invoiceId, storage);
        Invoice invoiceSource = invoiceWrapper.getInvoice();
        log.info("Start invoice status changed mapping, sequenceId={}, invoiceId={}, partyId={}, shopId={}, status={}",
                sequenceId, invoiceId, invoiceSource.getPartyId(), invoiceSource.getShopId(),
                invoiceStatus.getSetField().getFieldName());

        setDefaultProperties(invoiceSource, sequenceId, changeId, event.getCreatedAt());
        invoiceSource.setStatus(
                TBaseUtil.unionFieldToEnum(invoiceStatus, dev.vality.newway.domain.enums.InvoiceStatus.class));
        if (invoiceStatus.isSetCancelled()) {
            invoiceSource.setStatusCancelledDetails(invoiceStatus.getCancelled().getDetails());
            invoiceSource.setStatusFulfilledDetails(null);
        } else if (invoiceStatus.isSetFulfilled()) {
            invoiceSource.setStatusCancelledDetails(null);
            invoiceSource.setStatusFulfilledDetails(invoiceStatus.getFulfilled().getDetails());
        }

        log.info("Invoice has been mapped, sequenceId={}, invoiceId={}, partyId={}, shopId={}, status={}",
                sequenceId, invoiceId, invoiceSource.getPartyId(), invoiceSource.getShopId(),
                invoiceStatus.getSetField().getFieldName());
        return invoiceWrapper;
    }

    @Override
    public Filter<InvoiceChange> getFilter() {
        return filter;
    }
}
