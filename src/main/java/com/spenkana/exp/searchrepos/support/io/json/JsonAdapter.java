package com.spenkana.exp.searchrepos.support.io.json;

import com.spenkana.exp.searchrepos.support.events.Event;
import com.spenkana.exp.searchrepos.support.events.EventHandlerFunction;
import com.spenkana.exp.searchrepos.support.result.Result;
import com.spenkana.exp.searchrepos.support.stateMachine.EventHandler;
import com.spenkana.exp.searchrepos.support.stateMachine.StateMachine
    .VisitorContext;

import java.util.ArrayList;
import java.util.List;

import static com.spenkana.exp.searchrepos.support.result.Result.success;

public class JsonAdapter {

    public static abstract class ProviderBindings {
        public abstract JsonTokenBindings bindTokens();
    }
    public static class JsonParseEvent extends Event{
        public JsonParseEvent(Object eventType, Object payload) {
            super(eventType, payload);
        }
    }

    public static abstract class JsonElement{}

    //todo create a Payload<T> class that adds timestamp
    //todo create a Map<EventId, List<Payload>> in VisitorContext
    //TODO add payload instance to list for event id
    public Result<EventHandler> makeFieldExtractor(
        String eventId, String fieldName) {
        EventHandlerFunction<FieldInfo> function =
            (FieldInfo payload, VisitorContext vc) -> {
                savePayload(eventId, payload, vc);
                return success();
            };
        EventHandler<FieldInfo> eventHandler = new EventHandler(
            fieldName, function, eventId);
        return success(eventHandler);
    }

    private static void savePayload(String eventId,
                             FieldInfo payload,
                             VisitorContext vc) {
        List<FieldInfo> payloads;
        Result<Object> result = vc.safelyGet(eventId);

        if (result.succeeded()) {
            payloads = (List<FieldInfo>) result.output;
        } else {
            payloads = new ArrayList<>();
            vc.put(eventId, payloads);
        }
        payloads.add(payload);
    }

}
