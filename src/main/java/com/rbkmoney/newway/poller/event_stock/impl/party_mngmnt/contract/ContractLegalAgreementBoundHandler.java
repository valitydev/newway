package com.rbkmoney.newway.poller.event_stock.impl.party_mngmnt.contract;

import com.rbkmoney.damsel.domain.LegalAgreement;
import com.rbkmoney.damsel.payment_processing.ClaimEffect;
import com.rbkmoney.damsel.payment_processing.ContractEffectUnit;
import com.rbkmoney.damsel.payment_processing.PartyChange;
import com.rbkmoney.machinegun.eventsink.MachineEvent;
import com.rbkmoney.newway.dao.party.iface.ContractDao;
import com.rbkmoney.newway.domain.tables.pojos.Contract;
import com.rbkmoney.newway.poller.event_stock.impl.party_mngmnt.AbstractClaimChangedHandler;
import com.rbkmoney.newway.service.ContractReferenceService;
import com.rbkmoney.newway.util.ContractUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContractLegalAgreementBoundHandler extends AbstractClaimChangedHandler {

    private final ContractDao contractDao;
    private final ContractReferenceService contractReferenceService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(PartyChange change, MachineEvent event, Integer changeId) {
        long sequenceId = event.getEventId();
        getClaimStatus(change).getAccepted().getEffects().stream()
                .filter(claimEffect -> claimEffect.isSetContractEffect() && claimEffect.getContractEffect().getEffect().isSetLegalAgreementBound())
                .forEach(claimEffect -> handleEvent(event, changeId, sequenceId, claimEffect));
    }

    private void handleEvent(MachineEvent event, Integer changeId, long sequenceId, ClaimEffect claimEffect) {
        ContractEffectUnit contractEffectUnit = claimEffect.getContractEffect();
        LegalAgreement legalAgreementBound = contractEffectUnit.getEffect().getLegalAgreementBound();
        String contractId = contractEffectUnit.getContractId();
        String partyId = event.getSourceId();
        log.info("Start contract legal agreement bound handling, sequenceId={}, partyId={}, contractId={}, changeId={}",
                sequenceId, partyId, contractId, changeId);

        Contract contractSource = contractDao.get(partyId, contractId);
        Long contractSourceId = contractSource.getId();
        ContractUtil.resetBaseFields(event, changeId, sequenceId, contractSource);
        ContractUtil.fillContractLegalAgreementFields(contractSource, legalAgreementBound);

        contractDao.save(contractSource).ifPresentOrElse(
                dbContractId -> {
                    contractDao.updateNotCurrent(contractSourceId);
                    contractReferenceService.updateContractReference(contractSourceId, dbContractId);
                    log.info("Contract legal agreement bound has been saved, sequenceId={}, partyId={}, contractId={}, changeId={}",
                            sequenceId, partyId, contractId, changeId);
                },
                () -> log.info("Contract legal agreement bound duplicated, sequenceId={}, partyId={}, contractId={}, changeId={}",
                        sequenceId, partyId, contractId, changeId)
        );
    }
}
