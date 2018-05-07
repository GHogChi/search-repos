package com.spenkana.exp.searchrepos.support;

import com.spenkana.exp.searchrepos.support.events.Event;
import com.spenkana.exp.searchrepos.support.io.json.JsonAdapter;
import com.spenkana.exp.searchrepos.support.io.json.JsonEventType;
import com.spenkana.exp.searchrepos.support.io.json.JsonAdapter.JsonElement;
import com.spenkana.exp.searchrepos.support.stateMachine.EventHandler;
import com.spenkana.exp.searchrepos.support.stateMachine.StateMachine;
import com.spenkana.exp.searchrepos.support.stateMachine.StateMachine
    .EventHandlersByEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.spenkana.exp.searchrepos.support.io.json.JsonEventType
    .STRUCTURE_INFO;
import static com.spenkana.exp.searchrepos.support.result.Result.success;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WhenAStateMachineIsBuilt {


    public static final String TOP_STRUCTURE_ID = "top";
    public static final String NULL_PAYLOAD = "nullPayload";
    private Map<String, Object> miscellany = new HashMap<>();

    @BeforeEach
    public void setup() {
        miscellany.clear(); ;
    }

    @Test
    public void itRunsFineOnEmpty() {
        String startId = "start", endId = "end";
        StateMachine<JsonEventType, JsonElement> machine = new
            StateMachine(NULL_PAYLOAD, new EventHandlersByEventType());

        assertEquals(TOP_STRUCTURE_ID, machine.idCurrentHandlerGroup());
        machine.accept(new Event(startId, NULL_PAYLOAD));
        assertEquals(TOP_STRUCTURE_ID, machine.idCurrentHandlerGroup());
        machine.accept(
            new JsonAdapter.JsonParseEvent(STRUCTURE_INFO, NULL_PAYLOAD)
        );
        assertEquals(TOP_STRUCTURE_ID, machine.idCurrentHandlerGroup());
    }


    @Test
    public void handlerIsInvokedForEvent() {
        EventHandlersByEventType handlers = new EventHandlersByEventType();
        String hiworld = "hiworld";
        handlers.put(hiworld, new EventHandler(hiworld,
            (s, vc) -> {
                miscellany.put(hiworld, "hiworld: " + s);
                return success();
            },
            hiworld
        ));
        StateMachine<JsonEventType, JsonElement> machine = new StateMachine("", handlers);

            machine.accept(new Event(hiworld, "loworld"));
        assertEquals("hiworld: loworld", miscellany.get(hiworld));
    }


}

