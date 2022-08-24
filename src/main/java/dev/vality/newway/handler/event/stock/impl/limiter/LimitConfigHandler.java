package dev.vality.newway.handler.event.stock.impl.limiter;

import dev.vality.limiter.config.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.handler.event.stock.Handler;

public interface LimitConfigHandler extends Handler<TimestampedChange, MachineEvent> {
}
