package io.funraise.dm.blitz.domain.remap_orika.target;

public class ToPlayer {

    private ToPosition position;

    public ToPosition getPosition() {
        return position;
    }

    public ToPlayer setPosition(ToPosition position) {
        this.position = position;
        return this;
    }

    @Override
    public String toString() {
        return "[position: " + position + "]";
    }
}
