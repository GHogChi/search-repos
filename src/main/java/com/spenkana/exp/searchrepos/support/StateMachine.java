package com.spenkana.exp.searchrepos.support;

import com.spenkana.exp.searchrepos.support.result.Result;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiFunction;

import static com.spenkana.exp.searchrepos.support.StateMachine.Structure
    .NO_STRUCTURE;
import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;

/**
 * The state machine handles events
 *
 * @param <T> The event type.
 */
public class StateMachine<T> {
    public static final String TOP_STRUCTURE_ID = "top";
    public static final class EventHandlersById extends DelegatingMap<String,
        EventHandler>{}
    /**
     * A session context through which handlers can intercommunicate and
     * persist state.
     */
    public final class VisitorContext extends DelegatingMap<String, Object>{}
    private final VisitorContext visitorContext = new VisitorContext();
    private final Stack<EventHandlersById>
        activeEventHandlersByEventId = new Stack<>();
    public final class LifoStructures extends Stack<Structure>{}
    private final Stack<Structure> activeStructures = new LifoStructures();
    public final T NullPayload;
    public final Event<Void> COMPLETION_EVENT;
    public static final String NO_ID = "no id";

    public StateMachine(T nullPayload,
                        EventHandlersById initialActiveHandlersByEventId) {
        NullPayload = nullPayload;
        COMPLETION_EVENT = new Event("COMPLETION", NullPayload);
        activeEventHandlersByEventId.push(initialActiveHandlersByEventId);
        Structure<T> topStructure =
            new Structure<T>(TOP_STRUCTURE_ID, "startMachine",
                "stopMachine");
        activeStructures.push(topStructure);
    }

    public Result<Void> acceptEvent(Event<T> event) {
        String id = event.id;
        final EventHandlersById eventHandlerMap =
            activeEventHandlersByEventId.peek();
        if (eventHandlerMap.containsKey(id)) {
            EventHandler<T> handler = eventHandlerMap.get(id);
            Result<Void> result = handler.apply(event.payload, visitorContext);
            //todo broadcast an event maybe
            return result;
        }
        return success();
    }

    public String idCurrentStructure() {
        return activeStructures.peek().id;
    }

    public static class Event<T> {
        public final String id;
        public final T payload;

        protected Event(String id, T payload) {
            this.id = id;
            this.payload = payload;
        }

    }

    /**
     * The handler is bound to the event by its ID. When the handler is
     * visible to the machine (as an expected event handler) and an event
     * with a matching ID is received, the handler will be invoked.
     *
     * @param <T>
     */
    public static class EventHandler<T> implements
        BiFunction<T, Map<String, Object>, Result<Void>> {
        public final String id;
        public final BiFunction<T, Map<String, Object>, Result<Void>> function;
        public final String expectedEventId;

        public EventHandler(
            String id,
            BiFunction<T, Map<String, Object>, Result<Void>> function,
            String expectedEventId) {
            this.id = id;
            this.function = function;
            this.expectedEventId = expectedEventId;
        }

        @Override
        public Result<Void> apply(T payload,
                                  Map<String, Object> visitorContext) {
            return function.apply(payload, visitorContext);
        }

    }

    /**
     * A structured collection of event handlers, itself a handler for its
     * defined start event.
     * Handling the start event:
     * Places itself on the structure stack
     *
     * @param <T>
     */
    public static class Structure<T> extends EventHandler<T> {
        public static final Structure NO_STRUCTURE = new Structure<>(
            NO_ID, NO_ID, NO_ID
        );
        public final String endEventId;
        public final EventHandler<T>[] handlerSequence;

        public Structure(String id, String startEventId, String endEventId,
                         EventHandler<T>... handlerSequence) {
            super(id, (payload, visitorContext) -> success(), startEventId);
            this.endEventId = endEventId;
            this.handlerSequence = handlerSequence;
        }
    }

    public static class Context<T> {
        private final Stack<Structure> structureStack = new Stack<>();
        public final Map<String, Object> visitorContext = new HashMap<>();
        private final Structure topStructure;
        public EventHandler<T>[] handlersForCurrentlyExpectedEvents =
            new EventHandler[]{};
        private AbstractMap<String, Structure> structuresByStartEventId = new
            HashMap<>();

        public Context(Structure topStructure) {
            this.topStructure = topStructure;
        }

        public Structure exitStructure() {
            if (!structureStack.empty()) {
                return structureStack.pop();
            }
            return NO_STRUCTURE;
        }

        private Structure currentStructure() {
            return structureStack.empty() ? NO_STRUCTURE :
                structureStack.peek();
        }

        public Result<Void> enterStructureFor(String eventId) {
            if (structuresByStartEventId.containsKey(eventId)) {
                structureStack.push(structuresByStartEventId.get(eventId));
                return success();
            }
            return failure("No structure starts with eventId");
        }

        public Result<Object> exitStructureFor(String id) {
            if (!structureStack.empty() &&
                structureStack.peek().endEventId.equals(id)) {
                return success(structureStack.pop());
            }
            return failure("Current structure does not end with eventId");
        }
    }

}
