package dev.vality.newway.handler.dominant.impl;

import dev.vality.damsel.domain.*;
import dev.vality.newway.dao.dominant.impl.ProviderDaoImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class ProviderHandlerTest {

    @Mock
    private ProviderDaoImpl providerDao;

    @Test
    public void convertToDatabaseObjectTest() throws IOException {
        ProviderObject providerObject = buildProviderObject();
        ProviderHandler providerHandler = new ProviderHandler(providerDao);
        providerHandler.setDomainObject(DomainObject.provider(providerObject));
        dev.vality.newway.domain.tables.pojos.Provider provider =
                providerHandler.convertToDatabaseObject(providerObject, 1L, true);
        Assertions.assertNotNull(provider);
        Assertions.assertEquals(provider.getName(), providerObject.getData().getName());
        Assertions.assertEquals(provider.getIdentity(), providerObject.getData().getIdentity());
        Assertions.assertEquals(provider.getDescription(), providerObject.getData().getDescription());
        Assertions.assertEquals(provider.getAbsAccount(), providerObject.getData().getAbsAccount());
        Assertions.assertFalse(provider.getPaymentTermsJson().isEmpty());
        Assertions.assertFalse(provider.getRecurrentPaytoolTermsJson().isEmpty());
    }

    private ProviderObject buildProviderObject() throws IOException {
        return new ProviderObject()
                .setRef(new ProviderRef(1))
                .setData(new Provider()
                        .setName(dev.vality.testcontainers.annotations.util.RandomBeans.random(String.class))
                        .setDescription(dev.vality.testcontainers.annotations.util.RandomBeans.random(String.class))
                        .setProxy(new Proxy()
                                .setRef(new ProxyRef(dev.vality.testcontainers.annotations.util.RandomBeans.random(Integer.class)))
                                .setAdditional(Map.of(dev.vality.testcontainers.annotations.util.RandomBeans.random(String.class), dev.vality.testcontainers.annotations.util.RandomBeans.random(String.class)))
                        )
                        .setIdentity(dev.vality.testcontainers.annotations.util.RandomBeans.random(String.class))
                        .setAccounts(
                                Map.of(new CurrencyRef(dev.vality.testcontainers.annotations.util.RandomBeans.random(String.class)), new ProviderAccount(dev.vality.testcontainers.annotations.util.RandomBeans.random(Long.class))))
                        .setTerms(new ProvisionTermSet()
                                .setPayments(buildProvisionTermSet())
                                .setRecurrentPaytools(buildRecurrentPaytools())
                        )
                        .setAbsAccount(dev.vality.testcontainers.annotations.util.RandomBeans.random(String.class))
                        .setParamsSchema(Collections.singletonList(buildProviderParameter()))
                );
    }

    private PaymentsProvisionTerms buildProvisionTermSet() {
        CashFlowPosting cashFlowPosting = new CashFlowPosting();
        cashFlowPosting.setSource(buildMerchantCashFlowAccount());
        cashFlowPosting.setDestination(buildProviderCashFlowAccount());
        CashVolume cashVolume = new CashVolume();
        cashVolume.setFixed(new CashVolumeFixed().setCash(new Cash(1000L, new CurrencyRef("RUB"))));
        cashFlowPosting.setVolume(cashVolume);
        PaymentChargebackProvisionTerms paymentChargebackProvisionTerms = new PaymentChargebackProvisionTerms();
        PaymentsProvisionTerms provisionTermSet = new PaymentsProvisionTerms();
        paymentChargebackProvisionTerms.setCashFlow(CashFlowSelector.value(List.of(cashFlowPosting)));
        provisionTermSet.setChargebacks(paymentChargebackProvisionTerms);
        return provisionTermSet;
    }

    private RecurrentPaytoolsProvisionTerms buildRecurrentPaytools() {
        RecurrentPaytoolsProvisionTerms recurrentPaytoolsProvisionTerms = new RecurrentPaytoolsProvisionTerms();
        PaymentMethodSelector paymentMethodSelector = new PaymentMethodSelector();
        paymentMethodSelector.setValue(Set.of(new PaymentMethodRef(
                PaymentMethod.bank_card(new BankCardPaymentMethod()
                        .setPaymentSystemDeprecated(LegacyBankCardPaymentSystem.visa)))));
        recurrentPaytoolsProvisionTerms.setPaymentMethods(paymentMethodSelector);
        CashValueSelector cashValueSelector = new CashValueSelector();
        cashValueSelector.setValue(buildCash());
        recurrentPaytoolsProvisionTerms.setCashValue(cashValueSelector);
        recurrentPaytoolsProvisionTerms.setCategories(CategorySelector.value(Set.of(new CategoryRef(123))));
        return recurrentPaytoolsProvisionTerms;
    }

    private ProviderParameter buildProviderParameter() {
        ProviderParameter providerParameter = new ProviderParameter();
        providerParameter.setId(dev.vality.testcontainers.annotations.util.RandomBeans.random(String.class));
        providerParameter.setDescription(dev.vality.testcontainers.annotations.util.RandomBeans.random(String.class));
        providerParameter.setType(ProviderParameterType.string_type(new ProviderParameterString()));
        providerParameter.setIsRequired(true);
        return providerParameter;
    }

    private CashFlowAccount buildMerchantCashFlowAccount() {
        CashFlowAccount cashFlowAccount = new CashFlowAccount();
        cashFlowAccount.setMerchant(MerchantCashFlowAccount.guarantee);
        return cashFlowAccount;
    }

    private CashFlowAccount buildProviderCashFlowAccount() {
        CashFlowAccount cashFlowAccount = new CashFlowAccount();
        cashFlowAccount.setProvider(ProviderCashFlowAccount.settlement);
        return cashFlowAccount;
    }

    private Cash buildCash() {
        Cash cash = new Cash();
        cash.setAmount(1000);
        cash.setCurrency(new CurrencyRef("RUB"));
        return cash;
    }

}
