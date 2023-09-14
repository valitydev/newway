package dev.vality.newway.handler.event.stock.impl.withdrawal;

import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.handler.event.stock.Handler;

public interface WithdrawalAdjustmentHandler extends Handler<TimestampedChange, MachineEvent> {
}
