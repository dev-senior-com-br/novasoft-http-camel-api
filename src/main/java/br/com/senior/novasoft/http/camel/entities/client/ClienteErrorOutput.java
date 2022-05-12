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
 * Outout de erro
 * da requisição para
 * API de Clientes
 * da Novasoft
 *
 * @author lucas.nunes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection(serialization = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteErrorOutput extends RequestError {

    public static final JacksonDataFormat JACKSON_DATA_FORMAT = new JacksonDataFormat(ClienteErrorOutput.class);

    /**
     * Id do cliente
     */
    @JsonProperty("cliente")
    public String cliente;

    /**
     * Mensagem do que aconteceu
     */
    @JsonProperty("mensaje")
    public String mensaje;

    /**
     * É um erro
     */
    @JsonProperty("indError")
    public Boolean indError;
}
