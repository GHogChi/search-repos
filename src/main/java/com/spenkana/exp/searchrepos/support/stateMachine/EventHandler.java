package com.spenkana.exp.searchrepos.support.stateMachine;

import com.spenkana.exp.searchrepos.support.events.EventHandlerFunction;
import com.spenkana.exp.searchrepos.support.result.Result;

/**
 * The handler is bound to the event by its ID. When the handler is
 * visible to the machine (as an expected event handler) and an event
 * with a matching ID is received, the handler will be invoked.
 *
 * @param <T>
 */
public class EventHandler<T>  {
    public final String id;
    public final EventHandlerFunction function;
    public final String expectedEventId;

    public EventHandler(
        String id,
        EventHandlerFunction function,
        String expectedEventId) {
        this.id = id;
        this.function = function;
        this.expectedEventId = expectedEventId;
    }

    public Result<Void> apply(T payload,
                              StateMachine.VisitorContext visitorContext) {
        return (Result<Void>) function.apply(payload, visitorContext);
    }

}
