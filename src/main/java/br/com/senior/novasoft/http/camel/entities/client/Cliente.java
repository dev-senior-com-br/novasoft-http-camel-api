package br.com.senior.novasoft.http.camel.entities.client;

import br.com.senior.novasoft.http.camel.entities.RequestError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.component.jackson.JacksonDataFormat;

/**
 * Input da API de
 * clientes da Novasoft.
 * <br>
 * <br>
 * Idenpendente de campos obrigatórios
 * ou não todos devem ser enviados. Para os
 * campos opcionais que não queremos enviar
 * vamos utilizar vazio("") para {@link String}
 * e Datas e 0 para {@link Long}.
 *
 * @author lucas.nunes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection(serialization = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cliente extends RequestError {

    public static final JacksonDataFormat JACKSON_DATA_FORMAT = new JacksonDataFormat(Cliente.class);

    /**
     * Código do Consumidor
     */
    @JsonProperty("codCli")
    private String codCli;

    /**
     * Nome do Cliente
     */
    @JsonProperty("nomCli")
    private String nomCli;

    /**
     * Noite do Cliente
     */
    @JsonProperty("nitCli")
    private String nitCli;

    /**
     * Código da Cidade
     */
    @JsonProperty("codCiu")
    private String codCiu;


    /**
     * Código do Departamento
     */
    @JsonProperty("codDep")
    private String codDep;

    /**
     * Código do País
     */
    @JsonProperty("codPai")
    private String codPai;

    /**
     * Endereço
     */
    @JsonProperty("di1Cli")
    private String di1Cli;

    /**
     * Bairro
     */
    @JsonProperty("di2Cli")
    private String di2Cli;

    /**
     * Telefone 1 do Cliente
     */
    @JsonProperty("te1Cli")
    private String te1Cli;

    /**
     * Tipo cliente,
     * deve estar entre 1 e 5
     */
    @JsonProperty("tipCli")
    private Long tipCli = 0L;

    /**
     * Data de admissão
     */
    @JsonProperty("fecIng")
    private String fecIng;

    /**
     * E-mail
     */
    @JsonProperty("eMail")
    private String eMail;

    /**
     * Não especificado
     * na documentação
     */
    @JsonProperty("tipIde")
    private String tipIde;

    /**
     * Apelido 1
     */
    @JsonProperty("ap1Cli")
    private String ap1Cli = "";

    /**
     * Nome 1 do Cliente
     */
    @JsonProperty("nom1Cli")
    private String nom1Cli = "";

    /**
     * Tipo de pessoa,
     * deve estar entre 1 e 2
     */
    @JsonProperty("tipPer")
    private Long tipPer = 0L;

    /**
     * Estado do cliente
     * 1 = Ativo
     * 2 = Inativo
     */
    @JsonProperty("estCli")
    private String estCli = "2";

    /**
     * Código externo do Consumidor
     */
    @JsonProperty("codCliExtr")
    private String codCliExtr;

    /**
     * Site
     */
    @JsonProperty("pagWeb")
    private String pagWeb;

    /**
     * Código verificador do NIT
     */
    @JsonProperty("digVer")
    private String digVer;
}