package io.funraise.dm.blitz.domain.nested_mapper.mapping;

import io.funraise.dm.blitz.annotation.Mapping;
import io.funraise.dm.blitz.annotation.MappingTo;
import io.funraise.dm.blitz.domain.nested_mapper.source.FromTreatment;
import io.funraise.dm.blitz.domain.nested_mapper.target.ToTreatment;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@MappingTo(ToTreatment.class)
public class ToTreatmentMapping {

    @Mapping(originalClass = FromTreatment.class)
    public Map<String, Function<FromTreatment, ?>> getMapping() {
        Map<String, Function<FromTreatment, ?>> map = new HashMap<>();
        map.put("name", fromTreatment -> fromTreatment.getName().toUpperCase());
        return map;
    }
}
