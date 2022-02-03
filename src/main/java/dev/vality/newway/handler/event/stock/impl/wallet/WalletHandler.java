package dev.vality.newway.handler.event.stock.impl.wallet;

import dev.vality.fistful.wallet.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.handler.event.stock.Handler;

public interface WalletHandler extends Handler<TimestampedChange, MachineEvent> {
}
