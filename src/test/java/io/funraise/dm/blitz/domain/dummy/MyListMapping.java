package io.funraise.dm.blitz.domain.dummy;

import io.funraise.dm.blitz.annotation.Mapping;
import io.funraise.dm.blitz.annotation.MappingTo;

import java.util.*;
import java.util.function.Function;

/**
 * Created by jackson.brodeur on 8/7/15.
 */

@MappingTo(MyList.class)
public class MyListMapping {

    @Mapping(originalClass = HashSet.class)
    public Map<String, Function<HashSet, ?>> getMapping() {
        Map<String, Function<HashSet, ?>> map = new HashMap<>();
        map.put("data", (HashSet s) -> {
            List<String> list = new ArrayList<String>();
            list.addAll(s);
            return list;
        });

        return map;
    }



}
