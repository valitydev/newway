package dev.vality.newway.handler.event.stock.impl.withdrawal;

import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.newway.TestData;
import dev.vality.newway.config.PostgresqlJooqSpringBootITest;
import dev.vality.newway.dao.withdrawal.impl.WithdrawalAdjustmentDaoImpl;
import dev.vality.newway.domain.enums.WithdrawalAdjustmentStatus;
import dev.vality.newway.domain.tables.pojos.WithdrawalAdjustment;
import dev.vality.newway.domain.tables.records.WithdrawalAdjustmentRecord;
import dev.vality.newway.factory.machine.event.WithdrawalAdjustmentMachineEventCopyFactoryImpl;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static dev.vality.newway.domain.tables.WithdrawalAdjustment.WITHDRAWAL_ADJUSTMENT;
import static org.junit.jupiter.api.Assertions.assertEquals;

@PostgresqlJooqSpringBootITest
@ContextConfiguration(classes = {WithdrawalAdjustmentDaoImpl.class, WithdrawalAdjustmentStatusChangedHandler.class,
        WithdrawalAdjustmentMachineEventCopyFactoryImpl.class,})
class WithdrawalAdjustmentStatusChangedHandlerTest {

    @Autowired
    WithdrawalAdjustmentStatusChangedHandler handler;

    @Autowired
    DSLContext dslContext;

    @BeforeEach
    void setUp() {
        dslContext.deleteFrom(WITHDRAWAL_ADJUSTMENT).execute();
    }

    @Test
    void handle() {
        String adjustmentId = "adjustment_id";
        WithdrawalAdjustment withdrawalAdjustment = TestData.createWithdrawalAdjustment(adjustmentId);
        dslContext.insertInto(WITHDRAWAL_ADJUSTMENT)
                .set(dslContext.newRecord(WITHDRAWAL_ADJUSTMENT, withdrawalAdjustment))
                .execute();
        TimestampedChange timestampedChange = TestData.createWithdrawalAdjustmentStatusChange(adjustmentId);

        handler.handle(timestampedChange, TestData.createWithdrawalAdjustmentdMachineEvent(timestampedChange));

        Result<WithdrawalAdjustmentRecord> recordNew = dslContext.fetch(WITHDRAWAL_ADJUSTMENT, WITHDRAWAL_ADJUSTMENT.CURRENT.eq(Boolean.TRUE));
        assertEquals(1, recordNew.size());
        assertEquals(WithdrawalAdjustmentStatus.succeeded, recordNew.get(0).getStatus());
        Result<WithdrawalAdjustmentRecord> recordOld = dslContext.fetch(WITHDRAWAL_ADJUSTMENT, WITHDRAWAL_ADJUSTMENT.CURRENT.eq(Boolean.FALSE));
        assertEquals(1, recordOld.size());
        assertEquals(WithdrawalAdjustmentStatus.pending, recordOld.get(0).getStatus());
    }
}
