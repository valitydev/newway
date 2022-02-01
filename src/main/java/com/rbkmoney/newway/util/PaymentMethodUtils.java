package com.rbkmoney.newway.util;

import dev.vality.damsel.domain.*;
import dev.vality.mamsel.PaymentSystemUtil;
import dev.vality.mamsel.TokenProviderUtil;

import java.util.Optional;
import java.util.function.Supplier;

public class PaymentMethodUtils {

    private static final String TOKENIZED_BANK_CARD_SEPARATOR = "_";
    private static final String EMPTY_CVV = "empty_cvv_";

    public static Optional<String> getPaymentMethodRefIdByBankCard(
            Supplier<Optional<PaymentMethod>> paymentMethod) {
        return paymentMethod.get()
                .filter(dev.vality.damsel.domain.PaymentMethod::isSetBankCard)
                .map(dev.vality.damsel.domain.PaymentMethod::getBankCard)
                .flatMap(bankCard -> PaymentSystemUtil.getPaymentSystemNameIfPresent(
                        bankCard.getPaymentSystem(),
                        bankCard.getPaymentSystemDeprecated()))
                .or(() -> paymentMethod.get()
                        .filter(dev.vality.damsel.domain.PaymentMethod::isSetBankCardDeprecated)
                        .map(dev.vality.damsel.domain.PaymentMethod::getBankCardDeprecated)
                        .map(Enum::name))
                .or(() -> paymentMethod.get()
                        .filter(dev.vality.damsel.domain.PaymentMethod::isSetEmptyCvvBankCardDeprecated)
                        .map(dev.vality.damsel.domain.PaymentMethod::getEmptyCvvBankCardDeprecated)
                        .map(legacyBankCardPaymentSystem -> EMPTY_CVV + legacyBankCardPaymentSystem.name()))
                .or(() -> paymentMethod.get()
                        .filter(dev.vality.damsel.domain.PaymentMethod::isSetTokenizedBankCardDeprecated)
                        .map(dev.vality.damsel.domain.PaymentMethod::getTokenizedBankCardDeprecated)
                        .flatMap(PaymentMethodUtils::getTokenizedBankCardId));
    }

    private static Optional<String> getTokenizedBankCardId(TokenizedBankCard tokenizedBankCard) {
        Optional<String> paymentSystemNameIfPresent = PaymentSystemUtil.getPaymentSystemNameIfPresent(
                tokenizedBankCard.getPaymentSystem(),
                tokenizedBankCard.getPaymentSystemDeprecated());
        Optional<String> tokenProviderNameIfPresent = TokenProviderUtil.getTokenProviderNameIfPresent(
                tokenizedBankCard.getPaymentToken(),
                tokenizedBankCard.getTokenProviderDeprecated());

        return paymentSystemNameIfPresent
                .flatMap(paymentSystemName -> tokenProviderNameIfPresent
                        .map(tokenProviderName -> paymentSystemName +
                                TOKENIZED_BANK_CARD_SEPARATOR +
                                tokenProviderName));
    }

    public static Optional<String> getPaymentMethodRefIdByPaymentTerminal(
            Supplier<Optional<dev.vality.damsel.domain.PaymentMethod>> paymentMethod) {
        return paymentMethod.get()
                .filter(dev.vality.damsel.domain.PaymentMethod::isSetPaymentTerminal)
                .map(dev.vality.damsel.domain.PaymentMethod::getPaymentTerminal)
                .map(PaymentServiceRef::getId)
                .or(() -> paymentMethod.get()
                        .filter(dev.vality.damsel.domain.PaymentMethod::isSetPaymentTerminalDeprecated)
                        .map(dev.vality.damsel.domain.PaymentMethod::getPaymentTerminalDeprecated)
                        .map(Enum::name));
    }

    public static Optional<String> getPaymentMethodRefIdByDigitalWallet(
            Supplier<Optional<dev.vality.damsel.domain.PaymentMethod>> paymentMethod) {
        return paymentMethod.get()
                .filter(dev.vality.damsel.domain.PaymentMethod::isSetDigitalWallet)
                .map(dev.vality.damsel.domain.PaymentMethod::getDigitalWallet)
                .map(PaymentServiceRef::getId)
                .or(() -> paymentMethod.get()
                        .filter(dev.vality.damsel.domain.PaymentMethod::isSetDigitalWalletDeprecated)
                        .map(dev.vality.damsel.domain.PaymentMethod::getDigitalWalletDeprecated)
                        .map(Enum::name));
    }

    public static Optional<String> getPaymentMethodRefIdByCryptoCurrency(
            Supplier<Optional<dev.vality.damsel.domain.PaymentMethod>> paymentMethod) {
        return paymentMethod.get()
                .filter(dev.vality.damsel.domain.PaymentMethod::isSetCryptoCurrency)
                .map(dev.vality.damsel.domain.PaymentMethod::getCryptoCurrency)
                .map(CryptoCurrencyRef::getId)
                .or(() -> paymentMethod.get()
                        .filter(dev.vality.damsel.domain.PaymentMethod::isSetCryptoCurrencyDeprecated)
                        .map(dev.vality.damsel.domain.PaymentMethod::getCryptoCurrencyDeprecated)
                        .map(Enum::name));
    }

    public static Optional<String> getPaymentMethodRefIdByMobile(
            Supplier<Optional<dev.vality.damsel.domain.PaymentMethod>> paymentMethod) {
        return paymentMethod.get()
                .filter(dev.vality.damsel.domain.PaymentMethod::isSetMobile)
                .map(dev.vality.damsel.domain.PaymentMethod::getMobile)
                .map(MobileOperatorRef::getId)
                .or(() -> paymentMethod.get()
                        .filter(dev.vality.damsel.domain.PaymentMethod::isSetMobileDeprecated)
                        .map(dev.vality.damsel.domain.PaymentMethod::getMobileDeprecated)
                        .map(Enum::name));
    }
}
