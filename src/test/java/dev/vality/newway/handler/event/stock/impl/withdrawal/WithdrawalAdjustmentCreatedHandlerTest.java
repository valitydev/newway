package dev.vality.newway.handler.event.stock.impl.withdrawal;

import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.newway.TestData;
import dev.vality.newway.config.PostgresqlJooqSpringBootITest;
import dev.vality.newway.dao.withdrawal.impl.WithdrawalAdjustmentDaoImpl;
import dev.vality.newway.domain.tables.records.WithdrawalAdjustmentRecord;
import dev.vality.newway.factory.machine.event.WithdrawalAdjustmentMachineEventCopyFactoryImpl;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static dev.vality.newway.domain.tables.WithdrawalAdjustment.WITHDRAWAL_ADJUSTMENT;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@PostgresqlJooqSpringBootITest
@ContextConfiguration(classes = {WithdrawalAdjustmentDaoImpl.class, WithdrawalAdjustmentCreatedHandler.class,
        WithdrawalAdjustmentMachineEventCopyFactoryImpl.class,})
class WithdrawalAdjustmentCreatedHandlerTest {

    @Autowired
    WithdrawalAdjustmentCreatedHandler handler;

    @Autowired
    DSLContext dslContext;

    @BeforeEach
    void setUp() {
        dslContext.deleteFrom(WITHDRAWAL_ADJUSTMENT).execute();
    }

    @Test
    void handledStatusChange() {
        TimestampedChange timestampedChange = TestData.createWithdrawalAdjustmentCreatedStatusChange();

        handler.handle(timestampedChange, TestData.createWithdrawalAdjustmentdMachineEvent("id", timestampedChange));

        WithdrawalAdjustmentRecord record = dslContext.fetchAny(WITHDRAWAL_ADJUSTMENT);
        assertNotNull(record);
        assertNotNull(record.getNewWithdrawalStatus());
        assertNull(record.getNewDomainRevision());
    }

    @Test
    void handleDomainRevisionChange() {
        TimestampedChange timestampedChange = TestData.createWithdrawalAdjustmentCreatedDomainRevisionChange();

        handler.handle(timestampedChange, TestData.createWithdrawalAdjustmentdMachineEvent("id", timestampedChange));

        WithdrawalAdjustmentRecord record = dslContext.fetchAny(WITHDRAWAL_ADJUSTMENT);
        assertNotNull(record);
        assertNull(record.getNewWithdrawalStatus());
        assertNotNull(record.getNewDomainRevision());
    }
}
