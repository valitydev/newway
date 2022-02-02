package dev.vality.newway.serde.deserializer;

import dev.vality.fistful.wallet.TimestampedChange;
import dev.vality.sink.common.serialization.impl.AbstractThriftBinaryDeserializer;
import org.springframework.stereotype.Service;

@Service
public class WalletChangeDeserializer extends AbstractThriftBinaryDeserializer<TimestampedChange> {

    @Override
    public TimestampedChange deserialize(byte[] bin) {
        return deserialize(bin, new TimestampedChange());
    }
}
