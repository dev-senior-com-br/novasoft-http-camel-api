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
public class ClienteInput extends RequestError {

    public static final JacksonDataFormat JACKSON_DATA_FORMAT = new JacksonDataFormat(ClienteInput.class);

    //Campos obrigatórios
    /**
     * Atividade econômica
     */
    @JsonProperty("actCli")
    private String actCli = "0";

    /**
     * Agente de retenção
     * deve estar entre 1 e 2
     */
    @JsonProperty("ageRet")
    private Long ageRet = 0L;

    /**
     * Apelido 1
     */
    @JsonProperty("ap1Cli")
    private String ap1Cli = "-";

    /**
     * Código da Cidade
     */
    @JsonProperty("codCiu")
    private String codCiu = "";

    /**
     * Código do Consumidor
     */
    @JsonProperty("codCli")
    private String codCli = "";

    /**
     * Código do Departamento
     */
    @JsonProperty("codDep")
    private String codDep = "";

    /**
     * Código do País
     */
    @JsonProperty("codPai")
    private String codPai = "";

    /**
     * Código do Responsável
     */
    @JsonProperty("codRes")
    private String codRes = "0";

    /**
     * Código da Zona
     */
    @JsonProperty("codZon")
    private String codZon = "0";

    /**
     * Código do tributo
     */
    @JsonProperty("codTrib")
    private String codTrib = "0";

    /**
     * Conta contábil IFRS
     */
    @JsonProperty("codCtaNiif")
    private String codCtaNiif = "0";

    /**
     * Direção
     */
    @JsonProperty("di1Cli")
    private String di1Cli = "";

    /**
     * Dígito de verificação
     */
    @JsonProperty("digVer")
    private String digVer = "0";

    /**
     * E-mail
     */
    @JsonProperty("eMail")
    private String eMail = "";

    /**
     * Forma de pagamento
     */
    @JsonProperty("forPag")
    private String forPag = "0";

    /**
     * Fatura da forma de entrega,
     * deve estar entre 1 e 3
     */
    @JsonProperty("formEnt")
    private Long formEnt = 0L;

    /**
     * Contato Cliente
     */
    @JsonProperty("jefCom")
    private String jefCom = "0";

    /**
     * Domicilío,
     * deve estar entre 1 e 3
     */
    @JsonProperty("nacCli")
    private Long nacCli = 0L;

    /**
     * Noite do Cliente
     */
    @JsonProperty("nitCli")
    private String nitCli = "";

    /**
     * Nome do Cliente
     */
    @JsonProperty("nomCli")
    private String nomCli = "";

    /**
     * Nome 1 do Cliente
     */
    @JsonProperty("nom1Cli")
    private String nom1Cli = "";

    /**
     * Telefone 1 do Cliente
     */
    @JsonProperty("te1Cli")
    private String te1Cli = "0";

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
    private Long tipCli = 0L;

    /**
     * Não especificado
     * na documentação
     */
    @JsonProperty("tipIde")
    private String tipIde = "";

    /**
     * Tipo de moeda
     */
    @JsonProperty("tipMon")
    private String tipMon = "00";

    /**
     * Tipo de pessoa,
     * deve estar entre 1 e 2
     */
    @JsonProperty("tipPer")
    private Long tipPer = 0L;


    //Campos opcionais
    /**
     * Data de admissão
     */
    @JsonProperty("fecIng")
    private String fecIng = "";

    /**
     * Conta contavél
     */
    @JsonProperty("codCta")
    private String codCta = "0";

    /**
     * % de retenção de
     * IVA(Imposto de Valor Agregado)
     */
    @JsonProperty("retIva")
    private Long retIva = 0L;

    /**
     * Código do Vendedor
     */
    @JsonProperty("codVen")
    private String codVen = "0";

    /**
     * Capacidade de Endividamento
     */
    @JsonProperty("capEnd")
    private Long capEnd = 0L;

    /**
     * Vaga
     */
    @JsonProperty("cupCli")
    private Long cupCli = 0L;

    /**
     * Apelido 2
     */
    @JsonProperty("ap2Cli")
    private String ap2Cli = "";

    /**
     * Nome 2
     */
    @JsonProperty("nom2Cli")
    private String nom2Cli = "";

    /**
     * Cota de observação
     * atribuído
     */
    @JsonProperty("obsCupo")
    private String obsCupo = "";

    /**
     * Dias limite para
     * imperfeição
     */
    @JsonProperty("plaPag")
    private Long plaPag = 0L;

    /**
     * Gênero
     */
    @JsonProperty("genero")
    private Long genero = 0L;

    /**
     * Data de
     * Aniversário
     */
    @JsonProperty("fecNac")
    private String fecNac;

    /**
     * Indicador de status
     * em atraso
     */
    @JsonProperty("indMora")
    private Long indMora = 0L;

    /**
     * Dias habitados
     */
    @JsonProperty("diasMora")
    private Long diasMora = 0L;
}