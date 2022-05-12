package br.com.senior.novasoft.http.camel.utils.enums;

import lombok.Getter;

public enum MethodEnum {

    POST("post"),
    PUT("put"),
    GET("get"),
    DELETE("delete");

    @Getter
    private String path;

    MethodEnum(String path) {
        this.path = path;
    }
}
