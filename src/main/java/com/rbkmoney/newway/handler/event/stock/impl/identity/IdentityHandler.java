package com.rbkmoney.newway.handler.event.stock.impl.identity;

import dev.vality.fistful.identity.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import com.rbkmoney.newway.handler.event.stock.Handler;

public interface IdentityHandler extends Handler<TimestampedChange, MachineEvent> {
}
