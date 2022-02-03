package dev.vality.newway.handler.event.stock.impl.partymngmnt.contract;

import dev.vality.damsel.domain.LegalAgreement;
import dev.vality.damsel.payment_processing.ClaimEffect;
import dev.vality.damsel.payment_processing.ContractEffectUnit;
import dev.vality.damsel.payment_processing.PartyChange;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.dao.party.iface.ContractDao;
import dev.vality.newway.domain.tables.pojos.Contract;
import dev.vality.newway.factory.claim.effect.ClaimEffectCopyFactory;
import dev.vality.newway.handler.event.stock.impl.partymngmnt.AbstractClaimChangedHandler;
import dev.vality.newway.service.ContractReferenceService;
import dev.vality.newway.util.ContractUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContractLegalAgreementBoundHandler extends AbstractClaimChangedHandler {

    private final ContractDao contractDao;
    private final ContractReferenceService contractReferenceService;
    private final ClaimEffectCopyFactory<Contract, Integer> claimEffectCopyFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void handle(PartyChange change, MachineEvent event, Integer changeId) {
        long sequenceId = event.getEventId();
        List<ClaimEffect> claimEffects = getClaimStatus(change).getAccepted().getEffects();
        for (int i = 0; i < claimEffects.size(); i++) {
            ClaimEffect claimEffect = claimEffects.get(i);
            if (claimEffect.isSetContractEffect()
                    && claimEffect.getContractEffect().getEffect().isSetLegalAgreementBound()) {
                handleEvent(event, changeId, sequenceId, claimEffects.get(i), i);
            }
        }
    }

    private void handleEvent(MachineEvent event, Integer changeId, long sequenceId, ClaimEffect claimEffect,
                             Integer claimEffectId) {
        ContractEffectUnit contractEffectUnit = claimEffect.getContractEffect();
        LegalAgreement legalAgreementBound = contractEffectUnit.getEffect().getLegalAgreementBound();
        String contractId = contractEffectUnit.getContractId();
        String partyId = event.getSourceId();
        log.info("Start contract legal agreement bound handling, sequenceId={}, partyId={}, contractId={}, changeId={}",
                sequenceId, partyId, contractId, changeId);

        Contract contractSourceOld = contractDao.get(partyId, contractId);
        Contract contractNew =
                claimEffectCopyFactory.create(event, sequenceId, claimEffectId, changeId, contractSourceOld);

        ContractUtil.fillContractLegalAgreementFields(contractNew, legalAgreementBound);

        contractDao.save(contractNew).ifPresentOrElse(
                dbContractId -> {
                    Long oldId = contractSourceOld.getId();
                    contractDao.updateNotCurrent(oldId);
                    contractReferenceService.updateContractReference(oldId, dbContractId);
                    log.info("Contract legal agreement bound has been saved, " +
                                    "sequenceId={}, partyId={}, contractId={}, changeId={}",
                            sequenceId, partyId, contractId, changeId);
                },
                () -> log.info("Contract legal agreement bound duplicated, " +
                                "sequenceId={}, partyId={}, contractId={}, changeId={}",
                        sequenceId, partyId, contractId, changeId)
        );
    }
}
