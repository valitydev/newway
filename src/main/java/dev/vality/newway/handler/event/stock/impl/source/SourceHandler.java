package dev.vality.newway.handler.event.stock.impl.source;

import dev.vality.fistful.source.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.handler.event.stock.Handler;

public interface SourceHandler extends Handler<TimestampedChange, MachineEvent> {

}
