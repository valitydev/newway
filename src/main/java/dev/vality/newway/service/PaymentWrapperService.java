package dev.vality.newway.service;

import dev.vality.newway.dao.invoicing.iface.*;
import dev.vality.newway.domain.tables.pojos.*;
import dev.vality.newway.model.CashFlowWrapper;
import dev.vality.newway.model.InvoicingKey;
import dev.vality.newway.model.PaymentWrapper;
import dev.vality.newway.util.PaymentWrapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentWrapperService {

    private final PaymentDao paymentDao;
    private final PaymentStatusInfoDao paymentStatusInfoDao;
    private final PaymentPayerInfoDao paymentPayerInfoDao;
    private final PaymentAdditionalInfoDao paymentAdditionalInfoDao;
    private final PaymentRecurrentInfoDao paymentRecurrentInfoDao;
    private final PaymentRiskDataDao paymentRiskDataDao;
    private final PaymentFeeDao paymentFeeDao;
    private final PaymentRouteDao paymentRouteDao;
    private final CashFlowWrapperService cashFlowWrapperService;

    public void save(List<PaymentWrapper> paymentWrappers) {
        log.info("Start saving of payment batch, size={}", paymentWrappers.size());
        savePayments(paymentWrappers);
        savePaymentStatusInfo(paymentWrappers);
        savePaymentPayerInfo(paymentWrappers);
        savePaymentAdditionalInfo(paymentWrappers);
        savePaymentRecurrentInfo(paymentWrappers);
        savePaymentRiskData(paymentWrappers);
        savePaymentFee(paymentWrappers);
        savePaymentRoute(paymentWrappers);
        saveCashFlow(paymentWrappers);
        log.info("Saved payment batch");
    }

    private void savePayments(List<PaymentWrapper> paymentWrappers) {
        List<Payment> payments = paymentWrappers.stream()
                .map(PaymentWrapper::getPayment)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!payments.isEmpty()) {
            paymentDao.saveBatch(payments);
        }
    }

    private void savePaymentStatusInfo(List<PaymentWrapper> paymentWrappers) {
        List<PaymentStatusInfo> paymentStatusInfos = paymentWrappers.stream()
                .map(PaymentWrapper::getPaymentStatusInfo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!paymentStatusInfos.isEmpty()) {
            paymentStatusInfoDao.saveBatch(paymentStatusInfos);
            paymentStatusInfoDao.switchCurrent(PaymentWrapperUtil.getInvoicingKeys(paymentWrappers));
        }
    }

    private void savePaymentPayerInfo(List<PaymentWrapper> paymentWrappers) {
        List<PaymentPayerInfo> paymentPayerInfos = paymentWrappers.stream()
                .map(PaymentWrapper::getPaymentPayerInfo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!paymentPayerInfos.isEmpty()) {
            paymentPayerInfoDao.saveBatch(paymentPayerInfos);
        }
    }

    private void savePaymentAdditionalInfo(List<PaymentWrapper> paymentWrappers) {
        List<PaymentAdditionalInfo> paymentAdditionalInfos = paymentWrappers.stream()
                .map(PaymentWrapper::getPaymentAdditionalInfo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!paymentAdditionalInfos.isEmpty()) {
            paymentAdditionalInfoDao.saveBatch(paymentAdditionalInfos);
            paymentAdditionalInfoDao.switchCurrent(PaymentWrapperUtil.getInvoicingKeys(paymentWrappers));
        }
    }

    private void savePaymentRecurrentInfo(List<PaymentWrapper> paymentWrappers) {
        List<PaymentRecurrentInfo> paymentRecurrentInfos = paymentWrappers.stream()
                .map(PaymentWrapper::getPaymentRecurrentInfo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!paymentRecurrentInfos.isEmpty()) {
            paymentRecurrentInfoDao.saveBatch(paymentRecurrentInfos);
            paymentRecurrentInfoDao.switchCurrent(PaymentWrapperUtil.getInvoicingKeys(paymentWrappers));
        }
    }

    private void savePaymentRiskData(List<PaymentWrapper> paymentWrappers) {
        List<PaymentRiskData> paymentRiskDatas = paymentWrappers.stream()
                .map(PaymentWrapper::getPaymentRiskData)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!paymentRiskDatas.isEmpty()) {
            paymentRiskDataDao.saveBatch(paymentRiskDatas);
            paymentRiskDataDao.switchCurrent(PaymentWrapperUtil.getInvoicingKeys(paymentWrappers));
        }
    }

    private void savePaymentFee(List<PaymentWrapper> paymentWrappers) {
        List<PaymentFee> paymentFees = paymentWrappers.stream()
                .map(PaymentWrapper::getPaymentFee)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!paymentFees.isEmpty()) {
            paymentFeeDao.saveBatch(paymentFees);
            paymentFeeDao.switchCurrent(PaymentWrapperUtil.getInvoicingKeys(paymentWrappers));
        }
    }

    private void savePaymentRoute(List<PaymentWrapper> paymentWrappers) {
        List<PaymentRoute> paymentRoutes = paymentWrappers.stream()
                .map(PaymentWrapper::getPaymentRoute)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!paymentRoutes.isEmpty()) {
            paymentRouteDao.saveBatch(paymentRoutes);
            paymentRouteDao.switchCurrent(PaymentWrapperUtil.getInvoicingKeys(paymentWrappers));
        }
    }

    private void saveCashFlow(List<PaymentWrapper> paymentWrappers) {
        List<CashFlowWrapper> cashFlowWrappers = paymentWrappers.stream()
                .map(PaymentWrapper::getCashFlowWrapper)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!cashFlowWrappers.isEmpty()) {
            cashFlowWrapperService.saveBatch(cashFlowWrappers);
        }
    }

}