package io.funraise.dm.blitz.domain.nested_mapper.source;

public class FromIllness {
    private String name;
    private FromTreatment treatment;


    public String getName() {
        return name;
    }

    public FromIllness setName(String name) {
        this.name = name;
        return this;
    }

    public FromTreatment getTreatment() {
        return treatment;
    }

    public FromIllness setTreatment(
        FromTreatment treatment) {
        this.treatment = treatment;
        return this;
    }

    @Override
    public String toString() {
        return "[name: " + name + ", treatment: " + treatment + "]";
    }

}
