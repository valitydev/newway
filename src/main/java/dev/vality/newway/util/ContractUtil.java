package dev.vality.newway.util;

import dev.vality.damsel.domain.*;
import dev.vality.geck.common.util.TypeUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ContractUtil {
    public static List<dev.vality.newway.domain.tables.pojos.ContractAdjustment> convertContractAdjustments(Contract contract, long cntrctId) {
        return contract.getAdjustments().stream().map(ca -> convertContractAdjustment(ca, cntrctId))
                .collect(Collectors.toList());
    }

    public static dev.vality.newway.domain.tables.pojos.ContractAdjustment convertContractAdjustment(dev.vality.damsel.domain.ContractAdjustment ca,
                                                                                                     long cntrctId) {
        dev.vality.newway.domain.tables.pojos.ContractAdjustment adjustment = new dev.vality.newway.domain.tables.pojos.ContractAdjustment();
        adjustment.setCntrctId(cntrctId);
        adjustment.setContractAdjustmentId(ca.getId());
        adjustment.setCreatedAt(TypeUtil.stringToLocalDateTime(ca.getCreatedAt()));
        if (ca.isSetValidSince()) {
            adjustment.setValidSince(TypeUtil.stringToLocalDateTime(ca.getValidSince()));
        }
        if (ca.isSetValidUntil()) {
            adjustment.setValidUntil(TypeUtil.stringToLocalDateTime(ca.getValidUntil()));
        }
        adjustment.setTermsId(ca.getTerms().getId());
        return adjustment;
    }

    public static List<dev.vality.newway.domain.tables.pojos.PayoutTool> convertPayoutTools(Contract contract, long cntrctId) {
        return contract.getPayoutTools().stream().map(pt -> convertPayoutTool(pt, cntrctId))
                .collect(Collectors.toList());
    }

    public static dev.vality.newway.domain.tables.pojos.PayoutTool buildPayoutTool(long cntrctId,
                                                                                   String payoutToolId,
                                                                                   LocalDateTime createdAt,
                                                                                   String currCode,
                                                                                   dev.vality.damsel.domain.PayoutToolInfo payoutToolInfo) {
        dev.vality.newway.domain.tables.pojos.PayoutTool payoutTool = new dev.vality.newway.domain.tables.pojos.PayoutTool();
        payoutTool.setCntrctId(cntrctId);
        payoutTool.setPayoutToolId(payoutToolId);
        payoutTool.setCreatedAt(createdAt);
        payoutTool.setCurrencyCode(currCode);
        setPayoutToolInfo(payoutTool, payoutToolInfo);
        return payoutTool;
    }

    public static dev.vality.newway.domain.tables.pojos.PayoutTool convertPayoutTool(dev.vality.damsel.domain.PayoutTool pt, long cntrctId) {
        return buildPayoutTool(cntrctId, pt.getId(), TypeUtil.stringToLocalDateTime(pt.getCreatedAt()),
                pt.getCurrency().getSymbolicCode(), pt.getPayoutToolInfo());
    }

    public static void setPayoutToolInfo(dev.vality.newway.domain.tables.pojos.PayoutTool payoutTool,
                                         dev.vality.damsel.domain.PayoutToolInfo payoutToolInfoSource) {
        dev.vality.newway.domain.enums.PayoutToolInfo payoutToolInfo =
                TypeUtil.toEnumField(payoutToolInfoSource.getSetField().getFieldName(), dev.vality.newway.domain.enums.PayoutToolInfo.class);
        if (payoutToolInfo == null) {
            throw new IllegalArgumentException("Illegal payout tool info: " + payoutToolInfoSource);
        }
        payoutTool.setPayoutToolInfo(payoutToolInfo);
        if (payoutToolInfoSource.isSetRussianBankAccount()) {
            RussianBankAccount russianBankAccount = payoutToolInfoSource.getRussianBankAccount();
            payoutTool.setPayoutToolInfoRussianBankAccount(russianBankAccount.getAccount());
            payoutTool.setPayoutToolInfoRussianBankName(russianBankAccount.getBankName());
            payoutTool.setPayoutToolInfoRussianBankPostAccount(russianBankAccount.getBankPostAccount());
            payoutTool.setPayoutToolInfoRussianBankBik(russianBankAccount.getBankBik());
        } else if (payoutToolInfoSource.isSetInternationalBankAccount()) {
            InternationalBankAccount internationalBankAccount = payoutToolInfoSource.getInternationalBankAccount();
            payoutTool.setPayoutToolInfoInternationalBankNumber(internationalBankAccount.getNumber());
            payoutTool.setPayoutToolInfoInternationalBankAccountHolder(internationalBankAccount.getAccountHolder());
            payoutTool.setPayoutToolInfoInternationalBankIban(internationalBankAccount.getIban());

            if (internationalBankAccount.isSetBank()) {
                InternationalBankDetails bankDetails = internationalBankAccount.getBank();
                payoutTool.setPayoutToolInfoInternationalBankName(bankDetails.getName());
                payoutTool.setPayoutToolInfoInternationalBankAddress(bankDetails.getAddress());
                payoutTool.setPayoutToolInfoInternationalBankBic(bankDetails.getBic());
                payoutTool.setPayoutToolInfoInternationalBankAbaRtn(bankDetails.getAbaRtn());
                payoutTool.setPayoutToolInfoInternationalBankCountryCode(
                        Optional.ofNullable(bankDetails.getCountry())
                                .map(country -> country.toString())
                                .orElse(null)
                );
            }
            if (internationalBankAccount.isSetCorrespondentAccount()) {
                InternationalBankAccount correspondentBankAccount = internationalBankAccount.getCorrespondentAccount();
                payoutTool.setPayoutToolInfoInternationalCorrespondentBankNumber(correspondentBankAccount.getNumber());
                payoutTool.setPayoutToolInfoInternationalCorrespondentBankAccount(
                        correspondentBankAccount.getAccountHolder());
                payoutTool.setPayoutToolInfoInternationalCorrespondentBankIban(correspondentBankAccount.getIban());

                if (correspondentBankAccount.isSetBank()) {
                    InternationalBankDetails correspondentBankDetails = correspondentBankAccount.getBank();
                    payoutTool.setPayoutToolInfoInternationalCorrespondentBankName(correspondentBankDetails.getName());
                    payoutTool.setPayoutToolInfoInternationalCorrespondentBankAddress(
                            correspondentBankDetails.getAddress());
                    payoutTool.setPayoutToolInfoInternationalCorrespondentBankBic(correspondentBankDetails.getBic());
                    payoutTool.setPayoutToolInfoInternationalCorrespondentBankAbaRtn(
                            correspondentBankDetails.getAbaRtn());
                    payoutTool.setPayoutToolInfoInternationalCorrespondentBankCountryCode(
                            Optional.ofNullable(correspondentBankDetails.getCountry())
                                    .map(country -> country.toString())
                                    .orElse(null)
                    );
                }
            }
        } else if (payoutToolInfoSource.isSetWalletInfo()) {
            payoutTool.setPayoutToolInfoWalletInfoWalletId(payoutToolInfoSource.getWalletInfo().getWalletId());
        }
    }

    public static void fillContractLegalAgreementFields(dev.vality.newway.domain.tables.pojos.Contract contract,
                                                        LegalAgreement legalAgreement) {
        contract.setLegalAgreementId(legalAgreement.getLegalAgreementId());
        contract.setLegalAgreementSignedAt(TypeUtil.stringToLocalDateTime(legalAgreement.getSignedAt()));
        if (legalAgreement.isSetValidUntil()) {
            contract.setLegalAgreementValidUntil(TypeUtil.stringToLocalDateTime(legalAgreement.getValidUntil()));
        }
    }

    public static void fillReportPreferences(dev.vality.newway.domain.tables.pojos.Contract contract,
                                             ServiceAcceptanceActPreferences serviceAcceptanceActPreferences) {
        contract.setReportActScheduleId(serviceAcceptanceActPreferences.getSchedule().getId());
        contract.setReportActSignerPosition(serviceAcceptanceActPreferences.getSigner().getPosition());
        contract.setReportActSignerFullName(serviceAcceptanceActPreferences.getSigner().getFullName());
        dev.vality.damsel.domain.RepresentativeDocument representativeDocument =
                serviceAcceptanceActPreferences.getSigner().getDocument();
        dev.vality.newway.domain.enums.RepresentativeDocument reportActSignerDocument =
                TypeUtil.toEnumField(representativeDocument.getSetField().getFieldName(), dev.vality.newway.domain.enums.RepresentativeDocument.class);
        if (reportActSignerDocument == null) {
            throw new IllegalArgumentException("Illegal representative document: " + representativeDocument);
        }
        contract.setReportActSignerDocument(reportActSignerDocument);
        if (representativeDocument.isSetPowerOfAttorney()) {
            contract.setReportActSignerDocPowerOfAttorneyLegalAgreementId(
                    representativeDocument.getPowerOfAttorney().getLegalAgreementId());
            contract.setReportActSignerDocPowerOfAttorneySignedAt(
                    TypeUtil.stringToLocalDateTime(representativeDocument.getPowerOfAttorney().getSignedAt()));
            if (representativeDocument.getPowerOfAttorney().isSetValidUntil()) {
                contract.setReportActSignerDocPowerOfAttorneyValidUntil(
                        TypeUtil.stringToLocalDateTime(representativeDocument.getPowerOfAttorney().getValidUntil()));
            }
        }
    }

    public static void setNullReportPreferences(dev.vality.newway.domain.tables.pojos.Contract contract) {
        contract.setReportActScheduleId(null);
        contract.setReportActSignerPosition(null);
        contract.setReportActSignerFullName(null);
        contract.setReportActSignerDocument(null);
        contract.setReportActSignerDocPowerOfAttorneyLegalAgreementId(null);
        contract.setReportActSignerDocPowerOfAttorneySignedAt(null);
        contract.setReportActSignerDocPowerOfAttorneyValidUntil(null);
    }

}
