package io.funraise.dm.blitz;

import ma.glasnost.orika.Converter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.metadata.Type;

public class SafeConverterFactory implements ConverterFactory {

    private final ConverterFactory converterFactory;

    public SafeConverterFactory(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    @Override
    public void setMapperFacade(MapperFacade mapperFacade) {
        converterFactory.setMapperFacade(mapperFacade);
    }

    @Override
    public Converter<Object, Object> getConverter(Type<?> sourceType, Type<?> destinationType) {
        return converterFactory.getConverter(sourceType, destinationType);
    }

    @Override
    public Converter<Object, Object> getConverter(String converterId) {
        return converterFactory.getConverter(converterId);
    }

    @Override
    public <S, D> void registerConverter(Converter<S, D> converter) {
        converterFactory.registerConverter(new SafeConverter<>(converter));
    }

    @Override
    public <S, D> void registerConverter(String converterId, Converter<S, D> converter) {
        converterFactory.registerConverter(converterId, new SafeConverter<>(converter));
    }

    @Override
    public boolean hasConverter(String converterId) {
        return converterFactory.hasConverter(converterId);
    }

    @Override
    public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
        return converterFactory.canConvert(sourceType, destinationType);
    }
}
