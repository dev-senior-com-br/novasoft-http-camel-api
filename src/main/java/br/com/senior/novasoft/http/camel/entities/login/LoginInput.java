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
 * Input da API de Login
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
public class LoginInput {

    public static final JacksonDataFormat LOGIN_INPUT_FORMAT = new JacksonDataFormat(LoginInput.class);

    /**
     * Nome do Usuário
     */
    @JsonProperty("userLogin")
    public String userLogin;

    /**
     * Senha do Usuário
     */
    @JsonProperty("password")
    public String password;

    /**
     * Nome da Conexão
     */
    @JsonProperty("connectionName")
    public String connectionName;
}