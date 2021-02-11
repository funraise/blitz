package io.funraise.dm.blitz.domain.complete.mapping;

import io.funraise.dm.blitz.annotation.Mapping;
import io.funraise.dm.blitz.annotation.MappingTo;
import io.funraise.dm.blitz.domain.complete.source.FromJob;
import io.funraise.dm.blitz.domain.complete.target.ToJob;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by jackson.brodeur on 8/19/15.
 */
@MappingTo(ToJob.class)
public class ToJobMapping {

    @Mapping(originalClass = FromJob.class)
    public Map<String, Function<FromJob, ?>> getMapping() {
        Map<String, Function<FromJob, ?>> map = new HashMap<>();
        map.put("title", (FromJob fj) -> fj.getPosition());
        map.put("monthlyPay", (FromJob fj) -> fj.getAnnualPay() / 12.0);
        return map;
    }
}
