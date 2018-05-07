package com.spenkana.exp.searchrepos.support;

import com.spenkana.exp.searchrepos.support.events.Event;
import com.spenkana.exp.searchrepos.support.result.Result;

public interface EventConsumer<T,U> {
    Result<Void> acceptEvent(Event<T,U> event);
}
