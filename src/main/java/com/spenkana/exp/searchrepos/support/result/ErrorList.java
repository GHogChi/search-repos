package com.spenkana.exp.searchrepos.support.result;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Mutable list of Error objects.
 * Any subclass instances of SafeError are accepted - does not require
 * uniformity.
 * @see SafeError
 */
public class ErrorList {
    private final List<SafeError> errors;

    public ErrorList(
        List<SafeError> errors) {
        this.errors = errors;
    }

    public ErrorList(){
        errors = new ArrayList<>();
    }

    public void add(SafeError error){
        errors.add(error);
    }

    public void add(String errorMessage){
        errors.add(new SimpleError(errorMessage));
    }

    public void add(Exception e){
        errors.add(new ExceptionalError(e));
    }
    public String message() {
        return String.join(",\n",
            errors.stream().map(e->e.message).collect(toList()));
    }

    public int errorCount() {
        return errors.size();
    }

    public boolean hasErrors() {
        return errorCount() > 0;
    }

    public SafeError asError() {
        return new SimpleError(message());
    }
}
