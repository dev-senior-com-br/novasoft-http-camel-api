package br.com.senior.novasoft.http.camel.entities.login;

import br.com.senior.novasoft.http.camel.entities.RequestError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginOutput extends RequestError {

    public static final JacksonDataFormat LOGIN_OUTPUT_FORMAT = new JacksonDataFormat(LoginOutput.class);

    // Success response
    /**
     * Token de autenticação
     */
    @JsonProperty("token")
    private String token;

    /**
     * Data e hora de
     * expiração do Token
     */
    @JsonProperty("expiration")
    private String expiration;

    /**
     * Tempo de expiração
     * em milisegundos
     */
    @JsonProperty("expireTime")
    private Long expireTime;
}