package com.rbkmoney.newway.handler.event.stock.impl.withdrawal;


import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import com.rbkmoney.newway.handler.event.stock.Handler;

public interface WithdrawalHandler extends Handler<TimestampedChange, MachineEvent> {
}
