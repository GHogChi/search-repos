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

    public String message() {
        return String.join(",",
            errors.stream().map(e->message()).collect(toList()));
    }

    public List<SafeError> data() {
        return errors;
    }

    public int errorCount() {
        return errors.size();
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }

    public SafeError asError() {
        return new SimpleError(message());
    }
}
