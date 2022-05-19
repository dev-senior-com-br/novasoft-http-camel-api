package br.com.senior.novasoft.http.camel.utils.enums;

/**
 * Enums com todas
 * as primitivas
 * da Novasoft
 *
 * @author lucas.nunes
 */
public enum PrimitiveEnum {

    LOGIN("login"),//
    ACTIVIDADES("Actividades"),//
    BODEGAS("Bodegas"),//
    CENTROS_DE_COSTO("CentrosDeCosto"),//
    CIUDADES("Ciudades"),//
    CLASIFICADORES_1("Clasificadores1"),//
    CLASIFICADORES_1_CONVENIOS("Clasificadores1Convenios"),//
    CLASIFICADORES_2("Clasificadores2"),//
    CLASIFICADORES_3("Clasificadores3"),//
    CLASIFICADORES_4_CONVENIOS("Clasificadores4Convenios"),//
    CLASIFICADORES_5_CONVENIOS("Clasificadores5Convenios"),//
    CLASIFICADORES_6_CONVENIOS("Clasificadores6Convenios"),//
    CLASIFICADORES_7_CONVENIOS("Clasificadores7Convenios"),//
    CLASIFICADORES_8_CONVENIOS("Clasificadores8Convenios"),//
    CLIENTES("Senior/Accounts"),//
    SUCERSALES_CONVENIOS("SucursalesConvenios"),//
    DEPARTAMENTOS("Departamentos"),//
    DOCUMENTOS("Documentos"),//
    SUCERSALES("Sucursales"),//
    ITEMS("Items"),//
    LISTAS("Listas"),//
    MONEDAS("Monedas"),//
    PAISES("Paises"),//
    RETENCIONES("Retenciones"),//
    TERCEROS("Terceros"),//
    TIPOS_IDENTIFICACIONES("TiposIdentificaciones"),//
    VENDEDORES("Vendedores"),//
    ZONAS("Zonas");//

    private String path;

    PrimitiveEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}