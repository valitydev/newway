package dev.vality.newway.service;

import dev.vality.newway.model.InvoiceWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceBatchService {

    private final InvoiceWrapperService invoiceWrapperService;

    // TODO: кандидат на удаление, лишнее звено

    public void process(List<InvoiceWrapper> invoiceWrappers) {
        log.info("Start processing of invoice batch, size={}", invoiceWrappers.size());
        invoiceWrapperService.save(invoiceWrappers);
        log.info("End processing of invoice batch");
    }
}
