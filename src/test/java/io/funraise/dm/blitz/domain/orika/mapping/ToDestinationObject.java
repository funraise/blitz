package io.funraise.dm.blitz.domain.orika.mapping;

import io.funraise.dm.blitz.annotation.Mapping;
import io.funraise.dm.blitz.annotation.MappingTo;
import io.funraise.dm.blitz.domain.orika.source.SourceObject;
import io.funraise.dm.blitz.domain.orika.target.DestinationObject;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@MappingTo(DestinationObject.class)
public class ToDestinationObject {

    @Mapping(originalClass = SourceObject.class)
    public Map<String, Function<SourceObject, ?>> getMapping() {
        Map<String, Function<SourceObject, ?>> mapping = new HashMap<>();
        mapping.put("someField", SourceObject::getOtherField);
        return mapping;
    }
}
