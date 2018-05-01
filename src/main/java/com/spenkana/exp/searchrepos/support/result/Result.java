package com.spenkana.exp.searchrepos.support.result;


import java.io.Serializable;
import java.util.Objects;

import static com.spenkana.exp.searchrepos.support.result.SafeError.NO_ERROR;
import static com.spenkana.exp.searchrepos.support.result.SimpleError
    .NOT_AN_ERROR;
//TODO solve: method signatures can't specify error type - causes casting
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
public class Result<T> implements Serializable {
    public final boolean ok;
    public final T output;
    public final SafeError error;
    public static final Object nullObject = new Object();

    /**
     * For serialization only.
     */
    public Result() {
        ok = false;
        output = null;
        error = (SafeError) NOT_AN_ERROR;
    }

    private Result(T output, SafeError error) {
        this.error = error;
        ok = error == NOT_AN_ERROR;
        this.output = output;
    }

    private Result(T output) {
        this.output = output;
        ok = true;
        error = (SafeError) NOT_AN_ERROR;
    }

    public boolean succeeded() {
        return ok;
    }

    public boolean failed() {
        return !ok;
    }

    public static <T> Result<T> success(T output) {
        return new Result(output, NOT_AN_ERROR);
    }

    public static Result<Void> success() {
        return new Result<>();
    }

    public static <T> Result<T> failure(SafeError error){
        return new Result(nullObject, error);
    }

    public static <T> Result<T> failure(String msg){
        return new Result(nullObject, new SimpleError(msg));
    }

    public static <T> Result<T> failure(Exception e){
        return new Result(new ExceptionalError(e, ""));
    }

    public T getOutput() {
            return output;
    }

    public String getErrorMessage(){
            return error.message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?> result = (Result<?>) o;
        return ok == result.ok &&
            Objects.equals(output, result.output) &&
            Objects.equals(error, result.error);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ok, output, error);
    }
}

