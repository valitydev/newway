package dev.vality.newway.handler.wrapper;

import java.util.List;

public interface WrapperHandler<T> {

    boolean accept(List<T> wrappers);

    void saveBatch(List<T> wrappers);

}
