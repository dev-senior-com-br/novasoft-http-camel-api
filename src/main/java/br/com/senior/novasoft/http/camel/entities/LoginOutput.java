package br.com.senior.novasoft.http.camel.entities;

import org.apache.camel.component.jackson.JacksonDataFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(serialization = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class LoginOutput {

    public static final JacksonDataFormat LOGIN_OUTPUT_FORMAT = new JacksonDataFormat(LoginOutput.class);

    // Success response
    @JsonProperty("token")
    public String token;

    @JsonProperty("expiration")
    public String expiration;

    @JsonProperty("expireTime")
    public Long expireTime;

    // Error response
    @JsonProperty("type")
    public String type;

    @JsonProperty("title")
    public String title;

    @JsonProperty("status")
    public String status;

    @JsonProperty("traceId")
    public String traceId;
}