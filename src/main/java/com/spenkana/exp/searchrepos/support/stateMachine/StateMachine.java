package com.spenkana.exp.searchrepos.support.stateMachine;

import com.spenkana.exp.searchrepos.support.DelegatingMap;
import com.spenkana.exp.searchrepos.support.events.Event;
import com.spenkana.exp.searchrepos.support.result.Result;

import java.util.Stack;
import java.util.function.Consumer;

/**
 * The state machine handles events
 *
 * @param <T> The event type.
 */
public class StateMachine<T, U> implements Consumer<Event<T, U>> {
    public static final String INITIAL_HANDLER_GROUP_ID = "top";

    public static final class EventHandlersByEventType
        extends DelegatingMap<String,
        EventHandler> {
    }

    /**
     * A session context through which handlers can intercommunicate and
     * persist state.
     */
    public static final class VisitorContext
        extends DelegatingMap<String, Object> {
    }
    private final VisitorContext visitorContext = new VisitorContext();

    private final Stack<EventHandlersByEventType>
        handlerMapStack = new Stack<>();

    public final class LifoStructures extends Stack<ActiveHandlerGroup> {
    }

    private final Stack<ActiveHandlerGroup> handlerGroups =
        new LifoStructures();
    public final T NullPayload;
    public final Event<String, Void> COMPLETION_EVENT;

    public StateMachine(T nullPayload,
                        EventHandlersByEventType
                            initialActiveHandlersByEventId) {
        NullPayload = nullPayload;
        COMPLETION_EVENT = new Event("COMPLETION", NullPayload);
        handlerMapStack.push(initialActiveHandlersByEventId);
        ActiveHandlerGroup initialHandlerGroup =
            new ActiveHandlerGroup(INITIAL_HANDLER_GROUP_ID
            );
        handlerGroups.push(initialHandlerGroup);
    }

    @Override
    public void accept(Event<T, U> event) {
        final EventHandlersByEventType eventHandlersByEventType =
            handlerMapStack.peek();
        if (eventHandlersByEventType.containsKey(event.eventType)) {
            EventHandler<T> handler =
                eventHandlersByEventType.get(event.eventType);
            Result<Void> result =
                handler.apply((T) event.payload, visitorContext);
            //todo broadcast an event maybe
        }
    }

    public String idCurrentHandlerGroup() {
        return handlerGroups.peek().id;
    }


}
