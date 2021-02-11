package io.funraise.dm.blitz.domain.cycle.target;

public class ToChild {

    private String name;

    private ToChild child;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ToChild getChild() {
        return child;
    }

    public void setChild(ToChild child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "[name: " + name + "]";
    }
}
