package io.funraise.dm.blitz.domain.remap_orika.mapping;

import io.funraise.dm.blitz.annotation.Mapping;
import io.funraise.dm.blitz.annotation.MappingTo;
import io.funraise.dm.blitz.domain.remap_orika.source.FromCoach;
import io.funraise.dm.blitz.domain.remap_orika.target.ToCoach;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@MappingTo(ToCoach.class)
public class ToCoachMapping {

    @Mapping(originalClass = FromCoach.class)
    public Map<String, Function<FromCoach, ?>> getMapping() {
        Map<String, Function<FromCoach, ?>> map = new HashMap<>();
        map.put("athletes", FromCoach::getPlayers);
        return map;
    }
}
