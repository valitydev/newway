package com.rbkmoney.newway.mapper;

import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.machinegun.eventsink.MachineEvent;

public abstract class AbstractInvoicingMapper<M> implements Mapper<InvoiceChange, MachineEvent, M> {
}
