package br.com.senior.novasoft.http.camel.entities.client;

import br.com.senior.novasoft.http.camel.entities.RequestError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.component.jackson.JacksonDataFormat;

import java.time.LocalDate;

/**
 * Input da API de
 * clientes da Novasoft.
 * <br>
 * <br>
 * Idenpendente de campos obrigatórios
 * ou não todos devem ser enviados. Para os
 * campos opcionais que não queremos enviar
 * vamos utilizar vazio("") para {@link String}
 * e {@link LocalDate} e 0 para {@link Long}.
 *
 * @author lucas.nunes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection(serialization = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteInput extends RequestError {

    public static final JacksonDataFormat CLIENTE_INPUT_FORMAT = new JacksonDataFormat(ClienteInput.class);

    //Campos obrigatórios
    /**
     * Atividade econômica
     */
    @JsonProperty("actCli")
    private String actCli;

    /**
     * Agente de retenção
     * deve estar entre 1 e 2
     */
    @JsonProperty("ageRet")
    private Long ageRet;

    /**
     * Apelido 1
     */
    @JsonProperty("ap1Cli")
    private String ap1Cli;

    /**
     * Código da Cidade
     */
    @JsonProperty("codCiu")
    private String codCiu;

    /**
     * Código do Consumidor
     */
    @JsonProperty("codCli")
    private String codCli;

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
     * Código do Responsável
     */
    @JsonProperty("codRes")
    private String codRes;

    /**
     * Código da Zona
     */
    @JsonProperty("codZon")
    private String codZon;

    /**
     * Código do tributo
     */
    @JsonProperty("codTrib")
    private String codTrib;

    /**
     * Conta contábil IFRS
     */
    @JsonProperty("codCtaNiif")
    private String codCtaNiif;

    /**
     * Direção
     */
    @JsonProperty("di1Cli")
    private String di1Cli;

    /**
     * Dígito de verificação
     */
    @JsonProperty("digVer")
    private String digVer;

    /**
     * E-mail
     */
    @JsonProperty("eMail")
    private String eMail;

    /**
     * Forma de pagamento
     */
    @JsonProperty("forPag")
    private String forPag;

    /**
     * Fatura da forma de entrega,
     * deve estar entre 1 e 3
     */
    @JsonProperty("formEnt")
    private String formEnt;

    /**
     * Contato Cliente
     */
    @JsonProperty("jefCom")
    private String jefCom;

    /**
     * Domicilío,
     * deve estar entre 1 e 3
     */
    @JsonProperty("nacCli")
    private String nacCli;

    /**
     * Noite do Cliente
     */
    @JsonProperty("nitCli")
    private String nitCli;

    /**
     * Nome do Cliente
     */
    @JsonProperty("nomCli")
    private String nomCli;

    /**
     * Nome 1 do Cliente
     */
    @JsonProperty("nom1Cli")
    private String nom1Cli;

    /**
     * Telefone 1 do Cliente
     */
    @JsonProperty("te1Cli")
    private String te1Cli;

    /**
     * Telefone 2 do Cliente
     */
    @JsonProperty("te2Cli")
    private String te2Cli;

    /**
     * Tipo cliente,
     * deve estar entre 1 e 5
     */
    @JsonProperty("tipCli")
    private Long tipCli;

    /**
     * Não especificado
     * na documentação
     */
    @JsonProperty("tipIde")
    private String tipIde;

    /**
     * Tipo de moeda
     */
    @JsonProperty("tipMon")
    private String tipMon;

    /**
     * Tipo de pessoa,
     * deve estar entre 1 e 2
     */
    @JsonProperty("tipPer")
    private Long tipPer;


    //Campos opcionais
    /**
     * Data de admissão
     */
    @JsonProperty("fecIng")
    private LocalDate fecIng;

    /**
     * Conta contavél
     */
    @JsonProperty("codCta")
    private String codCta;

    /**
     * % de retenção de
     * IVA(Imposto de Valor Agregado)
     */
    @JsonProperty("retIva")
    private Long retIva;

    /**
     * Código do Vendedor
     */
    @JsonProperty("codVen")
    private String codVen;

    /**
     * Capacidade de Endividamento
     */
    @JsonProperty("capEnd")
    private Long capEnd;

    /**
     * Vaga
     */
    @JsonProperty("cupCli")
    private Long cupCli;

    /**
     * Apelido 2
     */
    @JsonProperty("ap2Cli")
    private String ap2Cli;

    /**
     * Nome 2
     */
    @JsonProperty("nom2Cli")
    private String nom2Cli;

    /**
     * Cota de observação
     * atribuído
     */
    @JsonProperty("obsCupo")
    private String obsCupo;

    /**
     * Dias limite para
     * imperfeição
     */
    @JsonProperty("plaPag")
    private Long plaPag;

    /**
     * Gênero
     */
    @JsonProperty("genero")
    private Long genero;

    /**
     * Data de
     * Aniversário
     */
    @JsonProperty("fecNac")
    private LocalDate fecNac;

    /**
     * Indicador de status
     * em atraso
     */
    @JsonProperty("indMora")
    private Long indMora;

    /**
     * Dias habitados
     */
    @JsonProperty("diasMora")
    private Long diasMora;
}