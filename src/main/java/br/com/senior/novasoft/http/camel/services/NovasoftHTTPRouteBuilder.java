package br.com.senior.novasoft.http.camel.services;

import br.com.senior.novasoft.http.camel.exceptions.NovasoftHTTPException;
import br.com.senior.novasoft.http.camel.utils.constants.AuthenticationApiConstants;
import br.com.senior.novasoft.http.camel.utils.enums.PrimitiveEnums;
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
    private String method;
    private ServiceEnum serviceEnum;
    private PrimitiveEnums primitiveEnums;

    public void request(Exchange exchange) {
        String route = url;

        if (route == null)
            throw new NovasoftHTTPException("URL property not configured");

        route = buildRoute(route);

        route = route.concat("/")
            .concat(serviceEnum.getPath())
            .concat("/")
            .concat(primitiveEnums.getPath());

        Message message = exchange.getMessage();
        message.setHeader("Content-Type", "application/json");
        message.setHeader(Exchange.HTTP_METHOD, method);

        call(//
            route,//
            resolve(//
                exchange.getContext().getPropertiesComponent(),//
                allowedInsecureHost//
            ),//
            exchange//
        );
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
            log.info("Routing to {}", route);
            ForwardProcessor forwardProcessor = new ForwardProcessor(exchange);
            Exchange request = producerTemplate.request(httpComponent.createEndpoint(route), forwardProcessor);
            log.info("Routed to {}", route);
            log.info("Body: {}", request.getMessage().getBody().toString());
            Exception exception = request.getException();
            if (exception != null) {
                throw new NovasoftHTTPException(exception);
            }
            forwardProcessor.reverse(request);
        } catch (Exception exception) {
            throw new NovasoftHTTPException(exception);
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
