package io.funraise.dm.blitz.domain.nested_mapper.target;

public class ToPatient {
    private String name;
    private ToIllness illness;

    public String getName() {
        return name;
    }

    public ToPatient setName(String name) {
        this.name = name;
        return this;
    }

    public ToIllness getIllness() {
        return illness;
    }

    public ToPatient setIllness(ToIllness illness) {
        this.illness = illness;
        return this;
    }

    @Override
    public String toString() {
        return "[name: " + name + ", illness: " + illness + "]";
    }
}
