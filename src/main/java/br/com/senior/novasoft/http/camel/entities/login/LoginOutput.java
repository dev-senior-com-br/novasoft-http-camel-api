package br.com.senior.novasoft.http.camel.entities.login;

import lombok.Builder;
import lombok.Data;
import org.apache.camel.component.jackson.JacksonDataFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * Output da API de Login
 * da Novasoft
 *
 * @author leonardo.cardoso
 * @author lucas.nunes
 */
@Data
@Builder
@RegisterForReflection(serialization = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class LoginOutput {

    public static final JacksonDataFormat LOGIN_OUTPUT_FORMAT = new JacksonDataFormat(LoginOutput.class);

    // Success response
    /**
     * Token de autenticação
     */
    @JsonProperty("token")
    public String token;

    /**
     * Data e hora de
     * expiração do Token
     */
    @JsonProperty("expiration")
    public String expiration;

    /**
     * NÃO DOCUMENTADO
     */
    @JsonProperty("expireTime")
    public Long expireTime;

    // Error response
    /**
     * Tipo do Erro
     */
    @JsonProperty("type")
    public String type;

    /**
     * Nome do Erro
     */
    @JsonProperty("title")
    public String title;

    /**
     * Status da Requisição
     */
    @JsonProperty("status")
    public String status;

    /**
     * Id do trace de erro
     */
    @JsonProperty("traceId")
    public String traceId;
}