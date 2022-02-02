package dev.vality.newway.handler.dominant;

public interface DominantHandler<T> {

    boolean acceptAndSet(T change);

    void handle(T change, Long versionId);
}
