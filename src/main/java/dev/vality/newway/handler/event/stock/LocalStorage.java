package dev.vality.newway.handler.event.stock;

import dev.vality.newway.model.InvoicingKey;

import java.util.HashMap;
import java.util.Map;

public class LocalStorage {
    private Map<InvoicingKey, Object> map = new HashMap<>();

    public Object get(InvoicingKey key) {
        return map.get(key);
    }

    public void put(InvoicingKey key, Object object) {
        map.put(key, object);
    }
}
