package com.rbkmoney.newway.handler.event.stock.impl.invoicing;

import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import com.rbkmoney.newway.handler.event.stock.Handler;

public interface InvoicingHandler extends Handler<InvoiceChange, MachineEvent> {
}
