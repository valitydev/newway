package com.rbkmoney.newway.util;

import com.rbkmoney.damsel.domain.FinalCashFlowAccount;
import com.rbkmoney.damsel.domain.FinalCashFlowPosting;
import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.newway.domain.enums.Adjustmentcashflowtype;
import com.rbkmoney.newway.domain.enums.Cashflowaccount;
import com.rbkmoney.newway.domain.enums.Paymentchangetype;
import com.rbkmoney.newway.domain.tables.pojos.CashFlow;

import java.util.List;
import java.util.stream.Collectors;

public class CashFlowUtil {
    public static Cashflowaccount getCashFlowAccountType(FinalCashFlowAccount cfa) {
        Cashflowaccount sourceAccountType = TypeUtil.toEnumField(cfa.getAccountType().getSetField().getFieldName(), Cashflowaccount.class);
        if (sourceAccountType == null) {
            throw new IllegalArgumentException("Illegal cash flow account type: " + cfa.getAccountType());
        }
        return sourceAccountType;
    }

    public static String getCashFlowAccountTypeValue(FinalCashFlowAccount cfa) {
        if (cfa.getAccountType().isSetMerchant()) {
            return cfa.getAccountType().getMerchant().name();
        } else if (cfa.getAccountType().isSetProvider()) {
            return cfa.getAccountType().getProvider().name();
        } else if (cfa.getAccountType().isSetSystem()) {
            return cfa.getAccountType().getSystem().name();
        } else if (cfa.getAccountType().isSetExternal()) {
            return cfa.getAccountType().getExternal().name();
        } else if (cfa.getAccountType().isSetWallet()) {
            return cfa.getAccountType().getWallet().name();
        } else {
            throw new IllegalArgumentException("Illegal cash flow account type: " + cfa.getAccountType());
        }
    }

    public static List<CashFlow> convertCashFlows(List<FinalCashFlowPosting> cashFlowPostings, long objId, Paymentchangetype paymentchangetype) {
        return convertCashFlows(cashFlowPostings, objId, paymentchangetype, null);
    }

    public static List<CashFlow> convertCashFlows(List<FinalCashFlowPosting> cashFlowPostings, long objId, Paymentchangetype paymentchangetype, Adjustmentcashflowtype adjustmentcashflowtype) {
        return cashFlowPostings.stream().map(cf -> {
            CashFlow pcf = new CashFlow();
            pcf.setObjId(objId);
            pcf.setObjType(paymentchangetype);
            pcf.setAdjFlowType(adjustmentcashflowtype);
            pcf.setSourceAccountType(CashFlowUtil.getCashFlowAccountType(cf.getSource()));
            pcf.setSourceAccountTypeValue(getCashFlowAccountTypeValue(cf.getSource()));
            pcf.setSourceAccountId(cf.getSource().getAccountId());
            pcf.setDestinationAccountType(CashFlowUtil.getCashFlowAccountType(cf.getDestination()));
            pcf.setDestinationAccountTypeValue(getCashFlowAccountTypeValue(cf.getDestination()));
            pcf.setDestinationAccountId(cf.getDestination().getAccountId());
            pcf.setAmount(cf.getVolume().getAmount());
            pcf.setCurrencyCode(cf.getVolume().getCurrency().getSymbolicCode());
            pcf.setDetails(cf.getDetails());
            return pcf;
        }).collect(Collectors.toList());
    }
}
