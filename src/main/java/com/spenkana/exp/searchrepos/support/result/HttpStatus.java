package com.spenkana.exp.searchrepos.support.result;

import java.util.HashMap;

import static com.spenkana.exp.searchrepos.support.result.Result.failure;

public enum HttpStatus {
    SUCCESS(200);


    private static HashMap<Integer, HttpStatus> statusesByCode;
    public final int code;

    HttpStatus(int code) {

        this.code = code;
    }

    private static void register(HttpStatus status){
        if (statusesByCode == null){
            statusesByCode = new HashMap<Integer, HttpStatus>();
        }
    }

    public static Result<HttpStatus, SimpleError> forCode(int code){
        return (statusesByCode.containsKey(code))
            ? Result.success(statusesByCode.get(code))
            : failure("No HTTP status registered for code "+code);
    }
}
