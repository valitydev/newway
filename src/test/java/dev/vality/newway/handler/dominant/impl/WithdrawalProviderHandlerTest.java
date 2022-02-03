package dev.vality.newway.handler.dominant.impl;

import dev.vality.damsel.domain.*;
import dev.vality.newway.dao.dominant.impl.WithdrawalProviderDaoImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Map;

public class WithdrawalProviderHandlerTest {

    @Mock
    private WithdrawalProviderDaoImpl withdrawalProviderDao;

    @Test
    public void convertToDatabaseObject() {
        WithdrawalProviderObject withdrawalProviderObject = buildWithdrawalProviderObject();

        WithdrawalProviderHandler handler = new WithdrawalProviderHandler(withdrawalProviderDao);
        handler.setDomainObject(DomainObject.withdrawal_provider(withdrawalProviderObject));
        var withdrawalProvider = handler.convertToDatabaseObject(withdrawalProviderObject, 123L, true);
        Assertions.assertNotNull(withdrawalProvider);
        Assertions.assertEquals(withdrawalProvider.getName(), withdrawalProviderObject.getData().getName());
        Assertions.assertEquals(withdrawalProvider.getIdentity(), withdrawalProviderObject.getData().getIdentity());
        Assertions.assertTrue(withdrawalProvider.getAccountsJson().contains("RUB"));
    }

    private WithdrawalProviderObject buildWithdrawalProviderObject() {
        return new WithdrawalProviderObject()
                .setRef(new WithdrawalProviderRef(1))
                .setData(new WithdrawalProvider()
                        .setName(dev.vality.testcontainers.annotations.util.RandomBeans.random(String.class))
                        .setDescription(dev.vality.testcontainers.annotations.util.RandomBeans.random(String.class))
                        .setProxy(new Proxy()
                                .setRef(new ProxyRef(dev.vality.testcontainers.annotations.util.RandomBeans.random(Integer.class)))
                                .setAdditional(Map.of(dev.vality.testcontainers.annotations.util.RandomBeans.random(String.class), dev.vality.testcontainers.annotations.util.RandomBeans.random(String.class))))
                        .setIdentity(dev.vality.testcontainers.annotations.util.RandomBeans.random(String.class))
                        .setAccounts(Map.of(new CurrencyRef("RUB"), new ProviderAccount(123))));
    }
}
