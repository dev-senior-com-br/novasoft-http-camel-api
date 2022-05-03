package br.com.senior.novasoft.http.camel.entities.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.component.jackson.JacksonDataFormat;

/**
 * Output da API de Login
 * da Novasoft
 *
 * @author leonardo.cardoso
 * @author lucas.nunes
 */
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
     * Tempo de expiração
     * em milisegundos
     */
    @JsonProperty("expireTime")
    public Long expireTime;
}