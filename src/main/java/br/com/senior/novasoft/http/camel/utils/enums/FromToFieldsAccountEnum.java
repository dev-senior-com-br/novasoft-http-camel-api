package br.com.senior.novasoft.http.camel.utils.enums;

import lombok.Getter;

@Getter
public enum FromToFieldsAccountEnum {
    codCli("Codigo de cliente"),
    nomCli("Nombre / Razón Social"),
    nom1Cli("Nombre / Razón Social"),
    nitCli("NIT"),
    codCiu("Ciudad"),
    codDep("Departamento"),
    codPai("País"),
    di1Cli("Dirección"),
    di2Cli("Distrito"),
    te1Cli("Teléfono"),
    tipCli("Tipo de cuenta"),
    eMail("Correo electrónico general"),
    ap1Cli("Nombre Corto"),
    tipPer("Tipo de persona"),
    estCli("Activo"),
    pagWeb("Sitio web")
    ;
    private String name;
    FromToFieldsAccountEnum(String name) {
        this.name = name;
    }
}
