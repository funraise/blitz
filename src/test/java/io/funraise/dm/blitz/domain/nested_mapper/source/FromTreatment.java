package io.funraise.dm.blitz.domain.nested_mapper.source;

public class FromTreatment {

    private String name;

    public String getName() {
        return name;
    }

    public FromTreatment setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "[name: " + name + "]";
    }
}
