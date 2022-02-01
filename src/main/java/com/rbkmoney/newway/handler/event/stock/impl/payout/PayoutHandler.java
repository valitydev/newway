package com.rbkmoney.newway.handler.event.stock.impl.payout;

import com.rbkmoney.newway.handler.event.stock.Handler;
import dev.vality.payout.manager.Event;
import dev.vality.payout.manager.PayoutChange;

public interface PayoutHandler extends Handler<PayoutChange, Event> {
}
