package dev.vality.newway.service;

import dev.vality.damsel.payment_processing.EventPayload;
import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.geck.common.util.TypeUtil;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.newway.TestData;
import dev.vality.newway.dao.invoicing.iface.CashFlowDao;
import dev.vality.newway.dao.invoicing.iface.ChargebackDao;
import dev.vality.newway.dao.invoicing.iface.PaymentDao;
import dev.vality.newway.domain.enums.PaymentChangeType;
import dev.vality.newway.domain.tables.pojos.Chargeback;
import dev.vality.newway.domain.tables.pojos.Payment;
import dev.vality.newway.factory.MachineEventCopyFactory;
import dev.vality.newway.handler.event.stock.impl.invoicing.InvoicingHandler;
import dev.vality.newway.handler.event.stock.impl.invoicing.chargeback.*;
import dev.vality.newway.mapper.AbstractInvoicingMapper;
import dev.vality.newway.model.InvoiceWrapper;
import dev.vality.sink.common.parser.impl.MachineEventParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class InvoicingServiceTest {

    private final List<AbstractInvoicingMapper<InvoiceWrapper>> wrongHandlers = new ArrayList<>();
    private final List<AbstractInvoicingMapper<InvoiceWrapper>> rightHandlers = new ArrayList<>();

    @MockBean
    private InvoiceBatchService invoiceBatchService;
    @MockBean
    private PaymentBatchService paymentBatchService;
    @Mock
    private MachineEventCopyFactory<Chargeback, Integer> machineEventCopyFactory;
    @Mock
    private PaymentDao paymentDao;
    @Mock
    private CashFlowService cashFlowService;
    @Mock
    private MachineEventParser parser;

    @BeforeEach
    public void init() {
        AbstractInvoicingMapper wrong = mock(AbstractInvoicingMapper.class);
        when(wrong.accept(any())).thenReturn(false);
        wrongHandlers.add(wrong);

        AbstractInvoicingMapper right = mock(AbstractInvoicingMapper.class);
        when(right.accept(any())).thenReturn(true);
        rightHandlers.add(right);

        when(paymentDao.get(any(), any())).thenReturn(dev.vality.testcontainers.annotations.util.RandomBeans.random(Payment.class));

        when(machineEventCopyFactory.create(any(), any(), any(), any())).thenReturn(new Chargeback());
        when(machineEventCopyFactory.create(any(), any(), any(), any(), any())).thenReturn(new Chargeback());
    }

    @Test
    public void handleEmptyChanges() {
        InvoicingService invoicingService =
                new InvoicingService(new ArrayList<>(), rightHandlers, new ArrayList<>(), invoiceBatchService,
                        paymentBatchService, parser);

        EventPayload eventPayload = new EventPayload();
        when(parser.parse(any())).thenReturn(eventPayload);

        invoicingService.handleEvents(Collections.singletonList(new MachineEvent()));

        verify(rightHandlers.get(0), times(0)).accept(any());
    }

    @Test
    public void handlerSupportsInvoicing() {
        InvoicingService invoicingService =
                new InvoicingService(new ArrayList<>(), rightHandlers, new ArrayList<>(), invoiceBatchService,
                        paymentBatchService, parser);

        MachineEvent message = new MachineEvent();

        EventPayload eventPayload = new EventPayload();
        eventPayload.setInvoiceChanges(Collections.singletonList(new InvoiceChange()));
        when(parser.parse(any())).thenReturn(eventPayload);

        invoicingService.handleEvents(Collections.singletonList(message));

        verify(rightHandlers.get(0), times(1)).accept(any());
        verify(rightHandlers.get(0), times(1)).map(any(), any(), any(), any());
    }

    @Test
    public void handlerNotSupportInvoicing() {
        InvoicingService invoicingService =
                new InvoicingService(new ArrayList<>(), wrongHandlers, new ArrayList<>(), invoiceBatchService,
                        paymentBatchService, parser);

        EventPayload eventPayload = new EventPayload();
        eventPayload.setInvoiceChanges(Collections.singletonList(new InvoiceChange()));
        when(parser.parse(any())).thenReturn(eventPayload);

        invoicingService.handleEvents(Collections.singletonList(new MachineEvent()));

        verify(wrongHandlers.get(0), times(1)).accept(any());
        verify(wrongHandlers.get(0), times(0)).map(any(), any(), any(), any());
    }

    @Test
    public void handlerInvoicePaymentChargebackCreated() {
        ChargebackDao chargebackDao = mock(ChargebackDao.class);
        CashFlowDao cashFlowDao = mock(CashFlowDao.class);

        EventPayload eventPayload = new EventPayload();
        InvoiceChange invoiceChange = TestData.buildInvoiceChangeChargebackCreated();
        eventPayload.setInvoiceChanges(Collections.singletonList(invoiceChange));
        when(parser.parse(any())).thenReturn(eventPayload);

        List<InvoicingHandler> handlers = chargebackHandlers(chargebackDao, cashFlowDao, paymentDao);
        InvoicingService invoicingService =
                new InvoicingService(handlers, wrongHandlers, new ArrayList<>(), invoiceBatchService,
                        paymentBatchService, parser);
        MachineEvent machineEvent = buildMachineEvent();
        invoicingService.handleEvents(Collections.singletonList(machineEvent));

        Mockito.verify(chargebackDao, only()).save(any(Chargeback.class));
    }

    @Test
    public void handlerInvoicePaymentChargebackStatusChanged() {
        ChargebackDao chargebackDao = mockChargebackDao();
        CashFlowDao cashFlowDao = mock(CashFlowDao.class);

        EventPayload eventPayload = new EventPayload();
        InvoiceChange invoiceChange = TestData.buildInvoiceChangeChargebackStatusChanged();
        eventPayload.setInvoiceChanges(Collections.singletonList(invoiceChange));
        when(parser.parse(any())).thenReturn(eventPayload);

        List<InvoicingHandler> handlers = chargebackHandlers(chargebackDao, cashFlowDao, paymentDao);
        InvoicingService invoicingService =
                new InvoicingService(handlers, wrongHandlers, new ArrayList<>(), invoiceBatchService,
                        paymentBatchService, parser);

        MachineEvent machineEvent = buildMachineEvent();
        invoicingService.handleEvents(Collections.singletonList(machineEvent));

        Mockito.verify(chargebackDao, times(1)).save(any(Chargeback.class));
        Mockito.verify(chargebackDao, times(1)).updateNotCurrent(anyLong());
        Mockito.verify(cashFlowService, times(1)).save(anyLong(), anyLong(), any(PaymentChangeType.class));
    }

    @Test
    public void handlerInvoicePaymentChargebackLevyChanged() {
        ChargebackDao chargebackDao = mockChargebackDao();
        CashFlowDao cashFlowDao = mock(CashFlowDao.class);

        EventPayload eventPayload = new EventPayload();
        InvoiceChange invoiceChange = TestData.buildInvoiceChangeChargebackLevyChanged();
        eventPayload.setInvoiceChanges(Collections.singletonList(invoiceChange));
        when(parser.parse(any())).thenReturn(eventPayload);

        List<InvoicingHandler> handlers = chargebackHandlers(chargebackDao, cashFlowDao, paymentDao);
        InvoicingService invoicingService =
                new InvoicingService(handlers, wrongHandlers, new ArrayList<>(), invoiceBatchService,
                        paymentBatchService, parser);

        MachineEvent machineEvent = buildMachineEvent();
        invoicingService.handleEvents(Collections.singletonList(machineEvent));

        Mockito.verify(chargebackDao, times(1)).save(any(Chargeback.class));
        Mockito.verify(chargebackDao, times(1)).updateNotCurrent(anyLong());
        Mockito.verify(cashFlowService, times(1)).save(anyLong(), anyLong(), any(PaymentChangeType.class));
    }

    @Test
    public void handlerInvoicePaymentChargebackStageChanged() {
        ChargebackDao chargebackDao = mockChargebackDao();
        CashFlowDao cashFlowDao = mock(CashFlowDao.class);

        EventPayload eventPayload = new EventPayload();
        InvoiceChange invoiceChange = TestData.buildInvoiceChangeChargebackStageChanged();
        eventPayload.setInvoiceChanges(Collections.singletonList(invoiceChange));
        when(parser.parse(any())).thenReturn(eventPayload);
        List<InvoicingHandler> handlers = chargebackHandlers(chargebackDao, cashFlowDao, paymentDao);
        InvoicingService invoicingService =
                new InvoicingService(handlers, wrongHandlers, new ArrayList<>(), invoiceBatchService,
                        paymentBatchService, parser);

        MachineEvent machineEvent = buildMachineEvent();
        invoicingService.handleEvents(Collections.singletonList(machineEvent));

        Mockito.verify(chargebackDao, times(1)).save(any(Chargeback.class));
        Mockito.verify(chargebackDao, times(1)).updateNotCurrent(anyLong());
        Mockito.verify(cashFlowService, times(1)).save(anyLong(), anyLong(), any(PaymentChangeType.class));
    }

    @Test
    public void handlerInvoicePaymentChargebackCashFlowChanged() {
        ChargebackDao chargebackDao = mockChargebackDao();
        CashFlowDao cashFlowDao = mock(CashFlowDao.class);

        EventPayload eventPayload = new EventPayload();
        InvoiceChange invoiceChange = TestData.buildInvoiceChangeChargebackCashFlowChanged();
        eventPayload.setInvoiceChanges(Collections.singletonList(invoiceChange));
        when(parser.parse(any())).thenReturn(eventPayload);
        List<InvoicingHandler> handlers = chargebackHandlers(chargebackDao, cashFlowDao, paymentDao);
        InvoicingService invoicingService =
                new InvoicingService(handlers, wrongHandlers, new ArrayList<>(), invoiceBatchService,
                        paymentBatchService, parser);

        MachineEvent machineEvent = buildMachineEvent();
        invoicingService.handleEvents(Collections.singletonList(machineEvent));

        Mockito.verify(chargebackDao, times(1)).save(any(Chargeback.class));
        Mockito.verify(chargebackDao, times(1)).updateNotCurrent(anyLong());
        Mockito.verify(cashFlowService, times(1)).save(anyLong(), anyLong(), any(PaymentChangeType.class));
        Mockito.verify(cashFlowDao, times(1)).save(anyList());
    }

    @Test
    public void handlerInvoicePaymentChargebackBodyChanged() {
        ChargebackDao chargebackDao = mockChargebackDao();
        CashFlowDao cashFlowDao = mock(CashFlowDao.class);

        EventPayload eventPayload = new EventPayload();
        InvoiceChange invoiceChange = TestData.buildInvoiceChangeChargebackBodyChanged();
        eventPayload.setInvoiceChanges(Collections.singletonList(invoiceChange));
        when(parser.parse(any())).thenReturn(eventPayload);
        List<InvoicingHandler> handlers = chargebackHandlers(chargebackDao, cashFlowDao, paymentDao);
        InvoicingService invoicingService =
                new InvoicingService(handlers, wrongHandlers, new ArrayList<>(), invoiceBatchService,
                        paymentBatchService, parser);

        MachineEvent machineEvent = buildMachineEvent();
        invoicingService.handleEvents(Collections.singletonList(machineEvent));

        Mockito.verify(chargebackDao, times(1)).save(any(Chargeback.class));
        Mockito.verify(chargebackDao, times(1)).updateNotCurrent(anyLong());
        Mockito.verify(cashFlowService, times(1)).save(anyLong(), anyLong(), any(PaymentChangeType.class));
    }

    private ChargebackDao mockChargebackDao() {
        ChargebackDao chargebackDao = mock(ChargebackDao.class);
        when(chargebackDao.get(anyString(), anyString(), anyString()))
                .thenReturn(dev.vality.testcontainers.annotations.util.RandomBeans.random(Chargeback.class));
        when(chargebackDao.save(any(Chargeback.class))).thenReturn(Optional.of(1L));
        return chargebackDao;
    }

    private List<InvoicingHandler> chargebackHandlers(ChargebackDao chargebackDao, CashFlowDao cashFlowDao,
                                                      PaymentDao paymentDao) {
        return Arrays.asList(
                new InvoicePaymentChargebackStageChangedHandler(chargebackDao, cashFlowService,
                        machineEventCopyFactory),
                new InvoicePaymentChargebackBodyChangedHandler(chargebackDao, cashFlowService, machineEventCopyFactory),
                new InvoicePaymentChargebackCashFlowChangedHandler(chargebackDao, cashFlowService, cashFlowDao,
                        machineEventCopyFactory),
                new InvoicePaymentChargebackCreatedHandler(chargebackDao, paymentDao, machineEventCopyFactory),
                new InvoicePaymentChargebackLevyChangedHandler(chargebackDao, cashFlowService, machineEventCopyFactory),
                new InvoicePaymentChargebackStatusChangedHandler(chargebackDao, cashFlowService,
                        machineEventCopyFactory)
        );
    }

    private MachineEvent buildMachineEvent() {
        MachineEvent machineEvent = new MachineEvent();
        machineEvent.setSourceId("testSourceId");
        machineEvent.setCreatedAt(TypeUtil.temporalToString(Instant.now()));

        return machineEvent;
    }

}
