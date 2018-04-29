package com.spenkana.exp.searchrepos.support.result;

/**
 * Base class for error information.
 * Subclasses can provide arbitrary extractable information
 * @param <T> the type of extractable information
 */
public abstract class SafeError<T> {
    public static final String NO_ERROR = "No error";
    public abstract String message();
    public abstract T data();
    public int errorCount() { return 1;}
}
