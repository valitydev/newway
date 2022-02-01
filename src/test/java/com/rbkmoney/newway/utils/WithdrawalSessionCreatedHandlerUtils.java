package com.rbkmoney.newway.utils;

import dev.vality.fistful.base.*;
import dev.vality.fistful.withdrawal_session.Change;
import dev.vality.fistful.withdrawal_session.Route;
import dev.vality.fistful.withdrawal_session.TimestampedChange;
import dev.vality.fistful.withdrawal_session.Withdrawal;
import dev.vality.kafka.common.serialization.ThriftSerializer;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.machinegun.msgpack.Value;

public class WithdrawalSessionCreatedHandlerUtils {

    public static final String WITHDRAWAL_ID = "withdrawal_id";
    public static final String SESSION_ID = "session_id";

    public static final int PROVIDER_ID = 1;
    public static final int TERMINAL_ID = 1;

    public static final String DIGITAL_WALLET_ID = "digital_wallet_id";
    public static final String CRYPTO_WALLET_ID = "crypto_wallet_id";

    public static final String CARD_BIN = "bin";
    public static final String CARD_MASKED_PAN = "1232132";
    public static final String CARD_TOKEN_PROVIDER = "cardToken";

    public static MachineEvent createCreatedMachineEvent(String id,
                                                         dev.vality.fistful.withdrawal_session.Session session) {
        return new MachineEvent()
                .setEventId(2L)
                .setSourceId(id)
                .setSourceNs("2")
                .setCreatedAt("2021-05-31T06:12:27Z")
                .setData(Value.bin(new ThriftSerializer<>().serialize("", createCreated(session))));
    }

    public static TimestampedChange createCreated(dev.vality.fistful.withdrawal_session.Session session) {
        return new TimestampedChange()
                .setOccuredAt("2021-05-31T06:12:27Z")
                .setChange(Change.created(session));
    }

    public static dev.vality.fistful.base.DigitalWallet createFistfulDigitalWallet() {
        dev.vality.fistful.base.DigitalWallet digitalWallet = new dev.vality.fistful.base.DigitalWallet();
        digitalWallet.setId(DIGITAL_WALLET_ID);
        digitalWallet.setPaymentService(new PaymentServiceRef("webmoney"));
        return digitalWallet;
    }

    public static dev.vality.fistful.base.CryptoWallet createFistfulCryptoWallet() {
        dev.vality.fistful.base.CryptoWallet cryptoWallet = new dev.vality.fistful.base.CryptoWallet();
        cryptoWallet.setId(CRYPTO_WALLET_ID);
        cryptoWallet.setData(CryptoData.bitcoin(new CryptoDataBitcoin()));
        cryptoWallet.setCurrency(CryptoCurrency.bitcoin);
        return cryptoWallet;
    }

    public static dev.vality.fistful.base.BankCard createFistfulBankCard() {
        dev.vality.fistful.base.BankCard bankCard = new dev.vality.fistful.base.BankCard();
        bankCard.setToken(CARD_TOKEN_PROVIDER);
        bankCard.setBin(CARD_BIN);
        bankCard.setMaskedPan(CARD_MASKED_PAN);
        return bankCard;
    }

    public static ResourceDigitalWallet createDestinationResourceDigitalWallet() {
        ResourceDigitalWallet resourceDigitalWallet = new ResourceDigitalWallet();
        resourceDigitalWallet.setDigitalWallet(createFistfulDigitalWallet());
        return resourceDigitalWallet;
    }

    public static ResourceCryptoWallet createDestinationResourceCryptoWallet() {
        ResourceCryptoWallet resourceCryptoWallet = new ResourceCryptoWallet();
        resourceCryptoWallet.setCryptoWallet(createFistfulCryptoWallet());
        return resourceCryptoWallet;
    }

    public static ResourceBankCard createDestinationResourceBankCard() {
        ResourceBankCard resourceBankCard = new ResourceBankCard();
        resourceBankCard.setBankCard(createFistfulBankCard());
        return resourceBankCard;
    }

    public static dev.vality.fistful.withdrawal_session.Session createSession(Resource resource) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setId(WITHDRAWAL_ID);
        withdrawal.setDestinationResource(resource);

        dev.vality.fistful.base.Cash cash = new dev.vality.fistful.base.Cash();
        cash.setAmount(11L);
        cash.setCurrency(new dev.vality.fistful.base.CurrencyRef("RUB"));
        withdrawal.setCash(cash);
        dev.vality.fistful.withdrawal_session.Session session = new dev.vality.fistful.withdrawal_session.Session();
        session.setId(SESSION_ID);
        session.setWithdrawal(withdrawal);
        session.setRoute(createRoute());
        return session;
    }

    public static Route createRoute() {
        Route route = new Route();
        route.setProviderId(PROVIDER_ID);
        route.setTerminalId(TERMINAL_ID);
        return route;
    }

}
