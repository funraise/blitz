package io.funraise.dm.blitz.domain.empty_lists.target;

public class ToPainting {

    private String name;

    public ToPainting setName(String name) {
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
