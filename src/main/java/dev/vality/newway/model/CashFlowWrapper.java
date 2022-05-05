package dev.vality.newway.model;

import dev.vality.newway.domain.tables.pojos.CashFlow;
import dev.vality.newway.domain.tables.pojos.CashFlowLink;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CashFlowWrapper {

    private final CashFlowLink cashFlowLink;
    private final List<CashFlow> cashFlows;

}
