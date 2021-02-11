package io.funraise.dm.blitz.domain.remap_orika.source;

public class FromPosition {

    private String name;

    public String getName() {
        return name;
    }

    public FromPosition setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "[name: " + name + "]";
    }
}
