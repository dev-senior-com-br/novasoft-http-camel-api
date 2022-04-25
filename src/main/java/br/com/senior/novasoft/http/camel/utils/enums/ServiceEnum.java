package br.com.senior.novasoft.http.camel.utils.enums;

/**
 * Enum com os serviços
 * usados nas URL's
 * da API's da Novasoft
 *
 * @author leonardo.cardoso
 * @author lucas.nunes
 */
public enum ServiceEnum {

    GEN("GEN"), //
    CUENTA("cuenta"), //
    CXC("CXC"); //

    private String path;

    ServiceEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}