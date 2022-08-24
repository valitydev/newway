package dev.vality.newway.handler.event.stock.impl.limiter;

import dev.vality.geck.common.util.TBaseUtil;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.limiter.config.LimitTurnoverMetric;
import dev.vality.limiter.config.LimitType;
import dev.vality.limiter.config.TimestampedChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.dao.limiter.LimitConfigDao;
import dev.vality.newway.domain.enums.*;
import dev.vality.newway.domain.tables.pojos.LimitConfig;
import dev.vality.newway.factory.MachineEventCopyFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class LimitConfigCreatedHandler implements LimitConfigHandler {

    private final LimitConfigDao limitConfigDao;
    private final MachineEventCopyFactory<LimitConfig, String> limitConfigMachineEventCopyFactory;

    @Getter
    private final Filter filter = new PathConditionFilter(new PathConditionRule(
            "change.created",
            new IsNullCondition().not()));

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(TimestampedChange timestampedChange, MachineEvent event) {
        var change = timestampedChange.getChange();
        var limitConfigSource = change.getCreated().getLimitConfig();
        var limitConfigId = limitConfigSource.getId();
        var sequenceId = event.getEventId();
        var sourceId = event.getSourceId();
        log.info("Start LimitConfig created handling, sequenceId={}, limitConfigId={}", sequenceId, limitConfigId);
        var limitConfig = limitConfigMachineEventCopyFactory.create(
                event, sequenceId, sourceId, timestampedChange.getOccuredAt());
        limitConfig.setLimitConfigId(limitConfigId);
        limitConfig.setProcessorType(limitConfigSource.getProcessorType());
        limitConfig.setCreatedAt(TypeUtil.stringToLocalDateTime(limitConfigSource.getCreatedAt()));
        limitConfig.setStartedAt(TypeUtil.stringToLocalDateTime(limitConfigSource.getStartedAt()));
        limitConfig.setShardSize(limitConfigSource.getShardSize());
        if (limitConfigSource.isSetTimeRangeType()) {
            limitConfig.setTimeRangeType(TBaseUtil.unionFieldToEnum(
                    limitConfigSource.getTimeRangeType(), LimitConfigTimeRangeType.class));
            switch (limitConfigSource.getTimeRangeType().getSetField()) {
                case CALENDAR -> limitConfig.setTimeRangeTypeCalendar(TBaseUtil.unionFieldToEnum(
                        limitConfigSource.getTimeRangeType().getCalendar(),
                        LimitConfigTimeRangeTypeCalendar.class));
                case INTERVAL -> limitConfig.setTimeRangeTypeIntervalAmount(
                        limitConfigSource.getTimeRangeType().getInterval().getAmount());
            }
        }
        if (limitConfigSource.isSetContextType()) {
            limitConfig.setLimitContextType(TBaseUtil.unionFieldToEnum(
                    limitConfigSource.getContextType(), LimitConfigLimitContextType.class));
        }
        if (limitConfigSource.isSetType()) {
            if (limitConfigSource.getType().getSetField() == LimitType._Fields.TURNOVER) {
                if (limitConfigSource.getType().getTurnover().isSetMetric()) {
                    limitConfig.setLimitTypeTurnoverMetric(TBaseUtil.unionFieldToEnum(
                            limitConfigSource.getType().getTurnover().getMetric(),
                            LimitConfigLimitTypeTurnoverMetric.class));
                    if (limitConfigSource.getType().getTurnover().getMetric().getSetField()
                            == LimitTurnoverMetric._Fields.AMOUNT) {
                        limitConfig.setLimitTypeTurnoverMetricAmountCurrency(
                                limitConfigSource.getType().getTurnover().getMetric().getAmount().getCurrency());
                    }
                }
            }
        }
        if (limitConfigSource.isSetScope()) {
            limitConfig.setLimitScope(TBaseUtil.unionFieldToEnum(
                    limitConfigSource.getScope(), LimitConfigLimitScope.class));
            switch (limitConfigSource.getScope().getSetField()) {
                case SINGLE -> limitConfig.setLimitScopeTypes(TBaseUtil.unionFieldToEnum(
                        limitConfigSource.getScope().getSingle(), LimitConfigLimitScopeType.class).getLiteral());
                case MULTI -> limitConfig.setLimitScopeTypes(limitConfigSource.getScope().getMulti().stream()
                        .map(limitScopeType -> TBaseUtil.unionFieldToEnum(
                                limitScopeType, LimitConfigLimitScopeType.class).getLiteral())
                        .collect(Collectors.joining(", ")));
            }
        }
        limitConfig.setDescription(limitConfigSource.getDescription());
        if (limitConfigSource.isSetOpBehaviour() && limitConfigSource.getOpBehaviour().isSetInvoicePaymentRefund()) {
            limitConfig.setOperationLimitBehaviour(TBaseUtil.unionFieldToEnum(
                    limitConfigSource.getOpBehaviour().getInvoicePaymentRefund(),
                    LimitConfigOperationLimitBehaviour.class));
        }
        if (limitConfigDao.save(limitConfig).isEmpty()) {
            log.info("limitConfig has been bound duplicated, limitConfigId={}", limitConfigId);
        }
    }
}
