package io.funraise.dm.blitz.domain.cycle.source;

import io.funraise.dm.blitz.domain.cycle.target.ToChild;

public class FromChild {

    private String name;

    public ToChild getChild() {
        return child;
    }

    public void setChild(ToChild child) {
        this.child = child;
    }

    private ToChild child;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "[name: " + name + "]";
    }
}
