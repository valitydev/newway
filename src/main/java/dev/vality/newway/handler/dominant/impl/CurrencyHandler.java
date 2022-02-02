package dev.vality.newway.handler.dominant.impl;

import dev.vality.damsel.domain.CurrencyObject;
import dev.vality.newway.dao.dominant.iface.DomainObjectDao;
import dev.vality.newway.dao.dominant.impl.CurrencyDaoImpl;
import dev.vality.newway.domain.tables.pojos.Currency;
import dev.vality.newway.handler.dominant.AbstractDominantHandler;
import org.springframework.stereotype.Component;

@Component
public class CurrencyHandler extends AbstractDominantHandler<CurrencyObject, Currency, String> {

    private final CurrencyDaoImpl currencyDao;

    public CurrencyHandler(CurrencyDaoImpl currencyDao) {
        this.currencyDao = currencyDao;
    }

    @Override
    protected DomainObjectDao<Currency, String> getDomainObjectDao() {
        return currencyDao;
    }

    @Override
    protected CurrencyObject getTargetObject() {
        return getDomainObject().getCurrency();
    }

    @Override
    protected String getTargetObjectRefId() {
        return getTargetObject().getRef().getSymbolicCode();
    }

    @Override
    protected boolean acceptDomainObject() {
        return getDomainObject().isSetCurrency();
    }

    @Override
    public Currency convertToDatabaseObject(CurrencyObject currencyObject, Long versionId, boolean current) {
        Currency currency = new Currency();
        currency.setVersionId(versionId);
        currency.setCurrencyRefId(getTargetObjectRefId());
        dev.vality.damsel.domain.Currency data = currencyObject.getData();
        currency.setName(data.getName());
        currency.setSymbolicCode(data.getSymbolicCode());
        currency.setNumericCode(data.getNumericCode());
        currency.setExponent(data.getExponent());
        currency.setCurrent(current);
        return currency;
    }
}
