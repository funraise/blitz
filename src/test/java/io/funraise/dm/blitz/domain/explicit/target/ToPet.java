package io.funraise.dm.blitz.domain.explicit.target;

import io.funraise.dm.blitz.domain.explicit.source.FromPet;
import io.funraise.dm.blitz.domain.explicit.source.FromPet.Type;

public class ToPet {
    private String name;
    private FromPet.Type type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
