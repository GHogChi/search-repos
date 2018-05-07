package com.spenkana.exp.searchrepos.support.events;

import com.spenkana.exp.searchrepos.support.Id;

public class Event<T,U> {
    public final T eventType;
    public final U payload;

    public Event(T eventType, U payload) {
        this.eventType = eventType;
        this.payload = payload;
    }

}
