package io.funraise.dm.blitz.domain.cycle.target;

public class ToParent {

    private String name;
    private ToChild child;

    public String getName() {
        return name;
    }

    public ToParent setName(String name) {
        this.name = name;
        return this;
    }

    public ToChild getChild() {
        return child;
    }

    public ToParent setChild(ToChild child) {
        this.child = child;
        return this;
    }

    @Override
    public String toString() {
        return "[name: " + name + ", child: " + child + "]";
    }
}
