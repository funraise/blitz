package io.funraise.dm.blitz.domain.nested_mapper.target;

public class ToTreatment {

    private String name;

    public ToTreatment setName(String name) {
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
