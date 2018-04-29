package com.spenkana.exp.searchrepos.support.result;

import java.text.MessageFormat;

public class SimpleError extends SafeError<String> {
    private final String message;
    public static final SimpleError NOT_AN_ERROR = new SimpleError(NO_ERROR);

    public SimpleError(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

    public String data() {
        return message();
    }

    public static SafeError fromException(Exception e){
        return new SimpleError(MessageFormat.format(
            "{0}: {1}", e.getClass().getName(),
            e.getLocalizedMessage()
        ));
    }
}
