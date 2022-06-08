package br.com.senior.novasoft.http.camel.services;

import br.com.senior.novasoft.http.camel.exceptions.NovasoftHTTPException;
import br.com.senior.novasoft.http.camel.utils.constants.AuthenticationApiConstants;
import br.com.senior.novasoft.http.camel.utils.enums.MethodEnum;
import br.com.senior.novasoft.http.camel.utils.enums.PrimitiveComplementEnum;
import br.com.senior.novasoft.http.camel.utils.enums.PrimitiveEnum;
import br.com.senior.novasoft.http.camel.utils.enums.ServiceEnum;
import br.com.senior.novasoft.http.camel.utils.https.AllowHost;
import br.com.senior.novasoft.http.camel.utils.https.TrustALLManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.http.HttpClientConfigurer;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spi.PropertiesComponent;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class NovasoftHTTPRouteBuilder {

    private String url = "";
    private String allowedInsecureHost = "{{novasoft.allowedinsecurehost}}";
    private MethodEnum method;
    private ServiceEnum serviceEnum;
    private PrimitiveEnum primitiveEnum;

    private String complementPrimitive = "";

    public void setComplementPrimitive(PrimitiveComplementEnum primitiveComplement, String complement)
    {
        complementPrimitive = primitiveComplement.getPath() + complement;
    }

    public void clearComplementPrimitive()
    {
        complementPrimitive = "";
    }

    public void request(Exchange exchange) {
        String route = url;

        if (route == null)
            throw new NovasoftHTTPException("URL property not configured");

        route = buildRoute(route);

        route = route.concat("/")
            .concat(serviceEnum.getPath())
            .concat("/")
            .concat(primitiveEnum.getPath())
            .concat(complementPrimitive)
            .concat("?throwExceptionOnFailure=false");

        Message message = exchange.getMessage();
        message.setHeader("Content-Type", "application/json");
        message.setHeader(Exchange.HTTP_METHOD, method.getPath());
        exchange.setMessage(message);

        Object bodyIn = exchange.getIn().getBody();
        String payloadSent = exchange.getIn().getBody(String.class);
        exchange.getIn().setBody(bodyIn);
        exchange.setProperty("payloadSent", payloadSent);

        log.info("Request " + message.getHeader(Exchange.HTTP_METHOD) + " to " + route + " with payload sent " + payloadSent);

        call(//
            route,//
            resolve(//
                exchange.getContext().getPropertiesComponent(),//
                allowedInsecureHost//
            ),//
            exchange//
        );

        Object bodyOut = exchange.getMessage().getBody();
        String payloadReceiver = exchange.getMessage().getBody(String.class);
        exchange.getMessage().setBody(bodyOut);
        exchange.setProperty("payloadReceiver", payloadReceiver);

        log.info("Request " + exchange.getIn().getHeader(Exchange.HTTP_METHOD) + " to " + exchange.getIn().getHeader(Exchange.HTTP_URI) + " with payload receiver " + payloadReceiver);
    }

    private void call(String route, String insecureHost, Exchange exchange) {
        HttpComponent httpComponent = exchange.getContext().getComponent("http", HttpComponent.class);
        if (route.startsWith(AuthenticationApiConstants.HTTPS)) {
            httpComponent = exchange.getContext().getComponent(AuthenticationApiConstants.HTTPS, HttpComponent.class);
            if (insecureHost != null)
                configureInsecureCall(route, insecureHost, httpComponent);
        }
        exchange.getIn().setHeader(Exchange.HTTP_URI, route);

        try (ProducerTemplate producerTemplate = exchange.getContext().createProducerTemplate()) {
            ForwardProcessor forwardProcessor = new ForwardProcessor(exchange);
            Exchange request = producerTemplate.request(httpComponent.createEndpoint(route), forwardProcessor);
            Exception exception = request.getException();
            if (exception != null) {
                log.error(exception.getMessage());
            }
            forwardProcessor.reverse(request);
        } catch (Exception exception) {
            log.error("Error catch: {}", exception.getMessage());
        }
    }

    private void configureInsecureCall(String route, String insecureHost, HttpComponent httpComponent) {
        log.warn("Routing to insecure http call {}", route);
        SSLContext sslContext = getSSLContext();
        HttpClientConfigurer httpClientConfig = getEndpointClientConfigurer(sslContext);
        httpComponent.setHttpClientConfigurer(httpClientConfig);
        HostnameVerifier hnv = new AllowHost(insecureHost);
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hnv);
        Registry<ConnectionSocketFactory> lookup = RegistryBuilder.<ConnectionSocketFactory>create().register(AuthenticationApiConstants.HTTPS, sslSocketFactory).build();
        HttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(lookup);
        httpComponent.setClientConnectionManager(connManager);
    }

    private SSLContext getSSLContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2", "SunJSSE");
            TrustManager[] trustAllCerts = new TrustManager[]{new TrustALLManager()};
            sslContext.init(null, trustAllCerts, null);
            return sslContext;
        } catch (Exception exception) {
            throw new NovasoftHTTPException(exception);
        }
    }

    private HttpClientConfigurer getEndpointClientConfigurer(final SSLContext sslContext) {
        return clientBuilder -> clientBuilder.setSSLContext(sslContext);
    }

    private String resolve(PropertiesComponent properties, String value) {
        if (value != null && value.startsWith("{{") && value.endsWith("}}"))
            return properties.resolveProperty(value.substring(2, value.length() - 2)).orElse(null);
        return value;
    }

    private String buildRoute(String route) {
        return route.endsWith("/") ? route.substring(0, route.length() - 1) : route;
    }
}
