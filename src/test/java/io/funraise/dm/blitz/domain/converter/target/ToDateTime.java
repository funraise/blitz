package io.funraise.dm.blitz.domain.converter.target;

import java.time.OffsetDateTime;

public class ToDateTime {

    private OffsetDateTime dateTime;

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public ToDateTime setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

}
