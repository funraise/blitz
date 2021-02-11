package io.funraise.dm.blitz.domain.remap_orika.target;

import java.util.List;

public class ToCoach {

    private List<ToPlayer> athletes;

    public List<ToPlayer> getAthletes() {
        return athletes;
    }

    public ToCoach setAthletes(
        List<ToPlayer> athletes) {
        this.athletes = athletes;
        return this;
    }

    @Override
    public String toString() {
        return "[athletes: " + athletes + "]";
    }
}
