package io.funraise.dm.blitz.domain.cycle_collection.target;

import java.util.List;

public class FromEmployee {
    private String name;
    private List<FromEmployee> directReports;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FromEmployee> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(
        List<FromEmployee> directReports) {
        this.directReports = directReports;
    }

    @Override
    public String toString() {
        return "[name: " + name + ", directReports: " + directReports + "]";
    }
}
