package io.funraise.dm.blitz.domain.nested_mapper.mapping;

import io.funraise.dm.blitz.annotation.Mapping;
import io.funraise.dm.blitz.annotation.MappingTo;
import io.funraise.dm.blitz.domain.nested_mapper.source.FromPatient;
import io.funraise.dm.blitz.domain.nested_mapper.target.ToPatient;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@MappingTo(ToPatient.class)
public class ToPatientMapping {

    @Mapping(originalClass = FromPatient.class)
    public Map<String, Function<FromPatient, ?>> getMapping() {
        Map<String, Function<FromPatient, ?>> map = new HashMap<>();
        map.put("name", fromPatient -> fromPatient.getName().toUpperCase());
        return map;
    }
}

