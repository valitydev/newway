package dev.vality.newway.handler.event.stock.impl.exrate;

import dev.vality.exrates.events.CurrencyEvent;

import java.util.List;

public interface ExchangeRateHandler {

    void handle(List<CurrencyEvent> currencyEvents);

    boolean isHandle(CurrencyEvent currencyEvent);
}
