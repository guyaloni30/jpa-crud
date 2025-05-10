package com.example.jpacrud.crud;

import java.net.http.HttpResponse;

public class StatusErrorException extends RuntimeException {
    public final int code;

    public StatusErrorException(HttpResponse<String> response) {
        super(response.body());
        this.code = response.statusCode();
    }
}