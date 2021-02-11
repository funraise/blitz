package io.funraise.dm.blitz.domain.remap_orika.source;

public class FromPlayer {

    private FromPosition position;

    public FromPosition getPosition() {
        return position;
    }

    public FromPlayer setPosition(
        FromPosition position) {
        this.position = position;
        return this;
    }

    @Override
    public String toString() {
        return "[position: " + position + "]";
    }
}
