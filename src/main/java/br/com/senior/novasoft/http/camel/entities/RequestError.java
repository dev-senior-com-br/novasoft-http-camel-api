package br.com.senior.novasoft.http.camel.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.component.jackson.JacksonDataFormat;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection(serialization = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestError {

    public static final JacksonDataFormat REQUEST_ERROR_FORMAT = new JacksonDataFormat(RequestError.class);

    @JsonProperty("type")
    public String type;

    @JsonProperty("title")
    public String title;

    @JsonProperty("status")
    public Integer status;

    @JsonProperty("traceId")
    public String traceId;

    @JsonProperty("errors")
    public Object errors;
}
