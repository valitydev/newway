package dev.vality.newway.handler.event.stock.impl.source;

import dev.vality.fistful.account.Account;
import dev.vality.fistful.source.Change;
import dev.vality.fistful.source.TimestampedChange;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.dao.identity.iface.IdentityDao;
import dev.vality.newway.dao.source.iface.SourceDao;
import dev.vality.newway.domain.tables.pojos.Identity;
import dev.vality.newway.domain.tables.pojos.Source;
import dev.vality.newway.factory.machine.event.MachineEventCopyFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SourceAccountCreatedHandler implements SourceHandler {

    private final SourceDao sourceDao;
    private final IdentityDao identityDao;
    private final MachineEventCopyFactory<Source, String> sourceMachineEventCopyFactory;

    @Getter
    private final Filter filter =
            new PathConditionFilter(new PathConditionRule("change.account.created", new IsNullCondition().not()));

    @Override
    public void handle(TimestampedChange timestampedChange, MachineEvent event) {
        Change change = timestampedChange.getChange();
        Account account = change.getAccount().getCreated();
        long sequenceId = event.getEventId();
        String sourceId = event.getSourceId();
        log.info("Start source account created handling, sequenceId={}, sourceId={}", sequenceId, sourceId);
        final Source sourceOld = sourceDao.get(sourceId);
        Identity identity = identityDao.get(account.getIdentity());

        Source sourceNew = sourceMachineEventCopyFactory
                .create(event, sequenceId, sourceId, sourceOld, timestampedChange.getOccuredAt());

        sourceNew.setAccountId(account.getId());
        sourceNew.setIdentityId(account.getIdentity());
        sourceNew.setPartyId(identity.getPartyId());
        sourceNew.setAccounterAccountId(account.getAccounterAccountId());
        sourceNew.setCurrencyCode(account.getCurrency().getSymbolicCode());

        sourceDao.save(sourceNew).ifPresentOrElse(
                id -> {
                    sourceDao.updateNotCurrent(sourceOld.getId());
                    log.info("Source account have been changed, sequenceId={}, sourceId={}", sequenceId, sourceId);
                },
                () -> log.info("Source account have been saved, sequenceId={}, sourceId={}", sequenceId, sourceId));
    }


}
