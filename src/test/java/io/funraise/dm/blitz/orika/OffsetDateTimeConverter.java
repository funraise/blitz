package io.funraise.dm.blitz.orika;

import io.funraise.dm.blitz.CustomConverter;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

public class OffsetDateTimeConverter extends CustomConverter<LocalDateTime, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(LocalDateTime source, Type<? extends OffsetDateTime> destinationType, MappingContext mappingContext) {
        return OffsetDateTime.of(source, ZoneOffset.ofTotalSeconds(0));
    }
}
