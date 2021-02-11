package io.funraise.dm.blitz.domain.empty_lists.source;

import java.util.List;

public class FromArtist {

    List<FromPainting> artistPaintings;

    public List<FromPainting> getArtistPaintings() {
        return artistPaintings;
    }

    public FromArtist setArtistPaintings(List<FromPainting> artistPaintings) {
        this.artistPaintings = artistPaintings;
        return this;
    }

    @Override
    public String toString() {
        return "[artistPaintings: " + artistPaintings + "]";
    }
}
