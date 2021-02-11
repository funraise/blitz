package io.funraise.dm.blitz.domain.orikaforce.mapping;

import io.funraise.dm.blitz.CustomMappingWrapper;
import io.funraise.dm.blitz.annotation.Mapping;
import io.funraise.dm.blitz.annotation.MappingTo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@MappingTo(io.funraise.dm.blitz.domain.orikaforce.target.Person.class)
public class PersonMapping {

    @Mapping(originalClass = io.funraise.dm.blitz.domain.orikaforce.source.Person.class)
    public Map<String, Function<io.funraise.dm.blitz.domain.orikaforce.source.Person, ?>> getMapping() {
        Map<String, Function<io.funraise.dm.blitz.domain.orikaforce.source.Person, ?>> map = new HashMap<>();
        map.put("name", (io.funraise.dm.blitz.domain.orikaforce.source.Person fa) -> fa.getName());
        map.put("address", (io.funraise.dm.blitz.domain.orikaforce.source.Person fa) -> CustomMappingWrapper.customMapping(fa).withOrika(CustomMappingWrapper.Orika.FORCE_ON));

        return map;
    }
}
