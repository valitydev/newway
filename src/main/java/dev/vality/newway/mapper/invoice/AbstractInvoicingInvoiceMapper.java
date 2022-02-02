package dev.vality.newway.mapper.invoice;

import dev.vality.geck.common.util.TypeUtil;
import dev.vality.newway.domain.tables.pojos.Invoice;
import dev.vality.newway.mapper.AbstractInvoicingMapper;
import dev.vality.newway.model.InvoiceWrapper;

public abstract class AbstractInvoicingInvoiceMapper extends AbstractInvoicingMapper<InvoiceWrapper> {
    protected void setDefaultProperties(Invoice invoice, Long sequenceId, Integer changeId, String eventCreatedAt) {
        invoice.setId(null);
        invoice.setWtime(null);
        invoice.setCurrent(false);
        invoice.setChangeId(changeId);
        invoice.setSequenceId(sequenceId);
        invoice.setEventCreatedAt(TypeUtil.stringToLocalDateTime(eventCreatedAt));
    }
}
