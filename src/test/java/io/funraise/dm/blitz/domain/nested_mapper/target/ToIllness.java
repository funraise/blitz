package io.funraise.dm.blitz.domain.nested_mapper.target;

public class ToIllness {
    private String name;
    private ToTreatment treatment;

    public String getName() {
        return name;
    }

    public ToIllness setName(String name) {
        this.name = name;
        return this;
    }

    public ToTreatment getTreatment() {
        return treatment;
    }

    public ToIllness setTreatment(
        ToTreatment treatment) {
        this.treatment = treatment;
        return this;
    }

    @Override
    public String toString() {
        return "[name: " + name + ", treatment: " + treatment + "]";
    }
}
