package io.funraise.dm.blitz;

public interface Converter<S, D> extends ma.glasnost.orika.Converter<S, D> {
    default boolean skipMapping() {
        return false;
    }
}
