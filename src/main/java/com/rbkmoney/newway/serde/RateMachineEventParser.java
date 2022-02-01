package com.rbkmoney.newway.serde;

import dev.vality.sink.common.parser.impl.MachineEventParser;
import dev.vality.sink.common.serialization.BinaryDeserializer;
import com.rbkmoney.xrates.rate.Change;
import org.springframework.stereotype.Component;

@Component
public class RateMachineEventParser extends MachineEventParser<Change> {

    public RateMachineEventParser(BinaryDeserializer<Change> deserializer) {
        super(deserializer);
    }
}
