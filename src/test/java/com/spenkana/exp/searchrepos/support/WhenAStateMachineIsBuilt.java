package com.spenkana.exp.searchrepos.support;

import com.spenkana.exp.searchrepos.support.StateMachine.EventHandler;
import com.spenkana.exp.searchrepos.support.StateMachine.EventHandlersById;
import com.spenkana.exp.searchrepos.support.result.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.spenkana.exp.searchrepos.support.result.Result.success;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        StateMachine<String> machine = new StateMachine<>(NULL_PAYLOAD,
            new EventHandlersById());

        assertEquals(TOP_STRUCTURE_ID, machine.idCurrentStructure());
        assertTrue(machine.acceptEvent(
            new StateMachine.Event<>(startId, NULL_PAYLOAD))
            .succeeded());
        assertEquals(TOP_STRUCTURE_ID, machine.idCurrentStructure());
        assertTrue(machine.acceptEvent(
            new StateMachine.Event<>(endId, NULL_PAYLOAD))
            .succeeded());
        assertEquals(TOP_STRUCTURE_ID, machine.idCurrentStructure());
    }


    @Test
    public void handlerIsInvokedForEvent() {
        EventHandlersById handlers = new EventHandlersById();
        String hiworld = "hiworld";
        handlers.put(hiworld, new EventHandler(hiworld,
            (s, vc) -> {
                miscellany.put(hiworld, "hiworld: " + s);
                return success();
            },
            hiworld
        ));
        StateMachine<String> machine = new StateMachine<>("", handlers);

        Result<Void> result =
            machine.acceptEvent(new StateMachine.Event<>(hiworld, "loworld"));
        assertEquals("hiworld: loworld", miscellany.get(hiworld));
    }


}

