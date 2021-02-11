package io.funraise.dm.blitz.domain.fluent_setters.target;

public class ToDriver {

    private ToCar car;

    public ToCar getCar() {
        return car;
    }

    public ToDriver setCar(ToCar car) {
        this.car = car;
        return this;
    }
}
