package dev.vality.newway.handler.event.stock;

import dev.vality.geck.filter.Filter;
import org.apache.commons.lang3.NotImplementedException;

public interface Handler<T, E> {

    default boolean accept(T change) {
        return getFilter().match(change);
    }

    default void handle(T change, E event, Integer changeId) {
        throw new NotImplementedException("Override it!");
    }

    default void handle(T change, E event) {
        throw new NotImplementedException("Override it!");
    }

    Filter<T> getFilter();

}
