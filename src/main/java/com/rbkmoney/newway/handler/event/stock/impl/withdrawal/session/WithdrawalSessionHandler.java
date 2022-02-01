package com.rbkmoney.newway.handler.event.stock.impl.withdrawal.session;

import dev.vality.fistful.withdrawal_session.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import com.rbkmoney.newway.handler.event.stock.Handler;

public interface WithdrawalSessionHandler extends Handler<TimestampedChange, MachineEvent> {
}
