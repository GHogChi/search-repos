package com.spenkana.exp.searchrepos.support.result;

import java.text.MessageFormat;

//todo consider collapsing ExceptionalError into this class; create a
// NO_EXCEPTION exception
public class SimpleError extends SafeError {
    public static final SimpleError NOT_AN_ERROR = new SimpleError(NO_ERROR);

    public SimpleError(String message) {
        super(message);
    }


    public static SimpleError fromException(Exception e) {
        return new SimpleError(MessageFormat.format(
            "{0}: {1}", e.getClass()
                .getName(), e.getLocalizedMessage()
        ));
    }
}
