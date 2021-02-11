package io.funraise.dm.blitz.domain.nested_mapper.source;

public class FromPatient {
    private String name;
    private FromIllness illness;

    public String getName() {
        return name;
    }

    public FromPatient setName(String name) {
        this.name = name;
        return this;
    }

    public FromIllness getIllness() {
        return illness;
    }

    public FromPatient setIllness(
        FromIllness illness) {
        this.illness = illness;
        return this;
    }

    @Override
    public String toString() {
        return "[name: " + name + ", illness: " + illness + "]";
    }
}
