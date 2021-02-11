package io.funraise.dm.blitz.domain.explicit.mapping;

import io.funraise.dm.blitz.annotation.Mapping;
import io.funraise.dm.blitz.annotation.MappingTo;
import io.funraise.dm.blitz.domain.explicit.source.FromPet;
import io.funraise.dm.blitz.domain.explicit.target.ToPet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@MappingTo(ToPet.class)
public class ToPetMapping {

    @Mapping(originalClass = FromPet.class)
    public Map<String, Function<FromPet, ?>> mapping() {
        Map<String, Function<FromPet, ?>> mapping = new HashMap<>();
        mapping.put("name", FromPet::getNickname);
        mapping.put("type", FromPet::getPetType);
        return mapping;
    }
}
