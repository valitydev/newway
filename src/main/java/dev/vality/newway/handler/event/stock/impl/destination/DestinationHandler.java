package dev.vality.newway.handler.event.stock.impl.destination;

import dev.vality.fistful.destination.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.handler.event.stock.Handler;

public interface DestinationHandler extends Handler<TimestampedChange, MachineEvent> {
}
