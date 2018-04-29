package com.spenkana.exp.searchrepos.support.result;

/**
 * Use this class when you need exception-specific info like stack traces.
 * NOTE: SimpleError will accept an Exception and build an error message
 * containing the exception class and message - this should be sufficient for
 * most cases.
 * @see SimpleError
 */
public class ExceptionalError extends SafeError<Exception>{
    private final Exception exception;

    public ExceptionalError(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String message() {
        return exception.getLocalizedMessage();
    }

    @Override
    public Exception data() {
        return exception;
    }
}
