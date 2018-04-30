package com.spenkana.exp.searchrepos.support.result;


import java.io.Serializable;
import java.util.Objects;

/**
 * A monad to be used as a return type for functions and methods. It allows
 * for clean and informative failures and facilitates exception-free coding.
 *
 * Generic type T is the type of the object to be returned: the output of the
 * function(/method). If there is no object, use "Void".
 * Generic type E is the type of the error (if any), which must extend
 * SafeError.
 * It would be nice to eliminate nulls, but in one case null will be returned:
 * when output is requested from a successful Result with an output type of
 * Void. (An earlier version threw exceptions for invalid requests such as
 * the error object for a successful message. Nulls are better.)
 * <p>
 * @see SafeError
 *
 * @see <a href="http://www.lighterra.com/papers/exceptionsharmful/">Exception
 * Handling Considered Harmful
 * </a>
 */
public class Result<T, E extends SafeError> implements Serializable {
    private final boolean ok;
    private final T output;
    private final E error;

    /**
     * For serialization only.
     */
    public Result() {
        ok = false;
        output = null;
        error = (E) SimpleError.NOT_AN_ERROR;
    }

    private Result(
        E error) {
        this.error = error;
        ok = false;
        output = null;
    }

    private Result(T output) {
        this.output = output;
        ok = true;
        error = (E) SimpleError.NOT_AN_ERROR;
    }

    public boolean succeeded() {
        return ok;
    }

    public boolean failed() {
        return !ok;
    }

    public static <T, U extends SafeError> Result<T, U> success(T output) {
        return new Result<>(output);
    }

    public static <Void, E extends SafeError> Result<Void, E> success() {
        return new Result<>();
    }

    public static <T, E extends SafeError> Result<T,E> failure(E error){
        return new Result(error);
    }

    public static <T> Result<T,SimpleError> failure(String msg){
        return new Result(new SimpleError(msg));
    }

    public static <T> Result<T,ExceptionalError> failure(Exception e){
        return new Result(new ExceptionalError(e));
    }

    public T getOutput() {
            return output;
    }

    public String getErrorMessage() {
            return error.message();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?, ?> result = (Result<?, ?>) o;
        return ok == result.ok &&
            Objects.equals(output, result.output) &&
            Objects.equals(error, result.error);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ok, output, error);
    }
}

