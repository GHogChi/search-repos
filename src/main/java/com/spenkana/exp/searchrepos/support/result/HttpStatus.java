package com.spenkana.exp.searchrepos.support.result;

import java.util.HashMap;

import static com.spenkana.exp.searchrepos.support.result.Result.failure;

/**
 * Improvement on Apache's version: provides categories.
 * Statuses and categories are from Wikipedia list.
 * @see <a href="https://en.wikipedia.org/wiki/List_of_HTTP_status_codes">Http Status Codes - Wikipedia</a>
 */
//TODO add all valid statuses from Wikipedia list
public enum HttpStatus {
    CATEGORY(0, "A general category of status codes "),
    INFORMATIONAL(1, "The request was received and understood"),
    SUCCESS(2,"The action requested by the client was received, understood " +
        "and accepted"),
    OK(200, "OK");


    private static HashMap<Integer, HttpStatus> statusesByCode;
    public final int code;
    public final String name;
    public final int categoryCode;

    HttpStatus(int code, String name) {

        this.code = code;
        this.name = name;
        categoryCode = (code < 100) ? 0 : code / 100;
        register(this);
    }

    private static void register(HttpStatus status){
        if (statusesByCode == null){
            statusesByCode = new HashMap<>();
        }
        statusesByCode.put(status.code, status);
    }

    public static Result<HttpStatus> forCode(int code){
        return (statusesByCode.containsKey(code))
            ? Result.success(statusesByCode.get(code))
            : failure("No HTTP status registered for code "+code);
    }

    public Result<HttpStatus> category(){
        return forCode(categoryCode);
    }
}
