package io.funraise.dm.blitz.domain.remap_orika.source;

import java.util.List;

public class FromCoach {

    List<FromPlayer> players;

    public List<FromPlayer> getPlayers() {
        return players;
    }

    public FromCoach setPlayers(
        List<FromPlayer> players) {
        this.players = players;
        return this;
    }

    @Override
    public String toString() {
        return "[players: " + players + "]";
    }
}
