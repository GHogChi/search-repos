package com.spenkana.exp.searchrepos.support.result;

import org.codehaus.plexus.util.StringUtils;

/**
 * Use this class when you need exception-specific info like stack traces.
 * NOTE: SimpleError will accept an Exception and build an error message
 * containing the exception class and message - this should be sufficient for
 * most cases.
 * @see SimpleError
 */
public class ExceptionalError extends SafeError{
    public final Exception exception;
    public final String contextInfo;

    public ExceptionalError(Exception exception, String contextInfo) {
        super(buildMessage(exception, contextInfo));
        this.exception = exception;
        this.contextInfo = contextInfo;
    }

    public ExceptionalError(Exception exception) {
        super(buildMessage(exception, ""));
        this.exception = exception;
        this.contextInfo = "";
    }


    private static String buildMessage(Exception exception, String contextInfo) {
        final String message = exception.getLocalizedMessage()
            + (!StringUtils.isEmpty(contextInfo)
                ? " Context Info: " + contextInfo : ""
        );
        return message;
    }
}
