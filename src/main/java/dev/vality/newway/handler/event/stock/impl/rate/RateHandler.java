package dev.vality.newway.handler.event.stock.impl.rate;

import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.handler.event.stock.Handler;
import dev.vality.xrates.rate.Change;

public interface RateHandler extends Handler<Change, MachineEvent> {
}
