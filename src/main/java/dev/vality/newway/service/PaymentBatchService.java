package dev.vality.newway.service;

import dev.vality.newway.model.PaymentWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentBatchService {

    private final PaymentWrapperService paymentWrapperService;

    // TODO: кандидат на удаление, если все-таки необходимо сортировать записи по порядку,
    //   то можно это предварительно делать здесь.
    //   Аналогичные изменения должны распространяться и на InvoiceBatchService

    public void process(List<PaymentWrapper> paymentWrappers) {
        log.info("Start processing of payment batch, size={}", paymentWrappers.size());
        paymentWrapperService.save(paymentWrappers);
        log.info("End processing of payment batch");
    }
}
