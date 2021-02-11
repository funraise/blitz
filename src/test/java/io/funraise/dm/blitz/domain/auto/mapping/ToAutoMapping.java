package io.funraise.dm.blitz.domain.auto.mapping;

import io.funraise.dm.blitz.annotation.Mapping;
import io.funraise.dm.blitz.annotation.MappingTo;
import io.funraise.dm.blitz.domain.auto.source.FromAuto;
import io.funraise.dm.blitz.domain.auto.target.ToAuto;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by jackson.brodeur on 8/20/15.
 */
@MappingTo(ToAuto.class)
public class ToAutoMapping {

    @Mapping(originalClass = FromAuto.class)
    public Map<String, Function<FromAuto, ?>> getMapping() {
        Map<String, Function<FromAuto, ?>> map = new HashMap<>();

        return map;
    }
}
