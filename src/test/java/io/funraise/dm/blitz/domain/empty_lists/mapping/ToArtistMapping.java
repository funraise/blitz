package io.funraise.dm.blitz.domain.empty_lists.mapping;

import io.funraise.dm.blitz.annotation.Mapping;
import io.funraise.dm.blitz.annotation.MappingTo;
import io.funraise.dm.blitz.domain.empty_lists.source.FromArtist;
import io.funraise.dm.blitz.domain.empty_lists.target.ToArtist;
import io.funraise.dm.blitz.domain.empty_lists.target.ToPainting;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@MappingTo(ToArtist.class)
public class ToArtistMapping {

    @Mapping(originalClass = FromArtist.class)
    public Map<String, Function<FromArtist, ?>> getMapping() {
        Map<String, Function<FromArtist, ?>> map = new HashMap<>();
        map.put("listOfPaintings", artist -> artist.getArtistPaintings()
                .stream()
                .map(
                    painting -> new ToPainting().setName(painting.getName())
                )
                .collect(Collectors.toList())
        );
        map.put("setOfPaintings", artist -> artist.getArtistPaintings()
            .stream()
            .map(
                painting -> new ToPainting().setName(painting.getName())
            )
            .collect(Collectors.toSet())
        );
        return map;
    }
}
