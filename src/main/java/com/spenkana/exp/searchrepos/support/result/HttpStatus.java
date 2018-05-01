package com.spenkana.exp.searchrepos.support.result;

import java.util.HashMap;

import static com.spenkana.exp.searchrepos.support.result.Result.failure;

//TODO add all valid statuses
public enum HttpStatus {
    SUCCESS(200, "OK");


    private static HashMap<Integer, HttpStatus> statusesByCode;
    public final int code;
    public final String name;

    HttpStatus(int code, String name) {

        this.code = code;
        this.name = name;
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
}
