package br.com.senior.novasoft.http.camel.utils.enums;

import lombok.Getter;

public enum PrimitiveComplementEnum {

    PARAMETER("/"),
    FILTER("?");

    @Getter
    private String path;

    PrimitiveComplementEnum(String path) {
        this.path = path;
    }
}