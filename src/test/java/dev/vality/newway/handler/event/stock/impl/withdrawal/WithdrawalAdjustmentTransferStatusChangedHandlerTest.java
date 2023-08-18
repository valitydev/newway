package dev.vality.newway.handler.event.stock.impl.withdrawal;

import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.TestData;
import dev.vality.newway.config.PostgresqlJooqSpringBootITest;
import dev.vality.newway.dao.withdrawal.impl.FistfulCashFlowDaoImpl;
import dev.vality.newway.dao.withdrawal.impl.WithdrawalAdjustmentDaoImpl;
import dev.vality.newway.domain.enums.WithdrawalTransferStatus;
import dev.vality.newway.domain.tables.pojos.FistfulCashFlow;
import dev.vality.newway.domain.tables.pojos.WithdrawalAdjustment;
import dev.vality.newway.domain.tables.records.WithdrawalAdjustmentRecord;
import dev.vality.newway.factory.machine.event.WithdrawalAdjustmentMachineEventCopyFactoryImpl;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static dev.vality.newway.domain.tables.FistfulCashFlow.FISTFUL_CASH_FLOW;
import static dev.vality.newway.domain.tables.WithdrawalAdjustment.WITHDRAWAL_ADJUSTMENT;
import static org.junit.jupiter.api.Assertions.assertEquals;

@PostgresqlJooqSpringBootITest
@ContextConfiguration(classes = {WithdrawalAdjustmentDaoImpl.class, FistfulCashFlowDaoImpl.class,
        WithdrawalAdjustmentTransferStatusChangedHandler.class,
        WithdrawalAdjustmentMachineEventCopyFactoryImpl.class,})
class WithdrawalAdjustmentTransferStatusChangedHandlerTest {

    @Autowired
    WithdrawalAdjustmentTransferStatusChangedHandler handler;

    @Autowired
    DSLContext dslContext;

    @BeforeEach
    void setUp() {
        dslContext.deleteFrom(WITHDRAWAL_ADJUSTMENT).execute();
        dslContext.deleteFrom(FISTFUL_CASH_FLOW).execute();
    }

    @Test
    void handle() {
        String adjustmentId = "adjustment_id";
        WithdrawalAdjustment withdrawalAdjustment = TestData.createWithdrawalAdjustment(adjustmentId);
        dslContext.insertInto(WITHDRAWAL_ADJUSTMENT)
                .set(dslContext.newRecord(WITHDRAWAL_ADJUSTMENT, withdrawalAdjustment))
                .execute();
        WithdrawalAdjustmentRecord withdrawalAdjustmentRecord = dslContext.fetchAny(WITHDRAWAL_ADJUSTMENT);
        FistfulCashFlow fistfulCashFlow = TestData.createFistfulCashFlow();
        fistfulCashFlow.setObjId(withdrawalAdjustmentRecord.getId());
        dslContext.insertInto(FISTFUL_CASH_FLOW)
                .set(dslContext.newRecord(FISTFUL_CASH_FLOW, fistfulCashFlow))
                .execute();
        TimestampedChange timestampedChange = TestData.createWithdrawalAdjustmentTransferStatusChange(adjustmentId);
        MachineEvent event = TestData.createWithdrawalAdjustmentdMachineEvent(timestampedChange);
        event.setSourceId(withdrawalAdjustment.getWithdrawalId());

        handler.handle(timestampedChange, event);

        Result<WithdrawalAdjustmentRecord> recordNew = dslContext.fetch(WITHDRAWAL_ADJUSTMENT, WITHDRAWAL_ADJUSTMENT.CURRENT.eq(Boolean.TRUE));
        assertEquals(1, recordNew.size());
        assertEquals(WithdrawalTransferStatus.committed, recordNew.get(0).getWithdrawalTransferStatus());
        assertEquals(2, dslContext.fetchCount(FISTFUL_CASH_FLOW));
    }
}