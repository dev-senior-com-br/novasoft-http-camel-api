package br.com.senior.novasoft.http.camel.Utils.Enums;

public enum ServiceEnum {

    GEN("GEN"), //
    CUENTA("cuenta"), //
    CXC("CXC"); //

    public String path;

    ServiceEnum(String path) {
        this.path = path;
    }

}