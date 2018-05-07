package com.spenkana.exp.searchrepos.support.stateMachine;

import static com.spenkana.exp.searchrepos.support.result.Result.success;

//todo add name field, make a generic cla0ss Id<T> and use int for performance
/**
 * A structured collection of event handlers, itself a handler for its
 * defined start event.
 * Handling the start event:
 * Places itself on the handler group stack
 *
 */
public class ActiveHandlerGroup extends EventHandler<String> {
    public final String endEventId;
    public final EventHandler[] handlerSequence;

    public ActiveHandlerGroup(String id,
                              EventHandler... handlerSequence) {
        super(id, (payload, visitorContext) -> success(), id+".start");
        this.endEventId = id+".end";
        this.handlerSequence = handlerSequence;
    }
}
