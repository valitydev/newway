package dev.vality.newway.handler.event.stock.impl.withdrawal;

import dev.vality.fistful.withdrawal.TimestampedChange;
import dev.vality.fistful.withdrawal.adjustment.Adjustment;
import dev.vality.fistful.withdrawal.adjustment.ChangesPlan;
import dev.vality.fistful.withdrawal.adjustment.DataRevisionChangePlan;
import dev.vality.fistful.withdrawal.status.Status;
import dev.vality.geck.common.util.TBaseUtil;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.dao.withdrawal.iface.WithdrawalAdjustmentDao;
import dev.vality.newway.domain.enums.WithdrawalAdjustmentStatus;
import dev.vality.newway.domain.enums.WithdrawalAdjustmentType;
import dev.vality.newway.domain.enums.WithdrawalStatus;
import dev.vality.newway.domain.tables.pojos.WithdrawalAdjustment;
import dev.vality.newway.factory.machine.event.MachineEventCopyFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WithdrawalAdjustmentCreatedHandler implements WithdrawalHandler {

    private final WithdrawalAdjustmentDao withdrawalAdjustmentDao;
    private final MachineEventCopyFactory<WithdrawalAdjustment, String> machineEventCopyFactory;

    @Getter
    private final Filter filter =
            new PathConditionFilter(new PathConditionRule("change.adjustment.payload.created.adjustment", new IsNullCondition().not()));

    @Override
    public void handle(TimestampedChange timestampedChange, MachineEvent event) {
        Adjustment adjustmentDamsel = timestampedChange.getChange().getAdjustment().getPayload().getCreated().getAdjustment();
        long sequenceId = event.getEventId();
        String withdrawalId = event.getSourceId();
        String withdrawalAdjustmentId = adjustmentDamsel.getId();
        log.info("Start withdrawal adjustment created handling, sequenceId={}, withdrawalId={}, withdrawalAdjustmentId={}",
                sequenceId, withdrawalId, withdrawalAdjustmentId);

        WithdrawalAdjustment withdrawalAdjustment =
                machineEventCopyFactory.create(event, sequenceId, withdrawalAdjustmentId, timestampedChange.getOccuredAt());
        withdrawalAdjustment.setExternalId(adjustmentDamsel.getExternalId());
        withdrawalAdjustment.setStatus(WithdrawalAdjustmentStatus.pending);
        withdrawalAdjustment.setPartyRevision(adjustmentDamsel.getPartyRevision());
        withdrawalAdjustment.setWithdrawalId(withdrawalId);
        ChangesPlan changesPlan = adjustmentDamsel.getChangesPlan();
        if (changesPlan.isSetNewStatus()) {
            Status newStatus = changesPlan.getNewStatus().getNewStatus();
            withdrawalAdjustment.setType(WithdrawalAdjustmentType.status_change);
            withdrawalAdjustment.setWithdrawalStatus(TBaseUtil.unionFieldToEnum(newStatus, WithdrawalStatus.class));
        } else if (changesPlan.isSetNewDomainRevision()) {
            DataRevisionChangePlan newDomainRevision = changesPlan.getNewDomainRevision();
            withdrawalAdjustment.setType(WithdrawalAdjustmentType.domain_revision);
            withdrawalAdjustment.setDomainRevision(newDomainRevision.getNewDomainRevision());
        }
        withdrawalAdjustmentDao.save(withdrawalAdjustment).ifPresentOrElse(
                id -> log.info("withdrawalAdjustment created has been saved, sequenceId={}, withdrawalAdjustmentId={}",
                        sequenceId, withdrawalId),
                () -> log.info("withdrawalAdjustment created duplicated, sequenceId={}, withdrawalAdjustmentId={}",
                        sequenceId, withdrawalId));
    }

}
