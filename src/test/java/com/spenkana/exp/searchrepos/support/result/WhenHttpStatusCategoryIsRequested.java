package com.spenkana.exp.searchrepos.support.result;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WhenHttpStatusCategoryIsRequested {

    @Test
    public void allStatusesHaveCorrectCategories() {
        EnumSet.allOf(HttpStatus.class)
            .forEach(status -> {
                HttpStatus category = status.category().output;
                assertEquals(
                    (status.code < 100) ? 0 : status.code / 100,
                    category.code);
            });
    }

}
