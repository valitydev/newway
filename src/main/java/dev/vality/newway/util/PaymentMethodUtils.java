package dev.vality.newway.util;

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
                .filter(PaymentMethod::isSetBankCard)
                .map(PaymentMethod::getBankCard)
                .flatMap(bankCard -> PaymentSystemUtil.getPaymentSystemNameIfPresent(
                        bankCard.getPaymentSystem(),
                        bankCard.getPaymentSystemDeprecated()))
                .or(() -> paymentMethod.get()
                        .filter(PaymentMethod::isSetBankCardDeprecated)
                        .map(PaymentMethod::getBankCardDeprecated)
                        .map(Enum::name))
                .or(() -> paymentMethod.get()
                        .filter(PaymentMethod::isSetEmptyCvvBankCardDeprecated)
                        .map(PaymentMethod::getEmptyCvvBankCardDeprecated)
                        .map(legacyBankCardPaymentSystem -> EMPTY_CVV + legacyBankCardPaymentSystem.name()))
                .or(() -> paymentMethod.get()
                        .filter(PaymentMethod::isSetTokenizedBankCardDeprecated)
                        .map(PaymentMethod::getTokenizedBankCardDeprecated)
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
            Supplier<Optional<PaymentMethod>> paymentMethod) {
        return paymentMethod.get()
                .filter(PaymentMethod::isSetPaymentTerminal)
                .map(PaymentMethod::getPaymentTerminal)
                .map(PaymentServiceRef::getId)
                .or(() -> paymentMethod.get()
                        .filter(PaymentMethod::isSetPaymentTerminalDeprecated)
                        .map(PaymentMethod::getPaymentTerminalDeprecated)
                        .map(Enum::name));
    }

    public static Optional<String> getPaymentMethodRefIdByDigitalWallet(
            Supplier<Optional<PaymentMethod>> paymentMethod) {
        return paymentMethod.get()
                .filter(PaymentMethod::isSetDigitalWallet)
                .map(PaymentMethod::getDigitalWallet)
                .map(PaymentServiceRef::getId)
                .or(() -> paymentMethod.get()
                        .filter(PaymentMethod::isSetDigitalWalletDeprecated)
                        .map(PaymentMethod::getDigitalWalletDeprecated)
                        .map(Enum::name));
    }

    public static Optional<String> getPaymentMethodRefIdByCryptoCurrency(
            Supplier<Optional<PaymentMethod>> paymentMethod) {
        return paymentMethod.get()
                .filter(PaymentMethod::isSetCryptoCurrency)
                .map(PaymentMethod::getCryptoCurrency)
                .map(CryptoCurrencyRef::getId)
                .or(() -> paymentMethod.get()
                        .filter(PaymentMethod::isSetCryptoCurrencyDeprecated)
                        .map(PaymentMethod::getCryptoCurrencyDeprecated)
                        .map(Enum::name));
    }

    public static Optional<String> getPaymentMethodRefIdByMobile(
            Supplier<Optional<PaymentMethod>> paymentMethod) {
        return paymentMethod.get()
                .filter(PaymentMethod::isSetMobile)
                .map(PaymentMethod::getMobile)
                .map(MobileOperatorRef::getId)
                .or(() -> paymentMethod.get()
                        .filter(PaymentMethod::isSetMobileDeprecated)
                        .map(PaymentMethod::getMobileDeprecated)
                        .map(Enum::name));
    }

    public static Optional<String> getPaymentMethodRefIdByGeneric(
            Supplier<Optional<PaymentMethod>> paymentMethod) {
        return paymentMethod.get()
                .filter(PaymentMethod::isSetGeneric)
                .map(PaymentMethod::getGeneric)
                .map(GenericPaymentMethod::getPaymentService)
                .map(PaymentServiceRef::getId);
    }
}
