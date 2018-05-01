package com.spenkana.exp.searchrepos.support.result;

/**
 * Base class for error information.
 * Subclasses can provide arbitrary extractable information
 */
public abstract class SafeError {
    public static final String NO_ERROR = "No error";
    public final String message;

    protected SafeError(String message) {
        this.message = message;
    }
}
