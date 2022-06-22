package br.com.senior.novasoft.http.camel.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.component.jackson.JacksonDataFormat;

/**
 * <h1>DTO de erro das requisições da Novasoft.</h1>
 *
 * <p>DTO referente a resposta de erro vindo da
 * Novasoft. Casso aconteça algum erro na requisição
 * esse corpo é retornado.</p>
 *
 * @author lucas.nunes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection(serialization = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestError {

    public static final JacksonDataFormat REQUEST_ERROR_FORMAT = new JacksonDataFormat(RequestError.class);

    /**
     * Tipo de erro.
     */
    @JsonProperty("type")
    private String type;

    /**
     * Titulo do erro
     */
    @JsonProperty("title")
    private String title;

    /**
     * Status da requisição
     */
    @JsonProperty("status")
    private Integer status;

    /**
     * Id do erro
     */
    @JsonProperty("traceId")
    private String traceId;

    /**
     * Erros que ocorreram.
     */
    @JsonProperty("errors")
    private Object errors;
}