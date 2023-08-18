package dev.vality.newway.handler.event.stock.impl.withdrawal;

import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.TestData;
import dev.vality.newway.config.PostgresqlJooqSpringBootITest;
import dev.vality.newway.dao.withdrawal.impl.FistfulCashFlowDaoImpl;
import dev.vality.newway.dao.withdrawal.impl.WithdrawalAdjustmentDaoImpl;
import dev.vality.newway.domain.enums.WithdrawalTransferStatus;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@PostgresqlJooqSpringBootITest
@ContextConfiguration(classes = {WithdrawalAdjustmentDaoImpl.class, FistfulCashFlowDaoImpl.class,
        WithdrawalAdjustmentTransferCreatedHandler.class,
        WithdrawalAdjustmentMachineEventCopyFactoryImpl.class,})
class WithdrawalAdjustmentTransferCreatedHandlerTest {

    @Autowired
    WithdrawalAdjustmentTransferCreatedHandler handler;

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
        TimestampedChange timestampedChange = TestData.createWithdrawalAdjustmentTransferCreatedChange(adjustmentId);

        MachineEvent event = TestData.createWithdrawalAdjustmentdMachineEvent(timestampedChange);
        event.setSourceId(withdrawalAdjustment.getWithdrawalId());

        handler.handle(timestampedChange, event);

        Result<WithdrawalAdjustmentRecord> recordNew = dslContext.fetch(WITHDRAWAL_ADJUSTMENT, WITHDRAWAL_ADJUSTMENT.CURRENT.eq(Boolean.TRUE));
        assertEquals(1, recordNew.size());
        assertEquals(WithdrawalTransferStatus.created, recordNew.get(0).getWithdrawalTransferStatus());
        assertNotNull(recordNew.get(0).getFee());
        assertNotNull(recordNew.get(0).getProviderFee());
        assertEquals(4, dslContext.fetchCount(FISTFUL_CASH_FLOW));
    }
}