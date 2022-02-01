package com.rbkmoney.newway.util;

import dev.vality.damsel.domain.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.rbkmoney.newway.TestData.*;
import static com.rbkmoney.newway.TestData.createMerchantAccount;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class AdjustmentUtilsTest {

    @Test
    public void calculateMerchantAmountDiffTest() {
        List<FinalCashFlowPosting> oldCashFlow = createOldCashFlow(createMerchantAccount());
        long oldAmountSource = CashFlowUtil.computeMerchantAmount(oldCashFlow, FinalCashFlowPosting::getSource);
        long oldAmountDest = CashFlowUtil.computeMerchantAmount(oldCashFlow, FinalCashFlowPosting::getDestination);
        assertEquals(30, oldAmountSource);
        assertEquals(15, oldAmountDest);
        assertEquals(-15, CashFlowUtil.computeMerchantAmount(oldCashFlow));

        List<FinalCashFlowPosting> newCashFlow = createNewCashFlow(createMerchantAccount());
        long newAmountSource = CashFlowUtil.computeMerchantAmount(newCashFlow, FinalCashFlowPosting::getSource);
        long newAmountDest = CashFlowUtil.computeMerchantAmount(newCashFlow, FinalCashFlowPosting::getDestination);
        assertEquals(32, newAmountSource);
        assertEquals(13, newAmountDest);
        assertEquals(-19, CashFlowUtil.computeMerchantAmount(newCashFlow));

        InvoicePaymentAdjustment adjustment = createTestInvoicePaymentAdjustment(oldCashFlow, newCashFlow);
        assertEquals(-34, AdjustmentUtils.calculateMerchantAmountDiff(adjustment));
    }

    @Test
    public void calculateProviderAmountDiffTest() {
        List<FinalCashFlowPosting> oldCashFlow = createOldCashFlow(createProviderAccount());
        long oldAmountSource = CashFlowUtil.computeProviderAmount(oldCashFlow, FinalCashFlowPosting::getSource);
        long oldAmountDest = CashFlowUtil.computeProviderAmount(oldCashFlow, FinalCashFlowPosting::getDestination);
        assertEquals(30, oldAmountSource);
        assertEquals(15, oldAmountDest);
        assertEquals(-15, CashFlowUtil.computeProviderAmount(oldCashFlow));

        List<FinalCashFlowPosting> newCashFlow = createNewCashFlow(createProviderAccount());
        long newAmountSource = CashFlowUtil.computeProviderAmount(newCashFlow, FinalCashFlowPosting::getSource);
        long newAmountDest = CashFlowUtil.computeProviderAmount(newCashFlow, FinalCashFlowPosting::getDestination);
        assertEquals(32, newAmountSource);
        assertEquals(13, newAmountDest);
        assertEquals(-19, CashFlowUtil.computeProviderAmount(newCashFlow));

        InvoicePaymentAdjustment adjustment = createTestInvoicePaymentAdjustment(oldCashFlow, newCashFlow);
        assertEquals(-34, AdjustmentUtils.calculateProviderAmountDiff(adjustment));
    }

    @Test
    public void calculateSystemAmountDiffTest() {
        List<FinalCashFlowPosting> oldCashFlow = createOldCashFlow(createSystemAccount());
        long oldAmountSource = CashFlowUtil.computeSystemAmount(oldCashFlow, FinalCashFlowPosting::getSource);
        long oldAmountDest = CashFlowUtil.computeSystemAmount(oldCashFlow, FinalCashFlowPosting::getDestination);
        assertEquals(30, oldAmountSource);
        assertEquals(15, oldAmountDest);
        assertEquals(-15, CashFlowUtil.computeSystemAmount(oldCashFlow));

        List<FinalCashFlowPosting> newCashFlow = createNewCashFlow(createSystemAccount());
        long newAmountSource = CashFlowUtil.computeSystemAmount(newCashFlow, FinalCashFlowPosting::getSource);
        long newAmountDest = CashFlowUtil.computeSystemAmount(newCashFlow, FinalCashFlowPosting::getDestination);
        assertEquals(32, newAmountSource);
        assertEquals(13, newAmountDest);
        assertEquals(-19, CashFlowUtil.computeSystemAmount(newCashFlow));

        InvoicePaymentAdjustment adjustment = createTestInvoicePaymentAdjustment(oldCashFlow, newCashFlow);
        assertEquals(-34, AdjustmentUtils.calculateSystemAmountDiff(adjustment));
    }

    @Test
    public void calculateExternalIncomeAmountDiffTest() {
        List<FinalCashFlowPosting> oldCashFlow = createOldCashFlow(createExternalIncomeAccount());
        long oldAmountSource = CashFlowUtil.computeExternalIncomeAmount(oldCashFlow, FinalCashFlowPosting::getSource);
        long oldAmountDest =
                CashFlowUtil.computeExternalIncomeAmount(oldCashFlow, FinalCashFlowPosting::getDestination);
        assertEquals(30, oldAmountSource);
        assertEquals(15, oldAmountDest);
        assertEquals(-15, CashFlowUtil.computeExternalIncomeAmount(oldCashFlow));

        List<FinalCashFlowPosting> newCashFlow = createNewCashFlow(createExternalIncomeAccount());
        long newAmountSource = CashFlowUtil.computeExternalIncomeAmount(newCashFlow, FinalCashFlowPosting::getSource);
        long newAmountDest =
                CashFlowUtil.computeExternalIncomeAmount(newCashFlow, FinalCashFlowPosting::getDestination);
        assertEquals(32, newAmountSource);
        assertEquals(13, newAmountDest);
        assertEquals(-19, CashFlowUtil.computeExternalIncomeAmount(newCashFlow));

        InvoicePaymentAdjustment adjustment = createTestInvoicePaymentAdjustment(oldCashFlow, newCashFlow);
        assertEquals(-34, AdjustmentUtils.calculateExternalIncomeAmountDiff(adjustment));
    }

    @Test
    public void calculateExternalOutcomeAmountDiffTest() {
        List<FinalCashFlowPosting> oldCashFlow = createOldCashFlow(createExternalOutcomeAccount());
        long oldAmountSource = CashFlowUtil.computeExternalOutcomeAmount(oldCashFlow, FinalCashFlowPosting::getSource);
        long oldAmountDest =
                CashFlowUtil.computeExternalOutcomeAmount(oldCashFlow, FinalCashFlowPosting::getDestination);
        assertEquals(30, oldAmountSource);
        assertEquals(15, oldAmountDest);
        assertEquals(-15, CashFlowUtil.computeExternalOutcomeAmount(oldCashFlow));

        List<FinalCashFlowPosting> newCashFlow = createNewCashFlow(createExternalOutcomeAccount());
        long newAmountSource = CashFlowUtil.computeExternalOutcomeAmount(newCashFlow, FinalCashFlowPosting::getSource);
        long newAmountDest =
                CashFlowUtil.computeExternalOutcomeAmount(newCashFlow, FinalCashFlowPosting::getDestination);
        assertEquals(32, newAmountSource);
        assertEquals(13, newAmountDest);
        assertEquals(-19, CashFlowUtil.computeExternalOutcomeAmount(newCashFlow));

        InvoicePaymentAdjustment adjustment = createTestInvoicePaymentAdjustment(oldCashFlow, newCashFlow);
        assertEquals(-34, AdjustmentUtils.calculateExternalOutcomeAmountDiff(adjustment));
    }

    private static List<FinalCashFlowPosting> createOldCashFlow(FinalCashFlowAccount account) {
        List<FinalCashFlowPosting> oldCashFlow = new ArrayList<>();
        oldCashFlow.add(new FinalCashFlowPosting(createSubagentAccount(), account, createTestCash(5)));
        oldCashFlow.add(new FinalCashFlowPosting(createSubagentAccount(), account, createTestCash(10)));
        oldCashFlow.add(new FinalCashFlowPosting(account, createSubagentAccount(), createTestCash(13)));
        oldCashFlow.add(new FinalCashFlowPosting(account, createSubagentAccount(), createTestCash(17)));
        return oldCashFlow;
    }

    private static List<FinalCashFlowPosting> createNewCashFlow(FinalCashFlowAccount account) {
        List<FinalCashFlowPosting> oldCashFlow = new ArrayList<>();
        oldCashFlow.add(new FinalCashFlowPosting(createSubagentAccount(), account, createTestCash(4)));
        oldCashFlow.add(new FinalCashFlowPosting(createSubagentAccount(), account, createTestCash(9)));
        oldCashFlow.add(new FinalCashFlowPosting(account, createSubagentAccount(), createTestCash(14)));
        oldCashFlow.add(new FinalCashFlowPosting(account, createSubagentAccount(), createTestCash(18)));
        return oldCashFlow;
    }

}
