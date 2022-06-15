package br.com.senior.novasoft.http.camel.utils.enums;

import lombok.Getter;

@Getter
public enum FromToFieldsAccountEnum {
    codCli("Codigo de cliente", "CodCli"),
    nomCli("Nombre / Razón Social", "NomCli"),
    nom1Cli("Nombre / Razón Social", "Nom1Cli"),
    nitCli("NIT", "NitCli"),
    codCiu("Ciudad", "CodCiu"),
    codDep("Departamento", "CodDep"),
    codPai("País", "CodPai"),
    di1Cli("Dirección", "Di1Cli"),
    di2Cli("Distrito", "Di2Cli"),
    te1Cli("Teléfono", "Te1Cli"),
    tipCli("Tipo de cuenta", "TipCli"),
    eMail("Correo electrónico general", "EMail"),
    ap1Cli("Nombre Corto", "Ap1Cli"),
    tipPer("Tipo de persona", "TipPer"),
    estCli("Activo", "EstCli"),
    pagWeb("Sitio web", "PagWeb");
    private final String name;
    private final String errorName;

    FromToFieldsAccountEnum(String name, String errorName) {
        this.name = name;
        this.errorName = errorName;
    }
}
