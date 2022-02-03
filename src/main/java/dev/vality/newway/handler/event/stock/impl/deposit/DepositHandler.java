package dev.vality.newway.handler.event.stock.impl.deposit;

import dev.vality.fistful.deposit.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.handler.event.stock.Handler;

public interface DepositHandler extends Handler<TimestampedChange, MachineEvent> {
}
