package dev.vality.newway.service;

import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.invoicing.iface.*;
import dev.vality.newway.dao.invoicing.impl.PaymentDaoImpl;
import dev.vality.newway.domain.enums.PaymentChangeType;
import dev.vality.newway.domain.tables.pojos.*;
import dev.vality.newway.model.CashFlowWrapper;
import dev.vality.newway.model.PaymentWrapper;
import dev.vality.newway.utils.PaymentWrapperTestUtil;
import dev.vality.testcontainers.annotations.util.RandomBeans;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashSet;
import java.util.List;

import static dev.vality.newway.utils.JdbcUtil.countPaymentEntity;
import static dev.vality.newway.utils.JdbcUtil.countEntities;
import static org.junit.jupiter.api.Assertions.assertEquals;

@PostgresqlSpringBootITest
public class PaymentBatchServiceTest {

    @Autowired
    private PaymentBatchService paymentBatchService;

    @Autowired
    private PaymentDaoImpl paymentDao;
    @Autowired
    private PaymentStatusInfoDao paymentStatusInfoDao;
    @Autowired
    private PaymentPayerInfoDao paymentPayerInfoDao;
    @Autowired
    private PaymentAdditionalInfoDao paymentAdditionalInfoDao;
    @Autowired
    private PaymentRecurrentInfoDao paymentRecurrentInfoDao;
    @Autowired
    private PaymentRiskDataDao paymentRiskDataDao;
    @Autowired
    private PaymentFeeDao paymentFeeDao;
    @Autowired
    private PaymentRouteDao paymentRouteDao;
    @Autowired
    private CashFlowLinkDao cashFlowLinkDao;
    @Autowired
    private CashFlowDao cashFlowDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void processTest() {
        List<PaymentWrapper> paymentWrappers = RandomBeans.randomListOf(2,PaymentWrapper.class);
        String invoiceIdFirst = "invoiceIdFirst";
        String invoiceIdSecond = "invoiceIdSecond";
        String paymentIdFirst = "paymentIdFirst";
        String paymentIdSecond = "paymentIdSecond";
        paymentWrappers.forEach(pw -> {
            pw.setCashFlowWrapper(new CashFlowWrapper(
                    RandomBeans.random(CashFlowLink.class),
                    RandomBeans.randomListOf(3, CashFlow.class)
            ));
            pw.getCashFlowWrapper().getCashFlows().forEach(cf -> {
                cf.setObjType(PaymentChangeType.payment);
            });
            PaymentWrapperTestUtil.setCurrent(pw, true);
        });
        PaymentWrapperTestUtil.setInvoiceIdAndPaymentId(paymentWrappers.get(0), invoiceIdFirst, paymentIdFirst);
        PaymentWrapperTestUtil.setInvoiceIdAndPaymentId(paymentWrappers.get(1), invoiceIdSecond, paymentIdSecond);
        
        paymentBatchService.process(paymentWrappers);
        assertPaymentWrapperFromDao(paymentWrappers.get(0), invoiceIdFirst, paymentIdFirst);
        assertPaymentWrapperFromDao(paymentWrappers.get(1), invoiceIdSecond, paymentIdSecond);

        //Duplication check
        paymentBatchService.process(paymentWrappers);
        assertDuplication(invoiceIdFirst, paymentIdFirst);
        assertDuplication(invoiceIdSecond, paymentIdSecond);
        assertTotalDuplication();
    }

    private void assertPaymentWrapperFromDao(PaymentWrapper expected, String invoiceId, String paymentId) {
        assertEquals(expected.getPayment(), paymentDao.get(invoiceId, paymentId));
        assertEquals(expected.getPaymentStatusInfo(), paymentStatusInfoDao.get(invoiceId, paymentId));
        assertEquals(expected.getPaymentPayerInfo(), paymentPayerInfoDao.get(invoiceId, paymentId));
        assertEquals(expected.getPaymentAdditionalInfo(), paymentAdditionalInfoDao.get(invoiceId, paymentId));
        assertEquals(expected.getPaymentRecurrentInfo(), paymentRecurrentInfoDao.get(invoiceId, paymentId));
        assertEquals(expected.getPaymentRiskData(), paymentRiskDataDao.get(invoiceId, paymentId));
        assertEquals(expected.getPaymentFee(), paymentFeeDao.get(invoiceId, paymentId));
        assertEquals(expected.getPaymentRoute(), paymentRouteDao.get(invoiceId, paymentId));
        assertEquals(expected.getCashFlowWrapper().getCashFlowLink(), cashFlowLinkDao.get(invoiceId, paymentId));
        assertEquals(
                new HashSet<>(expected.getCashFlowWrapper().getCashFlows()),
                new HashSet<>(cashFlowDao.getByObjId(expected.getCashFlowWrapper().getCashFlowLink().getId(), PaymentChangeType.payment))
        );
    }

    private void assertDuplication(String invoiceId, String paymentId) {
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_status_info", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_payer_info", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_additional_info", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_recurrent_info", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_risk_data", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_fee", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "payment_route", invoiceId, paymentId, false));
        assertEquals(1, countPaymentEntity(jdbcTemplate, "cash_flow_link", invoiceId, paymentId, false));
    }

    private void assertTotalDuplication() {
        assertEquals(2, countEntities(jdbcTemplate, "payment"));
        assertEquals(2, countEntities(jdbcTemplate, "payment_status_info"));
        assertEquals(2, countEntities(jdbcTemplate, "payment_payer_info"));
        assertEquals(2, countEntities(jdbcTemplate, "payment_additional_info"));
        assertEquals(2, countEntities(jdbcTemplate, "payment_recurrent_info"));
        assertEquals(2, countEntities(jdbcTemplate, "payment_risk_data"));
        assertEquals(2, countEntities(jdbcTemplate, "payment_fee"));
        assertEquals(2, countEntities(jdbcTemplate, "payment_route"));
        assertEquals(2, countEntities(jdbcTemplate, "cash_flow_link"));
        assertEquals(6, countEntities(jdbcTemplate, "cash_flow"));
    }
}
