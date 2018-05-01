package com.spenkana.exp.searchrepos.support;

/**
 * Allows functions that handle exceptions to be total.
 * @see <a href="https://en.wikipedia.org/wiki/Partial_function#Total_function">Total Function</a>
 */
public class NoException extends Exception {
    public NoException() {
        super("No exception");
    }
}
