package br.com.senior.novasoft.http.camel.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.component.jackson.JacksonDataFormat;

import java.util.List;

@RegisterForReflection(serialization = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestError {

    public static final JacksonDataFormat REQUEST_ERROR_FORMAT = new JacksonDataFormat(RequestError.class);

    @JsonProperty("type")
    private String type;

    @JsonProperty("title")
    private String title;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("traceId")
    private String traceId;

    @JsonProperty("errors")
    private List<Object> errors;
}
