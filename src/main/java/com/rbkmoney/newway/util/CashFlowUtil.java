package com.rbkmoney.newway.util;

import dev.vality.damsel.domain.*;
import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.newway.domain.enums.AdjustmentCashFlowType;
import com.rbkmoney.newway.domain.enums.CashFlowAccount;
import com.rbkmoney.newway.domain.enums.PaymentChangeType;
import com.rbkmoney.newway.domain.tables.pojos.CashFlow;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CashFlowUtil {

    public static Long computeMerchantAmount(List<FinalCashFlowPosting> finalCashFlow) {
        long amountSource = computeMerchantAmount(finalCashFlow, FinalCashFlowPosting::getSource);
        long amountDest = computeMerchantAmount(finalCashFlow, FinalCashFlowPosting::getDestination);
        return amountDest - amountSource;
    }

    public static long computeMerchantAmount(List<FinalCashFlowPosting> finalCashFlow,
                                              Function<FinalCashFlowPosting, FinalCashFlowAccount> func) {
        return computeAmount(finalCashFlow, f -> isMerchantSettlement(func.apply(f).getAccountType()));
    }

    public static Long computeProviderAmount(List<FinalCashFlowPosting> finalCashFlow) {
        long amountSource = computeProviderAmount(finalCashFlow, FinalCashFlowPosting::getSource);
        long amountDest = computeProviderAmount(finalCashFlow, FinalCashFlowPosting::getDestination);
        return amountDest - amountSource;
    }

    public static long computeProviderAmount(List<FinalCashFlowPosting> finalCashFlow,
                                              Function<FinalCashFlowPosting, FinalCashFlowAccount> func) {
        return computeAmount(finalCashFlow, f -> isProviderSettlement(func.apply(f).getAccountType()));
    }

    public static Long computeSystemAmount(List<FinalCashFlowPosting> finalCashFlow) {
        long amountSource = computeSystemAmount(finalCashFlow, FinalCashFlowPosting::getSource);
        long amountDest = computeSystemAmount(finalCashFlow, FinalCashFlowPosting::getDestination);
        return amountDest - amountSource;
    }

    public static long computeSystemAmount(List<FinalCashFlowPosting> finalCashFlow,
                                            Function<FinalCashFlowPosting, FinalCashFlowAccount> func) {
        return computeAmount(finalCashFlow, f -> isSystemSettlement(func.apply(f).getAccountType()));
    }

    public static Long computeExternalIncomeAmount(List<FinalCashFlowPosting> finalCashFlow) {
        long amountSource = computeExternalIncomeAmount(finalCashFlow, FinalCashFlowPosting::getSource);
        long amountDest = computeExternalIncomeAmount(finalCashFlow, FinalCashFlowPosting::getDestination);
        return amountDest - amountSource;
    }

    public static long computeExternalIncomeAmount(List<FinalCashFlowPosting> finalCashFlow,
                                                    Function<FinalCashFlowPosting, FinalCashFlowAccount> func) {
        return computeAmount(finalCashFlow, f -> isExternalIncome(func.apply(f).getAccountType()));
    }

    public static Long computeExternalOutcomeAmount(List<FinalCashFlowPosting> finalCashFlow) {
        long amountSource = computeExternalOutcomeAmount(finalCashFlow, FinalCashFlowPosting::getSource);
        long amountDest = computeExternalOutcomeAmount(finalCashFlow, FinalCashFlowPosting::getDestination);
        return amountDest - amountSource;
    }

    public static long computeExternalOutcomeAmount(List<FinalCashFlowPosting> finalCashFlow,
                                                     Function<FinalCashFlowPosting, FinalCashFlowAccount> func) {
        return computeAmount(finalCashFlow, f -> isExternalOutcome(func.apply(f).getAccountType()));
    }

    private static long computeAmount(List<FinalCashFlowPosting> finalCashFlow,
                                      Predicate<FinalCashFlowPosting> filter) {
        return finalCashFlow.stream()
                .filter(filter)
                .mapToLong(cashFlow -> cashFlow.getVolume().getAmount())
                .sum();
    }

    private static boolean isMerchantSettlement(dev.vality.damsel.domain.CashFlowAccount cashFlowAccount) {
        return cashFlowAccount.isSetMerchant()
                && cashFlowAccount.getMerchant() == MerchantCashFlowAccount.settlement;
    }

    private static boolean isProviderSettlement(dev.vality.damsel.domain.CashFlowAccount cashFlowAccount) {
        return cashFlowAccount.isSetProvider()
                && cashFlowAccount.getProvider() == ProviderCashFlowAccount.settlement;
    }

    private static boolean isSystemSettlement(dev.vality.damsel.domain.CashFlowAccount cashFlowAccount) {
        return cashFlowAccount.isSetSystem()
                && cashFlowAccount.getSystem() == SystemCashFlowAccount.settlement;
    }

    private static boolean isExternalIncome(dev.vality.damsel.domain.CashFlowAccount cashFlowAccount) {
        return cashFlowAccount.isSetExternal()
                && cashFlowAccount.getExternal() == ExternalCashFlowAccount.income;
    }

    private static boolean isExternalOutcome(dev.vality.damsel.domain.CashFlowAccount cashFlowAccount) {
        return cashFlowAccount.isSetExternal()
                && cashFlowAccount.getExternal() == ExternalCashFlowAccount.outcome;
    }

    private static CashFlowAccount getCashFlowAccountType(FinalCashFlowAccount cfa) {
        CashFlowAccount sourceAccountType =
                TypeUtil.toEnumField(cfa.getAccountType().getSetField().getFieldName(), CashFlowAccount.class);
        if (sourceAccountType == null) {
            throw new IllegalArgumentException("Illegal cash flow account type: " + cfa.getAccountType());
        }
        return sourceAccountType;
    }

    private static String getCashFlowAccountTypeValue(FinalCashFlowAccount cfa) {
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

    public static List<CashFlow> convertCashFlows(List<FinalCashFlowPosting> cashFlowPostings, Long objId,
                                                  PaymentChangeType paymentChangeType) {
        return convertCashFlows(cashFlowPostings, objId, paymentChangeType, null);
    }

    public static List<CashFlow> convertCashFlows(List<FinalCashFlowPosting> cashFlowPostings, Long objId,
                                                  PaymentChangeType paymentChangeType,
                                                  AdjustmentCashFlowType adjustmentCashFlowType) {
        return cashFlowPostings.stream().map(cf -> {
            CashFlow pcf = new CashFlow();
            pcf.setObjId(objId);
            pcf.setObjType(paymentChangeType);
            pcf.setAdjFlowType(adjustmentCashFlowType);
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

    public static Map<CashFlowType, Long> parseCashFlow(List<FinalCashFlowPosting> finalCashFlow) {
        return parseCashFlow(finalCashFlow, CashFlowType::getCashFlowType);
    }

    private static Map<CashFlowType, Long> parseCashFlow(List<FinalCashFlowPosting> finalCashFlow,
                                                         Function<FinalCashFlowPosting, CashFlowType> classifier) {
        Map<CashFlowType, Long> collect = finalCashFlow.stream()
                .collect(
                        Collectors.groupingBy(
                                classifier,
                                Collectors.summingLong(cashFlow -> cashFlow.getVolume().getAmount()
                                )
                        )
                );
        return collect;
    }
}
