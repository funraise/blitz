package io.funraise.dm.blitz.domain.converter.source;

import java.time.LocalDateTime;

public class FromDateTime {

    private LocalDateTime dateTime;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public FromDateTime setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }
}
