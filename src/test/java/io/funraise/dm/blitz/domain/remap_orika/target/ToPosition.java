package io.funraise.dm.blitz.domain.remap_orika.target;

public class ToPosition {

    private String name;

    public ToPosition setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "[name: " + name + "]";
    }
}
