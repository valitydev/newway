package dev.vality.newway.handler.event.stock.impl.withdrawal;

import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.fistful.withdrawal.adjustment.Status;
import dev.vality.geck.common.util.TBaseUtil;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.dao.withdrawal.iface.WithdrawalAdjustmentDao;
import dev.vality.newway.domain.enums.WithdrawalAdjustmentStatus;
import dev.vality.newway.domain.tables.pojos.WithdrawalAdjustment;
import dev.vality.newway.factory.machine.event.MachineEventCopyFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class WithdrawalAdjustmentStatusChangedHandler implements WithdrawalHandler {

    private final WithdrawalAdjustmentDao withdrawalAdjustmentDao;
    private final MachineEventCopyFactory<WithdrawalAdjustment, String> machineEventCopyFactory;

    @Getter
    private final Filter filter =
            new PathConditionFilter(new PathConditionRule("change.adjustment.payload.status_changed.status", new IsNullCondition().not()));

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(TimestampedChange timestampedChange, MachineEvent event) {
        Status status = timestampedChange.getChange().getAdjustment().getPayload().getStatusChanged().getStatus();
        long sequenceId = event.getEventId();
        String withdrawalAdjustmentId = event.getSourceId();
        log.info("Start withdrawal adjustment status changed handling, sequenceId={}, withdrawalAdjustmentId={}", sequenceId,
                withdrawalAdjustmentId);
        final var withdrawalAdjustmentOld = withdrawalAdjustmentDao.getById(withdrawalAdjustmentId);
        var withdrawalAdjustmentNew = machineEventCopyFactory
                .create(event, sequenceId, withdrawalAdjustmentId, withdrawalAdjustmentOld, timestampedChange.getOccuredAt());
        withdrawalAdjustmentNew.setStatus(TBaseUtil.unionFieldToEnum(status, WithdrawalAdjustmentStatus.class));
        withdrawalAdjustmentDao.save(withdrawalAdjustmentNew).ifPresentOrElse(
                id -> {
                    Long oldId = withdrawalAdjustmentOld.getId();
                    withdrawalAdjustmentDao.updateNotCurrent(oldId);
                    log.info("WithdrawalAdjustment status have been changed, sequenceId={}, withdrawalId={}",
                            sequenceId, withdrawalAdjustmentId);
                },
                () -> log.info("WithdrawalAdjustment status have been changed, sequenceId={}, withdrawalId={}",
                        sequenceId, withdrawalAdjustmentId));
    }

}
