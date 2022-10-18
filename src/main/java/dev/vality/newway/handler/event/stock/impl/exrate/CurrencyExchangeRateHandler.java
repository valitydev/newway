package dev.vality.newway.handler.event.stock.impl.exrate;

import dev.vality.exrates.events.CurrencyEvent;
import dev.vality.exrates.events.CurrencyExchangeRate;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.newway.dao.exrate.iface.ExchangeRateDao;
import dev.vality.newway.domain.tables.pojos.Exrate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyExchangeRateHandler implements ExchangeRateHandler {

    private final ExchangeRateDao exchangeRateDao;

    @Override
    public void handle(CurrencyEvent event) {
        log.info("Handle currency event. eventId={};sourceCurrency={};destinationCurrency={}",
                event.getEventId(),
                event.payload.getExchangeRate().getSourceCurrency(),
                event.payload.getExchangeRate().getDestinationCurrency());
        CurrencyExchangeRate exchangeRate = event.getPayload().getExchangeRate();
        Exrate exrate = new Exrate();
        exrate.setEventId(UUID.fromString(event.getEventId()));
        exrate.setEventCreatedAt(TypeUtil.stringToLocalDateTime(event.getEventCreatedAt()));
        exrate.setSourceCurrencySymbolicCode(exchangeRate.getSourceCurrency().getSymbolicCode());
        exrate.setSourceCurrencyExponent(exchangeRate.getSourceCurrency().getExponent());
        exrate.setDestinationCurrencySymbolicCode(exchangeRate.getDestinationCurrency().getSymbolicCode());
        exrate.setDestinationCurrencyExponent(exchangeRate.getDestinationCurrency().getExponent());
        exrate.setRationalP(exchangeRate.getExchangeRate().getP());
        exrate.setRationalQ(exchangeRate.getExchangeRate().getQ());
        exrate.setRateTimestamp(TypeUtil.stringToLocalDateTime(exchangeRate.timestamp));

        exchangeRateDao.save(exrate);

        log.info("The exchange rate was successfully saved. eventId={};sourceCurrency={};destinationCurrency={}",
                event.getEventId(),
                event.payload.getExchangeRate().getSourceCurrency(),
                event.payload.getExchangeRate().getDestinationCurrency());
    }

    @Override
    public boolean isHandle(CurrencyEvent currencyEvent) {
        return currencyEvent.payload.isSetExchangeRate();
    }
}
