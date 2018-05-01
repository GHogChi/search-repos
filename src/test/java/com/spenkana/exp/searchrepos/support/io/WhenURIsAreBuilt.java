package com.spenkana.exp.searchrepos.support.io;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class WhenURIsAreBuilt {

    @Test
    public void theyCanBeCloned() {
        URI original = buildOriginal();
        URI clone = new UriBuilder(original)
            .build()
            .getOutput();
        assertEquals(clone,original);
    }

    @Test
    public void queryCanBeUpdated() {
       URI original = buildOriginal();
       URI updated = new UriBuilder(original)
           .replaceQuery("q","x")
           .query("z","m")
           .build()
           .getOutput();
       assertNotEquals(updated, original);
    }

    private URI buildOriginal() {
        return new UriBuilder()
            .scheme("https")
            .authority("com.spenkana")
            .path("a","b")
            .query("x","xval")
            .query("y","yval")
            .build()
            .getOutput();
    }


}
