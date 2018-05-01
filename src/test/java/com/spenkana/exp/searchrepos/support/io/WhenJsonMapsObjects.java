package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.result.ExceptionalError;
import com.spenkana.exp.searchrepos.support.result.Result;
import org.junit.jupiter.api.Test;

import static com.spenkana.exp.searchrepos.support.io.Json.fromObject;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WhenJsonMapsObjects {

    @Test
    public void itMapsArraysCorrectly() {
        String[] obj = {"one","two"};

        Result<Json> mapResult = fromObject(obj);
        assertTrue(mapResult.succeeded());
        Json json = mapResult.getOutput();
        assertTrue(json.theClass == obj.getClass());
    }

}
