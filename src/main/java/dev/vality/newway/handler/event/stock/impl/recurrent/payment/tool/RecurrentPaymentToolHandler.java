package dev.vality.newway.handler.event.stock.impl.recurrent.payment.tool;

import dev.vality.damsel.payment_processing.RecurrentPaymentToolChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.handler.event.stock.Handler;

public interface RecurrentPaymentToolHandler extends Handler<RecurrentPaymentToolChange, MachineEvent> {

}
