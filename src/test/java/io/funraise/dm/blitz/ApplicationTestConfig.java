package io.funraise.dm.blitz;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import io.funraise.dm.blitz.orika.OffsetDateTimeConverter;
import io.funraise.dm.blitz.service.DomainMapperConfig;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ApplicationTestConfig implements Module {

    @Override
    public void configure(Binder binder) { }

    @Provides
    DomainMapperConfig getDomainMapperConfig() {
        return new DomainMapperConfig(
            "io.funraise.dm.blitz.domain",
            Thread.currentThread().getContextClassLoader(),
            Stream.of(new OffsetDateTimeConverter()).collect(Collectors.toList())
        );
    }
}
