package io.funraise.dm.blitz.domain.complete.mapping;


import io.funraise.dm.blitz.annotation.Mapping;
import io.funraise.dm.blitz.annotation.MappingTo;
import io.funraise.dm.blitz.domain.complete.source.FromPerson;
import io.funraise.dm.blitz.domain.complete.target.ToPerson;
import io.funraise.dm.blitz.service.UnitConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.inject.Inject;

/**
 * Created by jackson.brodeur on 8/18/15.
 */

@MappingTo(ToPerson.class)
public class ToPersonMapping {


    @Inject
    private UnitConverter uc;

    @Mapping(originalClass = FromPerson.class)
    public Map<String, Function<FromPerson, ?>> getMapping() {
        Map<String, Function<FromPerson, ?>> map = new HashMap<>();
        map.put("weightKgs", (FromPerson fp) -> uc.lbsToKgs(fp.getWeightLbs()));
        map.put("heightCentimeters", (FromPerson fp) -> (int) uc.inchesToCentimeters(fp.getHeightInches()));
        map.put("job", (FromPerson fp) -> fp.getJob());

        return map;
    }
}
