package br.com.senior.novasoft.http.camel.services;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import br.com.senior.novasoft.http.camel.Utils.Enums.ServiceEnum;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpClientConfigurer;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.spi.PropertiesComponent;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NovasoftHTTPRouteBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(NovasoftHTTPRouteBuilder.class);

    private static final String HTTPS = "https";

    protected final RouteBuilder builder;
    protected String url = "";
    protected String allowedInsecureHost = "{{novasoft.allowedinsecurehost}}";
    protected String method;
    protected ServiceEnum service;
    protected String primitive;

    public NovasoftHTTPRouteBuilder(RouteBuilder builder) {
        this.builder = builder;
    }

    public NovasoftHTTPRouteBuilder method(String method) {
        this.method = method;
        return this;
    }

    public NovasoftHTTPRouteBuilder url(String url) {
        this.url = url;
        return this;
    }

    public NovasoftHTTPRouteBuilder allowedInsecureHost(String allowedInsecureHost) {
        this.allowedInsecureHost = allowedInsecureHost;
        return this;
    }

    public NovasoftHTTPRouteBuilder service(ServiceEnum service) {
        this.service = service;
        return this;
    }

    public NovasoftHTTPRouteBuilder primitive(String primitive) {
        this.primitive = primitive;
        return this;
    }

    public void route(Exchange exchange) {
        PropertiesComponent properties = exchange.getContext().getPropertiesComponent();
        String route = url;

        if (route == null) {
            throw new NovasoftHTTPException("URL property not configured");
        }
        if (route.endsWith("/")) {
            route = route.substring(0, route.length() - 1);
        }

        route += "/";
        route += service.path;
        route += "/";
        route += primitive;

        Message message = exchange.getMessage();
        message.setHeader("Content-Type", "application/json");
        message.setHeader(Exchange.HTTP_METHOD, method);

        call(route, resolve(properties, allowedInsecureHost), exchange);
    }

    private void call(String route, String insecureHost, Exchange exchange) {
        HttpComponent httpComponent = exchange.getContext().getComponent("http", HttpComponent.class);

        if (route.startsWith(HTTPS)) {
            httpComponent = exchange.getContext().getComponent(HTTPS, HttpComponent.class);

            if (insecureHost != null) {
                configureInsecureCall(route, insecureHost, httpComponent);
            }
        }
        exchange.getIn().setHeader(Exchange.HTTP_URI, route);
        try (ProducerTemplate producerTemplate = exchange.getContext().createProducerTemplate()) {
            LOGGER.info("Routing to {}", route);
            ForwardProcessor forwardProcessor = new ForwardProcessor(exchange);
            Exchange request = producerTemplate.request(httpComponent.createEndpoint(route), forwardProcessor);
            LOGGER.info("Routed to {}", route);
            Exception e = request.getException();
            if (e != null) {
                throw new NovasoftHTTPException(e);
            }
            forwardProcessor.reverse(request);
        } catch (NovasoftHTTPException e) {
            throw e;
        } catch (Exception e) {
            throw new NovasoftHTTPException(e);
        }
    }

    private void configureInsecureCall(String route, String insecureHost, HttpComponent httpComponent) {
        LOGGER.warn("Routing to insecure http call {}", route);
        SSLContext sslctxt = getSSLContext();
        HttpClientConfigurer httpClientConfig = getEndpointClientConfigurer(sslctxt);
        httpComponent.setHttpClientConfigurer(httpClientConfig);
        HostnameVerifier hnv = new AllowHost(insecureHost);
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslctxt, hnv);
        Registry<ConnectionSocketFactory> lookup = RegistryBuilder.<ConnectionSocketFactory>create().register(HTTPS, sslSocketFactory).build();
        HttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(lookup);
        httpComponent.setClientConnectionManager(connManager);
    }

    private SSLContext getSSLContext() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2", "SunJSSE");

            TrustManager[] trustAllCerts = new TrustManager[] { new TrustALLManager() };
            sslContext.init(null, trustAllCerts, null);
            return sslContext;
        } catch (NoSuchAlgorithmException | NoSuchProviderException | KeyManagementException e) {
            throw new NovasoftHTTPException(e);
        }
    }

    private class TrustALLManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

    }

    private static class AllowHost implements HostnameVerifier {

        private final String allowedInsecureHost;

        public AllowHost(String allowedInsecureHost) {
            this.allowedInsecureHost = allowedInsecureHost;
        }

        @Override
        public boolean verify(String hostname, SSLSession session) {
            boolean allowed = allowedInsecureHost.equals(hostname);
            if (allowed) {
                LOGGER.debug("Allowing {}", hostname);
            } else {
                LOGGER.error("Blocking {}", hostname);
            }
            return allowed;
        }

    }

    private HttpClientConfigurer getEndpointClientConfigurer(final SSLContext sslContext) {
        return clientBuilder -> clientBuilder.setSSLContext(sslContext);
    }

    private String resolve(PropertiesComponent properties, String value) {
        if (value != null && value.startsWith("{{") && value.endsWith("}}")) {
            return properties.resolveProperty(value.substring(2, value.length() - 2)).orElse(null);
        }
        return value;
    }

}
