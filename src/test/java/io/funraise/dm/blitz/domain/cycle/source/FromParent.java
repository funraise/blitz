package io.funraise.dm.blitz.domain.cycle.source;

public class FromParent {

    private String name;
    private FromChild child;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FromChild getChild() {
        return child;
    }

    public void setChild(FromChild child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "[name: " + name + ", child: " + child + "]";
    }

}
