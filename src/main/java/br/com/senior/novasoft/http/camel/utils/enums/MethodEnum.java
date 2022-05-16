package br.com.senior.novasoft.http.camel.utils.enums;

import lombok.Getter;

public enum MethodEnum {

    POST("POST"),
    PUT("PUT"),
    GET("GET"),
    DELETE("DELETE"),
    PATCH("PATCH");

    @Getter
    private String path;

    MethodEnum(String path) {
        this.path = path;
    }
}
