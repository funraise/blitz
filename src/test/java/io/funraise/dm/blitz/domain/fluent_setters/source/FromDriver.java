package io.funraise.dm.blitz.domain.fluent_setters.source;

public class FromDriver {

    private FromCar car;

    public FromCar getCar() {
        return car;
    }

    public FromDriver setCar(FromCar car) {
        this.car = car;
        return this;
    }
}
