package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.io.json.jackson.Json;
import com.spenkana.exp.searchrepos.support.result.Result;
import org.junit.jupiter.api.Test;

import static com.spenkana.exp.searchrepos.support.io.json.jackson.Json
    .fromObject;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class WhenJsonMapsObjects {

    @Test
    public void itMapsArraysCorrectly() {
        String[] anArray = {"one","two", "three"};
        final Class<?> theClass = anArray.getClass();

        Result<Json> mapResult = fromObject(anArray);
        assertTrue(mapResult.succeeded());
        Json json = mapResult.getOutput();
        assertTrue(json.theClass ==  theClass);
        Result<Object> outputResult = json.asObject();
        assertTrue(outputResult.succeeded());
            String[] anotherArray = new String[0];
        try {
            anotherArray = (String[]) outputResult.getOutput();
        } catch (Exception e){
            fail(e.getLocalizedMessage());
        }
        assertTrue(anotherArray.length == anArray.length);
    }

}
