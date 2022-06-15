package br.com.senior.novasoft.http.camel.utils.enums;

import lombok.Getter;

@Getter
public enum FromToFieldsAccountEnum {
    COD_CLI("Codigo de cliente", "CodCli"),
    NOM_CLI("Nombre / Razón Social", "NomCli"),
    NOM1_CLI("Nombre / Razón Social", "Nom1Cli"),
    NIT_CLI("NIT", "NitCli"),
    COD_CIU("Ciudad", "CodCiu"),
    COD_DEP("Departamento", "CodDep"),
    COD_PAI("País", "CodPai"),
    DI1_CLI("Dirección", "Di1Cli"),
    DI2_CLI("Distrito", "Di2Cli"),
    TE1_CLI("Teléfono", "Te1Cli"),
    TIP_CLI("Tipo de cuenta", "TipCli"),
    E_MAIL("Correo electrónico general", "EMail"),
    AP1_CLI("Nombre Corto", "Ap1Cli"),
    TIP_PER("Tipo de persona", "TipPer"),
    EST_CLI("Activo", "EstCli"),
    PAG_WEB("Sitio web", "PagWeb");
    private final String name;
    private final String errorName;

    FromToFieldsAccountEnum(String name, String errorName) {
        this.name = name;
        this.errorName = errorName;
    }
}