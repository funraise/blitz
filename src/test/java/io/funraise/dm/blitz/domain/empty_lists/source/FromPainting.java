package io.funraise.dm.blitz.domain.empty_lists.source;

public class FromPainting {

    private String name;

    public String getName() {
        return name;
    }

    public FromPainting setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "[name: " + name + "]";
    }
}
