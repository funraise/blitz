package io.funraise.dm.blitz.domain.cycle_collection.source;

import java.util.List;

public class ToEmployee {
    private String name;
    private List<ToEmployee> directReports;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ToEmployee> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(
        List<ToEmployee> directReports) {
        this.directReports = directReports;
    }


    @Override
    public String toString() {
        return "[name: " + name + ", directReports: " + directReports + "]";
    }}
