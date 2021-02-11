package io.funraise.dm.blitz;

import io.funraise.dm.blitz.service.DomainMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SafeConverter<S, D> implements ma.glasnost.orika.Converter<S, D> {

    private static final Logger log = LoggerFactory.getLogger(DomainMapper.class);

    private final ma.glasnost.orika.Converter<S, D> converter;

    public SafeConverter(ma.glasnost.orika.Converter<S, D> converter) {
        this.converter = converter;
    }

    @Override
    public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
        return converter.canConvert(sourceType, destinationType);
    }

    @Override
    public D convert(S source, Type<? extends D> destinationType, MappingContext mappingContext) {
        try {
            return converter.convert(source, destinationType, mappingContext);
        } catch (Exception e) {
            log.warn(String.format("Exception occurred while converting two types: %s and %s", source.getClass().getCanonicalName(), destinationType.getCanonicalName()), e);
            return null;
        }
    }

    @Override
    public void setMapperFacade(MapperFacade mapper) {
        converter.setMapperFacade(mapper);
    }

    @Override
    public Type<S> getAType() {
        return converter.getAType();
    }

    @Override
    public Type<D> getBType() {
        return converter.getBType();
    }
}
