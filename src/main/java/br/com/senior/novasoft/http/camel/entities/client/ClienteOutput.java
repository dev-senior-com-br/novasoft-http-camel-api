package br.com.senior.novasoft.http.camel.entities.client;

import br.com.senior.novasoft.http.camel.entities.RequestError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.component.jackson.JacksonDataFormat;

import java.util.List;

/**
 * Output da API de
 * clientes da Novasoft.
 *
 * @author lucas.nunes
 */
@RegisterForReflection(serialization = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteOutput extends RequestError {

    public static final JacksonDataFormat CLIENTE_OUTPUT_FORMAT = new JacksonDataFormat(ClienteOutput.class);

    @JsonProperty("")
    private List<ClienteErrorOutput> clienteOutputErrors;
}
