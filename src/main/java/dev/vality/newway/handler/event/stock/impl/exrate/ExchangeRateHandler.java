package dev.vality.newway.handler.event.stock.impl.exrate;

import dev.vality.exrates.events.CurrencyEvent;

public interface ExchangeRateHandler {

    void handle(CurrencyEvent currencyEvent);

    boolean isHandle(CurrencyEvent currencyEvent);
}
