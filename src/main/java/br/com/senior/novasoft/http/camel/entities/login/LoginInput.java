package br.com.senior.novasoft.http.camel.entities.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection(serialization = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class LoginInput {

    public static final JacksonDataFormat LOGIN_INPUT_FORMAT = new JacksonDataFormat(LoginInput.class);

    /**
     * Nome do Usuário
     */
    @JsonProperty("userLogin")
    private String userLogin;

    /**
     * Senha do Usuário
     */
    @JsonProperty("password")
    private String password;

    /**
     * Nome da Conexão
     */
    @JsonProperty("connectionName")
    private String connectionName;
}