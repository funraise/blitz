package io.funraise.dm.blitz.domain.empty_lists.target;

import java.util.List;
import java.util.Set;

public class ToArtist {

    private List<ToPainting> listOfPaintings;
    private Set<ToPainting> setOfPaintings;

    public List<ToPainting> getListOfPaintings() {
        return listOfPaintings;
    }

    public ToArtist setListOfPaintings(
        List<ToPainting> listOfPaintings) {
        this.listOfPaintings = listOfPaintings;
        return this;
    }

    public Set<ToPainting> getSetOfPaintings() {
        return setOfPaintings;
    }

    public ToArtist setSetOfPaintings(Set<ToPainting> paintings) {
        this.setOfPaintings = paintings;
        return this;
    }

    @Override
    public String toString() {
        return "[listOfPaintings: " + listOfPaintings + ", setOfPaintings: " + setOfPaintings + "]";
    }
}
