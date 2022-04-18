package dev.vality.newway.dao;

import dev.vality.newway.config.PostgresqlSpringBootITest;
import dev.vality.newway.dao.dominant.iface.DominantDao;
import dev.vality.newway.dao.dominant.impl.*;
import dev.vality.newway.dao.invoicing.iface.*;
import dev.vality.newway.dao.invoicing.impl.InvoiceDaoImpl;
import dev.vality.newway.dao.invoicing.impl.PaymentDaoImpl;
import dev.vality.newway.dao.invoicing.impl.PaymentIdsGeneratorDaoImpl;
import dev.vality.newway.dao.party.iface.*;
import dev.vality.newway.dao.rate.iface.RateDao;
import dev.vality.newway.dao.recurrent.payment.tool.iface.RecurrentPaymentToolDao;
import dev.vality.newway.domain.enums.AdjustmentCashFlowType;
import dev.vality.newway.domain.enums.CashFlowAccount;
import dev.vality.newway.domain.enums.PaymentChangeType;
import dev.vality.newway.domain.tables.pojos.Calendar;
import dev.vality.newway.domain.tables.pojos.Currency;
import dev.vality.newway.domain.tables.pojos.*;
import dev.vality.newway.exception.NotFoundException;
import dev.vality.newway.model.InvoicingKey;
import dev.vality.newway.model.InvoicingType;
import dev.vality.newway.utils.HashUtil;
import dev.vality.testcontainers.annotations.util.RandomBeans;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@PostgresqlSpringBootITest
public class DaoTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CalendarDaoImpl calendarDao;
    @Autowired
    private CategoryDaoImpl categoryDao;
    @Autowired
    private CurrencyDaoImpl currencyDao;
    @Autowired
    private InspectorDaoImpl inspectorDao;
    @Autowired
    private PaymentInstitutionDaoImpl paymentInstitutionDao;
    @Autowired
    private PaymentMethodDaoImpl paymentMethodDao;
    @Autowired
    private PayoutMethodDaoImpl payoutMethodDao;
    @Autowired
    private ProviderDaoImpl providerDao;
    @Autowired
    private WithdrawalProviderDaoImpl withdrawalProviderDao;
    @Autowired
    private ProxyDaoImpl proxyDao;
    @Autowired
    private TerminalDaoImpl terminalDao;
    @Autowired
    private TermSetHierarchyDaoImpl termSetHierarchyDao;
    @Autowired
    private DominantDao dominantDao;
    @Autowired
    private CashFlowDao cashFlowDao;
    @Autowired
    private AdjustmentDao adjustmentDao;
    @Autowired
    private PaymentDaoImpl paymentDao;
    @Autowired
    private InvoiceCartDao invoiceCartDao;
    @Autowired
    private InvoiceDaoImpl invoiceDao;
    @Autowired
    private InvoiceStatusInfoDao invoiceStatusInfoDao;
    @Autowired
    private RefundDao refundDao;
    @Autowired
    private ContractAdjustmentDao contractAdjustmentDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ContractorDao contractorDao;
    @Autowired
    private PartyDao partyDao;
    @Autowired
    private PayoutToolDao payoutToolDao;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private RateDao rateDao;
    @Autowired
    private PaymentIdsGeneratorDaoImpl idsGeneratorDao;
    @Autowired
    private RecurrentPaymentToolDao recurrentPaymentToolDao;


    @Test
    public void dominantDaoTest() {
        jdbcTemplate.execute("truncate table nw.calendar cascade");
        jdbcTemplate.execute("truncate table nw.category cascade");
        jdbcTemplate.execute("truncate table nw.currency cascade");
        jdbcTemplate.execute("truncate table nw.inspector cascade");
        jdbcTemplate.execute("truncate table nw.payment_institution cascade");
        jdbcTemplate.execute("truncate table nw.payment_method cascade");
        jdbcTemplate.execute("truncate table nw.payout_method cascade");
        jdbcTemplate.execute("truncate table nw.provider cascade");
        jdbcTemplate.execute("truncate table nw.withdrawal_provider cascade");
        jdbcTemplate.execute("truncate table nw.proxy cascade");
        jdbcTemplate.execute("truncate table nw.terminal cascade");
        jdbcTemplate.execute("truncate table nw.term_set_hierarchy cascade");

        var calendar = dev.vality.testcontainers.annotations.util.RandomBeans.random(Calendar.class);
        calendar.setCurrent(true);
        calendarDao.save(calendar);
        calendarDao.updateNotCurrent(calendar.getCalendarRefId());

        Category category = dev.vality.testcontainers.annotations.util.RandomBeans.random(Category.class);
        category.setCurrent(true);
        categoryDao.save(category);
        categoryDao.updateNotCurrent(category.getCategoryRefId());

        var currency = dev.vality.testcontainers.annotations.util.RandomBeans.random(Currency.class);
        currency.setCurrent(true);
        currencyDao.save(currency);
        currencyDao.updateNotCurrent(currency.getCurrencyRefId());

        Inspector inspector = dev.vality.testcontainers.annotations.util.RandomBeans.random(Inspector.class);
        inspector.setCurrent(true);
        inspectorDao.save(inspector);
        inspectorDao.updateNotCurrent(inspector.getInspectorRefId());

        PaymentInstitution paymentInstitution = dev.vality.testcontainers.annotations.util.RandomBeans.random(PaymentInstitution.class);
        paymentInstitution.setCurrent(true);
        paymentInstitutionDao.save(paymentInstitution);
        paymentInstitutionDao.updateNotCurrent(paymentInstitution.getPaymentInstitutionRefId());

        PaymentMethod paymentMethod = dev.vality.testcontainers.annotations.util.RandomBeans.random(PaymentMethod.class);
        paymentMethod.setCurrent(true);
        paymentMethodDao.save(paymentMethod);
        paymentMethodDao.updateNotCurrent(paymentMethod.getPaymentMethodRefId());

        PayoutMethod payoutMethod = dev.vality.testcontainers.annotations.util.RandomBeans.random(PayoutMethod.class);
        payoutMethod.setCurrent(true);
        payoutMethodDao.save(payoutMethod);
        payoutMethodDao.updateNotCurrent(payoutMethod.getPayoutMethodRefId());

        Provider provider = dev.vality.testcontainers.annotations.util.RandomBeans.random(Provider.class);
        provider.setCurrent(true);
        providerDao.save(provider);
        providerDao.updateNotCurrent(provider.getProviderRefId());

        WithdrawalProvider withdrawalProvider = dev.vality.testcontainers.annotations.util.RandomBeans.random(WithdrawalProvider.class);
        withdrawalProvider.setCurrent(true);
        withdrawalProviderDao.save(withdrawalProvider);
        withdrawalProviderDao.updateNotCurrent(withdrawalProvider.getWithdrawalProviderRefId());

        Proxy proxy = dev.vality.testcontainers.annotations.util.RandomBeans.random(Proxy.class);
        proxy.setCurrent(true);
        proxyDao.save(proxy);
        proxyDao.updateNotCurrent(proxy.getProxyRefId());

        Terminal terminal = dev.vality.testcontainers.annotations.util.RandomBeans.random(Terminal.class);
        terminal.setCurrent(true);
        terminalDao.save(terminal);
        terminalDao.updateNotCurrent(terminal.getTerminalRefId());

        TermSetHierarchy termSetHierarchy = dev.vality.testcontainers.annotations.util.RandomBeans.random(TermSetHierarchy.class);
        termSetHierarchy.setCurrent(true);
        termSetHierarchyDao.save(termSetHierarchy);
        termSetHierarchyDao.updateNotCurrent(termSetHierarchy.getTermSetHierarchyRefId());

        Long lastVersionId = dominantDao.getLastVersionId();

        OptionalLong maxVersionId = LongStream.of(
                calendar.getVersionId(),
                category.getVersionId(),
                currency.getVersionId(),
                inspector.getVersionId(),
                paymentInstitution.getVersionId(),
                paymentMethod.getVersionId(),
                payoutMethod.getVersionId(),
                provider.getVersionId(),
                withdrawalProvider.getVersionId(),
                proxy.getVersionId(),
                terminal.getVersionId(),
                termSetHierarchy.getVersionId()).max();

        assertEquals(maxVersionId.getAsLong(), lastVersionId.longValue());
    }

    @Test
    public void differentCashFlowAggregateFunctionTest() {
        jdbcTemplate.execute("truncate table nw.cash_flow cascade");
        List<CashFlow> cashFlows = new ArrayList<>();

        CashFlow cashFlowPaymentAmount =
                DaoUtils.createCashFlow(1L, 1000L, "RUB", 1L, CashFlowAccount.provider, "settlement", 2L,
                        CashFlowAccount.merchant, "settlement", PaymentChangeType.payment);
        cashFlows.add(cashFlowPaymentAmount);
        CashFlow cashFlowPaymentFee =
                DaoUtils.createCashFlow(1L, 10L, "RUB", 2L, CashFlowAccount.merchant, "settlement", 2L, CashFlowAccount.system,
                        "settlement", PaymentChangeType.payment);
        cashFlows.add(cashFlowPaymentFee);
        CashFlow cashFlowPaymentExternalIncomeFee =
                DaoUtils.createCashFlow(1L, 3L, "RUB", 2L, CashFlowAccount.system, "settlement", 3L, CashFlowAccount.external,
                        "income", PaymentChangeType.payment);
        cashFlows.add(cashFlowPaymentExternalIncomeFee);
        CashFlow cashFlowPaymentExternalOutcomeFee =
                DaoUtils.createCashFlow(1L, 3L, "RUB", 2L, CashFlowAccount.system, "settlement", 4L, CashFlowAccount.external,
                        "outcome", PaymentChangeType.payment);
        cashFlows.add(cashFlowPaymentExternalOutcomeFee);
        CashFlow cashFlowPaymentProviderFee =
                DaoUtils.createCashFlow(1L, 3L, "RUB", 2L, CashFlowAccount.system, "settlement", 5L, CashFlowAccount.provider,
                        "settlement", PaymentChangeType.payment);
        cashFlows.add(cashFlowPaymentProviderFee);
        CashFlow cashFlowPaymentGuaranteeDeposit =
                DaoUtils.createCashFlow(1L, 30L, "RUB", 2L, CashFlowAccount.merchant, "settlement", 5L, CashFlowAccount.merchant,
                        "guarantee", PaymentChangeType.payment);
        cashFlows.add(cashFlowPaymentGuaranteeDeposit);
        CashFlow cashFlowRefundAmount = DaoUtils.createCashFlow(1L, 1000L, "RUB", 2L, CashFlowAccount.merchant, "settlement", 5L,
                CashFlowAccount.provider, "settlement", PaymentChangeType.refund);
        cashFlows.add(cashFlowRefundAmount);
        CashFlow cashFlowRefundFee =
                DaoUtils.createCashFlow(1L, 10L, "RUB", 2L, CashFlowAccount.merchant, "settlement", 2L, CashFlowAccount.system,
                        "settlement", PaymentChangeType.refund);
        cashFlows.add(cashFlowRefundFee);
        CashFlow cashFlowRefundExternalIncomeFee =
                DaoUtils.createCashFlow(1L, 3L, "RUB", 2L, CashFlowAccount.system, "settlement", 3L, CashFlowAccount.external,
                        "income", PaymentChangeType.refund);
        cashFlows.add(cashFlowRefundExternalIncomeFee);
        CashFlow cashFlowRefundExternalOutcomeFee =
                DaoUtils.createCashFlow(1L, 3L, "RUB", 2L, CashFlowAccount.system, "settlement", 4L, CashFlowAccount.external,
                        "outcome", PaymentChangeType.refund);
        cashFlows.add(cashFlowRefundExternalOutcomeFee);
        CashFlow cashFlowRefundProviderFee =
                DaoUtils.createCashFlow(1L, 3L, "RUB", 2L, CashFlowAccount.system, "settlement", 5L, CashFlowAccount.provider,
                        "settlement", PaymentChangeType.refund);
        cashFlows.add(cashFlowRefundProviderFee);
        CashFlow cashFlowPayoutAmount = DaoUtils.createCashFlow(1L, 1000L, "RUB", 2L, CashFlowAccount.merchant, "settlement", 7L,
                CashFlowAccount.merchant, "payout", PaymentChangeType.payout);
        cashFlows.add(cashFlowPayoutAmount);
        CashFlow cashFlowPayoutFee =
                DaoUtils.createCashFlow(1L, 10L, "RUB", 1L, CashFlowAccount.merchant, "settlement", 2L, CashFlowAccount.system,
                        "settlement", PaymentChangeType.payout);
        cashFlows.add(cashFlowPayoutFee);
        CashFlow cashFlowPayoutFixedFee =
                DaoUtils.createCashFlow(1L, 100L, "RUB", 7L, CashFlowAccount.merchant, "payout", 2L, CashFlowAccount.system,
                        "settlement", PaymentChangeType.payout);
        cashFlows.add(cashFlowPayoutFixedFee);
        CashFlow cashFlowAdjustmentAmount =
                DaoUtils.createCashFlow(1L, 1000L, "RUB", 1L, CashFlowAccount.provider, "settlement", 2L,
                        CashFlowAccount.merchant, "settlement", PaymentChangeType.adjustment);
        cashFlowAdjustmentAmount.setAdjFlowType(AdjustmentCashFlowType.new_cash_flow);
        cashFlows.add(cashFlowAdjustmentAmount);

        cashFlowDao.save(cashFlows);

        assertEquals(cashFlowPaymentAmount.getAmount(), jdbcTemplate
                .queryForObject("SELECT nw.get_payment_amount(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(cashFlowPaymentFee.getAmount(), jdbcTemplate
                .queryForObject("SELECT nw.get_payment_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(cashFlowPaymentExternalIncomeFee.getAmount() + cashFlowPaymentExternalOutcomeFee.getAmount(), (long) jdbcTemplate.queryForObject(
                "SELECT nw.get_payment_external_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                new SingleColumnRowMapper<>(Long.class)));
        assertEquals(cashFlowPaymentProviderFee.getAmount(), jdbcTemplate
                .queryForObject("SELECT nw.get_payment_provider_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(cashFlowPaymentGuaranteeDeposit.getAmount(), jdbcTemplate.queryForObject(
                "SELECT nw.get_payment_guarantee_deposit(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                new SingleColumnRowMapper<>(Long.class)));

        assertEquals(cashFlowRefundAmount.getAmount(), jdbcTemplate
                .queryForObject("SELECT nw.get_refund_amount(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(cashFlowRefundFee.getAmount(), jdbcTemplate
                .queryForObject("SELECT nw.get_refund_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(cashFlowRefundExternalIncomeFee.getAmount() + cashFlowRefundExternalOutcomeFee.getAmount(), (long) jdbcTemplate.queryForObject(
                "SELECT nw.get_refund_external_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                new SingleColumnRowMapper<>(Long.class)));
        assertEquals(cashFlowRefundProviderFee.getAmount(), jdbcTemplate
                .queryForObject("SELECT nw.get_refund_provider_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));

        assertEquals(cashFlowPayoutAmount.getAmount(), jdbcTemplate
                .queryForObject("SELECT nw.get_payout_amount(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(cashFlowPayoutFixedFee.getAmount(), jdbcTemplate
                .queryForObject("SELECT nw.get_payout_fixed_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(cashFlowPayoutFee.getAmount(), jdbcTemplate
                .queryForObject("SELECT nw.get_payout_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
    }

    @Test
    public void whenDataNotFoundCashFlowAggregateFunctionTest() {
        jdbcTemplate.execute("truncate table nw.cash_flow cascade");
        CashFlow notCashFlow = dev.vality.testcontainers.annotations.util.RandomBeans.random(CashFlow.class, "objId");
        notCashFlow.setObjId(1L);
        cashFlowDao.save(Collections.singletonList(notCashFlow));

        assertEquals(0L, (long) jdbcTemplate
                .queryForObject("SELECT nw.get_payment_amount(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(0L, (long) jdbcTemplate
                .queryForObject("SELECT nw.get_payment_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(0L, (long) jdbcTemplate
                .queryForObject("SELECT nw.get_payment_external_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(0L, (long) jdbcTemplate
                .queryForObject("SELECT nw.get_payment_provider_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(0L, (long) jdbcTemplate.queryForObject(
                "SELECT nw.get_payment_guarantee_deposit(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                new SingleColumnRowMapper<>(Long.class)));

        assertEquals(0L, (long) jdbcTemplate
                .queryForObject("SELECT nw.get_refund_amount(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(0L, (long) jdbcTemplate
                .queryForObject("SELECT nw.get_refund_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(0L, (long) jdbcTemplate
                .queryForObject("SELECT nw.get_refund_external_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(0L, (long) jdbcTemplate
                .queryForObject("SELECT nw.get_refund_provider_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));

        assertEquals(0L, (long) jdbcTemplate
                .queryForObject("SELECT nw.get_payout_amount(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(0L, (long) jdbcTemplate
                .queryForObject("SELECT nw.get_payout_fixed_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(0L, (long) jdbcTemplate
                .queryForObject("SELECT nw.get_payout_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));

        assertEquals(0L, (long) jdbcTemplate
                .queryForObject("SELECT nw.get_adjustment_amount(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(0L, (long) jdbcTemplate
                .queryForObject("SELECT nw.get_adjustment_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                        new SingleColumnRowMapper<>(Long.class)));
        assertEquals(0L, (long) jdbcTemplate.queryForObject(
                "SELECT nw.get_adjustment_external_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                new SingleColumnRowMapper<>(Long.class)));
        assertEquals(0L, (long) jdbcTemplate.queryForObject(
                "SELECT nw.get_adjustment_provider_fee(nw.cash_flow.*) FROM nw.cash_flow WHERE obj_id = 1",
                new SingleColumnRowMapper<>(Long.class)));
    }

    @Test
    public void cashFlowDaoTest() {
        jdbcTemplate.execute("truncate table nw.payment cascade");
        jdbcTemplate.execute("truncate table nw.cash_flow cascade");
        Long pmntId = 123L;
        List<CashFlow> cashFlowList = dev.vality.testcontainers.annotations.util.RandomBeans.randomListOf(100, CashFlow.class);
        cashFlowList.forEach(cf -> {
            cf.setObjId(pmntId);
            cf.setAmount((long) new Random().nextInt(100));
            cf.setObjType(PaymentChangeType.payment);
            cf.setAdjFlowType(null);
            cf.setSourceAccountTypeValue("settlement");
            if (cf.getDestinationAccountType() == CashFlowAccount.external) {
                cf.setDestinationAccountTypeValue("income");
            } else {
                cf.setDestinationAccountTypeValue("settlement");
            }
        });
        cashFlowDao.save(cashFlowList);
        List<CashFlow> byObjId = cashFlowDao.getByObjId(pmntId, PaymentChangeType.payment);
        assertEquals(new HashSet(byObjId), new HashSet(cashFlowList));
    }


    @Test
    public void adjustmentDaoTest() {
        jdbcTemplate.execute("truncate table nw.adjustment cascade");
        Adjustment adjustment = dev.vality.testcontainers.annotations.util.RandomBeans.random(Adjustment.class);
        adjustment.setCurrent(true);
        adjustmentDao.save(adjustment);
        assertEquals(adjustment.getPartyId(), adjustmentDao.get(adjustment.getInvoiceId(), adjustment.getPaymentId(), adjustment.getAdjustmentId())
                .getPartyId());
        adjustmentDao.updateNotCurrent(adjustment.getId());

        assertThrows(NotFoundException.class, () -> adjustmentDao.get(adjustment.getInvoiceId(), adjustment.getPaymentId(), adjustment.getAdjustmentId()));
    }

    @Test
    void invoiceDaoTest() {
        jdbcTemplate.execute("truncate table nw.invoice cascade");
        List<Invoice> invoices = dev.vality.testcontainers.annotations.util.RandomBeans.randomListOf(3, Invoice.class);
        invoices.forEach(invoice -> invoice.setCurrent(true));
        invoiceDao.saveBatch(invoices);
        assertEquals(invoices.get(0), invoiceDao.get(invoices.get(0).getInvoiceId()));
        assertEquals(invoices.get(1), invoiceDao.get(invoices.get(1).getInvoiceId()));
        assertEquals(invoices.get(2), invoiceDao.get(invoices.get(2).getInvoiceId()));

        Invoice invoice = RandomBeans.random(Invoice.class);
        invoice.setInvoiceId(invoices.get(0).getInvoiceId());
        invoice.setCurrent(false);
        invoice.setId(invoices.get(0).getId() + 1);
        invoiceDao.saveBatch(List.of(invoice));
        assertNotEquals(invoice, invoices.get(0));
        assertEquals(invoices.get(0), invoiceDao.get(invoice.getInvoiceId()));
        invoiceDao.switchCurrent(Set.of(invoice.getInvoiceId()));
        invoice.setCurrent(true);
        assertEquals(invoice, invoiceDao.get(invoice.getInvoiceId()));

        Set<String> invoiceIds = invoices.stream()
                .map(Invoice::getInvoiceId)
                .collect(Collectors.toSet());
        assertEquals(Set.of(invoice, invoices.get(1), invoices.get(2)), Set.copyOf(invoiceDao.getList(invoiceIds)));
    }


    @Test
    void invoiceStatusInfoDaoTest() {
        jdbcTemplate.execute("truncate table nw.invoice_status_info cascade");
        List<InvoiceStatusInfo> statusInfos =
                dev.vality.testcontainers.annotations.util.RandomBeans.randomListOf(3, InvoiceStatusInfo.class);
        statusInfos.forEach(status -> status.setCurrent(true));
        invoiceStatusInfoDao.saveBatch(statusInfos);
        assertEquals(statusInfos.get(0), invoiceStatusInfoDao.get(statusInfos.get(0).getInvoiceId()));
        assertEquals(statusInfos.get(1), invoiceStatusInfoDao.get(statusInfos.get(1).getInvoiceId()));
        assertEquals(statusInfos.get(2), invoiceStatusInfoDao.get(statusInfos.get(2).getInvoiceId()));

        InvoiceStatusInfo statusInfo = RandomBeans.random(InvoiceStatusInfo.class);
        InvoiceStatusInfo initialStatusInfo = statusInfos.get(0);
        statusInfo.setInvoiceId(initialStatusInfo.getInvoiceId());
        statusInfo.setCurrent(false);
        statusInfo.setId(initialStatusInfo.getId() + 1);
        invoiceStatusInfoDao.saveBatch(List.of(statusInfo));
        assertNotEquals(statusInfo, initialStatusInfo);
        assertEquals(initialStatusInfo, invoiceStatusInfoDao.get(initialStatusInfo.getInvoiceId()));
        invoiceStatusInfoDao.switchCurrent(Set.of(statusInfo.getInvoiceId()));
        statusInfo.setCurrent(true);
        assertEquals(statusInfo, invoiceStatusInfoDao.get(initialStatusInfo.getInvoiceId()));

        Set<String> invoiceIds = statusInfos.stream()
                .map(InvoiceStatusInfo::getInvoiceId)
                .collect(Collectors.toSet());
        assertEquals(
                Set.of(statusInfo, statusInfos.get(1), statusInfos.get(2)),
                Set.copyOf(invoiceStatusInfoDao.getList(invoiceIds))
        );
    }

    @Test
    public void invoiceCartDaoTest() {
        jdbcTemplate.execute("truncate table nw.invoice_cart cascade");
        String invoiceId = UUID.randomUUID().toString();
        List<InvoiceCart> invoiceCarts = dev.vality.testcontainers.annotations.util.RandomBeans.randomListOf(10, InvoiceCart.class);
        invoiceCarts.forEach(ic -> ic.setInvoiceId(invoiceId));
        invoiceCartDao.save(invoiceCarts);
        assertEquals(invoiceCarts, invoiceCartDao.getByInvoiceId(invoiceId));
        List<InvoiceCart> invoiceRandomCarts = dev.vality.testcontainers.annotations.util.RandomBeans.randomListOf(10, InvoiceCart.class);
        invoiceCartDao.save(invoiceRandomCarts);
        Set<String> invoiceIds = invoiceRandomCarts.stream()
                .map(InvoiceCart::getInvoiceId)
                .collect(Collectors.toSet());
        assertEquals(invoiceRandomCarts, invoiceCartDao.getByInvoiceIdsIn(invoiceIds));
    }

    @Test
    public void paymentDaoTest() {
        jdbcTemplate.execute("truncate table nw.payment cascade");
        Payment payment = dev.vality.testcontainers.annotations.util.RandomBeans.random(Payment.class);
        payment.setId(1L);
        payment.setCurrent(false);
        Payment paymentTwo = dev.vality.testcontainers.annotations.util.RandomBeans.random(Payment.class);
        paymentTwo.setId(2L);
        paymentTwo.setCurrent(false);
        paymentTwo.setInvoiceId(payment.getInvoiceId());
        paymentTwo.setPaymentId(payment.getPaymentId());
        paymentDao.saveBatch(Arrays.asList(payment, paymentTwo));
        paymentDao.switchCurrent(Collections.singletonList(
                new InvoicingKey(payment.getInvoiceId(), payment.getPaymentId(), InvoicingType.PAYMENT)));
        Payment paymentGet = paymentDao.get(payment.getInvoiceId(), payment.getPaymentId());
        paymentTwo.setCurrent(true);
        assertEquals(paymentTwo, paymentGet);
        paymentTwo.setPartyRevision(1111L);
        paymentDao.updateBatch(Collections.singletonList(paymentTwo));
        Payment paymentGet2 = paymentDao.get(payment.getInvoiceId(), payment.getPaymentId());
        assertEquals(paymentTwo, paymentGet2);
    }

    @Test
    public void refundDaoTest() {
        jdbcTemplate.execute("truncate table nw.refund cascade");
        Refund refund = dev.vality.testcontainers.annotations.util.RandomBeans.random(Refund.class);
        refund.setCurrent(true);
        refundDao.save(refund);
        Refund refundGet = refundDao.get(refund.getInvoiceId(), refund.getPaymentId(), refund.getRefundId());
        assertEquals(refund, refundGet);
        refundDao.updateNotCurrent(refund.getId());

        assertThrows(NotFoundException.class, () -> refundDao.get(refund.getInvoiceId(), refund.getPaymentId(), refund.getRefundId()));
    }

    @Test
    public void contractAdjustmentDaoTest() {
        jdbcTemplate.execute("truncate table nw.contract_adjustment cascade");
        jdbcTemplate.execute("truncate table nw.contract cascade");
        Contract contract = dev.vality.testcontainers.annotations.util.RandomBeans.random(Contract.class);
        contract.setCurrent(true);
        Long cntrctId = contractDao.save(contract).get();
        List<ContractAdjustment> contractAdjustments = dev.vality.testcontainers.annotations.util.RandomBeans.randomListOf(10, ContractAdjustment.class);
        contractAdjustments.forEach(ca -> ca.setCntrctId(cntrctId));
        contractAdjustmentDao.save(contractAdjustments);
        List<ContractAdjustment> byCntrctId = contractAdjustmentDao.getByCntrctId(cntrctId);
        assertEquals(new HashSet(contractAdjustments), new HashSet(byCntrctId));
    }

    @Test
    public void contractDaoTest() {
        jdbcTemplate.execute("truncate table nw.contract cascade");
        Contract contract = dev.vality.testcontainers.annotations.util.RandomBeans.random(Contract.class);
        contract.setCurrent(true);
        contractDao.save(contract);
        Contract contractGet = contractDao.get(contract.getPartyId(), contract.getContractId());
        assertEquals(contract, contractGet);
    }

    @Test
    public void contractorDaoTest() {
        jdbcTemplate.execute("truncate table nw.contractor cascade");
        Contractor contractor = dev.vality.testcontainers.annotations.util.RandomBeans.random(Contractor.class);
        contractor.setCurrent(true);
        contractorDao.save(contractor);
        Contractor contractorGet = contractorDao.get(contractor.getPartyId(), contractor.getContractorId());
        assertEquals(contractor, contractorGet);
        Integer changeId = contractor.getChangeId() + 1;
        contractor.setChangeId(changeId);
        Long oldId = contractor.getId();
        contractor.setId(contractor.getId() + 1);
        contractorDao.save(contractor);
        contractorDao.updateNotCurrent(oldId);
    }

    @Test
    public void partyDaoTest() {
        jdbcTemplate.execute("truncate table nw.party cascade");
        Party party = dev.vality.testcontainers.annotations.util.RandomBeans.random(Party.class);
        party.setCurrent(true);
        partyDao.save(party);
        Party partyGet = partyDao.get(party.getPartyId());
        assertEquals(party, partyGet);
        Long oldId = party.getId();

        Integer changeId = party.getChangeId() + 1;
        party.setChangeId(changeId);
        party.setId(party.getId() + 1);
        partyDao.save(party);
        partyDao.updateNotCurrent(oldId);

        partyGet = partyDao.get(party.getPartyId());
        assertEquals(changeId, partyGet.getChangeId());
    }

    @Test
    public void payoutToolDaoTest() {
        jdbcTemplate.execute("truncate table nw.contract cascade");
        jdbcTemplate.execute("truncate table nw.payout_tool cascade");
        Contract contract = dev.vality.testcontainers.annotations.util.RandomBeans.random(Contract.class);
        contract.setCurrent(true);
        Long cntrctId = contractDao.save(contract).get();
        List<PayoutTool> payoutTools = dev.vality.testcontainers.annotations.util.RandomBeans.randomListOf(10, PayoutTool.class);
        payoutTools.forEach(pt -> pt.setCntrctId(cntrctId));
        payoutToolDao.save(payoutTools);
        List<PayoutTool> byCntrctId = payoutToolDao.getByCntrctId(cntrctId);
        assertEquals(new HashSet(payoutTools), new HashSet(byCntrctId));
    }

    @Test
    public void shopDaoTest() {
        jdbcTemplate.execute("truncate table nw.shop cascade");
        Shop shop = dev.vality.testcontainers.annotations.util.RandomBeans.random(Shop.class);
        shop.setCurrent(true);
        shopDao.save(shop);
        Shop shopGet = shopDao.get(shop.getPartyId(), shop.getShopId());
        assertEquals(shop, shopGet);

        Integer changeId = shop.getChangeId() + 1;
        shop.setChangeId(changeId);
        Long id = shop.getId();
        shop.setId(id + 1);
        shopDao.save(shop);
        shopDao.updateNotCurrent(id);
    }

    @Test
    public void rateDaoTest() {
        jdbcTemplate.execute("truncate table nw.rate cascade");
        Rate rate = dev.vality.testcontainers.annotations.util.RandomBeans.random(Rate.class);
        rate.setCurrent(true);

        Long id = rateDao.save(rate);
        rate.setId(id);
        assertEquals(rate, jdbcTemplate.queryForObject(
                "SELECT * FROM nw.rate WHERE id = ? ",
                new Object[]{id},
                new BeanPropertyRowMapper(Rate.class)
        ));

        List<Long> ids = rateDao.getIds(rate.getSourceId());
        Assertions.assertNotNull(ids);
        Assertions.assertFalse(ids.isEmpty());
        assertEquals(1, ids.size());
        assertEquals(id, ids.get(0));

        rateDao.updateNotCurrent(Collections.singletonList(id));
        assertThrows(EmptyResultDataAccessException.class, () -> jdbcTemplate.queryForObject(
                "SELECT * FROM nw.rate AS rate WHERE rate.id = ? AND rate.current",
                new Object[]{id},
                new BeanPropertyRowMapper(Rate.class)
        ));
    }

    @Test
    public void getIntHashTest() {
        Integer javaHash = HashUtil.getIntHash("kek");
        Integer postgresHash =
                jdbcTemplate.queryForObject("select ('x0'||substr(md5('kek'), 1, 7))::bit(32)::int", Integer.class);
        assertEquals(javaHash, postgresHash);
    }

    @Test
    public void constraintTests() {
        jdbcTemplate.execute("truncate table nw.adjustment cascade");
        Adjustment adjustment = dev.vality.testcontainers.annotations.util.RandomBeans.random(Adjustment.class);
        adjustment.setChangeId(1);
        adjustment.setSequenceId(1L);
        adjustment.setInvoiceId("1");
        adjustment.setPartyId("1");
        adjustment.setCurrent(true);
        adjustmentDao.save(adjustment);

        assertEquals("1", adjustmentDao.get(adjustment.getInvoiceId(), adjustment.getPaymentId(), adjustment.getAdjustmentId())
                .getPartyId());

        adjustment.setPartyId("2");

        adjustmentDao.save(adjustment);

        assertEquals("1", adjustmentDao.get(adjustment.getInvoiceId(), adjustment.getPaymentId(), adjustment.getAdjustmentId())
                .getPartyId());
    }

    @Test
    public void idsGeneratorTest() {
        List<Long> list = idsGeneratorDao.get(100);
        assertEquals(100, list.size());
        assertEquals(99, list.get(99) - list.get(0));
    }

    @Test
    public void recurrentPaymentToolDaoTest() {
        jdbcTemplate.execute("truncate table nw.recurrent_payment_tool cascade");
        RecurrentPaymentTool recurrentPaymentTool = dev.vality.testcontainers.annotations.util.RandomBeans.random(RecurrentPaymentTool.class);
        recurrentPaymentTool.setCurrent(true);
        Optional<Long> id = recurrentPaymentToolDao.save(recurrentPaymentTool);
        Assertions.assertTrue(id.isPresent());
        recurrentPaymentTool.setId(id.get());
        assertEquals(recurrentPaymentTool, recurrentPaymentToolDao.get(recurrentPaymentTool.getRecurrentPaymentToolId()));
        recurrentPaymentToolDao.updateNotCurrent(recurrentPaymentTool.getId());

        assertThrows(NotFoundException.class, () -> recurrentPaymentToolDao.get(recurrentPaymentTool.getRecurrentPaymentToolId()));
    }
}
