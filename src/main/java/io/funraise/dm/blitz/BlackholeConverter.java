package io.funraise.dm.blitz;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

/**
 * This converter acts as a fallback converter for conversions that would typically
 * throw an exception in Orika. This should be the converter with the lowest priority
 * to allow for application-level converters to override this behavior.
 *
 * The purpose of this converter is for performance reasons, it is to prevent generating stacktraces
 * when we know a particular conversion will always fail
 */
public class BlackholeConverter extends CustomConverter<Object, Object> {


    @Override
    public Object convert(Object source, ma.glasnost.orika.metadata.Type<?> destinationType, MappingContext mappingContext) {
        return null;
    }

    @Override
    public boolean canConvert(Type<?> sourceType, Type<?> destinationType) {
        return !sourceType.isString() && !destinationType.isString() &&
            !sourceType.isEnum() && !destinationType.isEnum() && (
                (isPrimitive(sourceType) && !isPrimitive(destinationType)) ||
                (!isPrimitive(sourceType) && isPrimitive(destinationType))
            );
    }

    private boolean isPrimitive(ma.glasnost.orika.metadata.Type<?> type) {
        return type.isPrimitive() || type.isPrimitiveWrapper();
    }
}
