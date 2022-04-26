package br.com.senior.novasoft.http.camel.services;

import static br.com.senior.novasoft.http.camel.entities.login.LoginInput.LOGIN_INPUT_FORMAT;
import static org.apache.camel.ExchangePattern.InOut;
import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;
import static org.ehcache.config.builders.ExpiryPolicyBuilder.timeToLiveExpiration;
import static org.ehcache.config.units.MemoryUnit.B;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import br.com.senior.novasoft.http.camel.entities.login.LoginInput;
import br.com.senior.novasoft.http.camel.entities.login.LoginOutput;
import br.com.senior.novasoft.http.camel.utils.enums.PrimitiveEnums;
import br.com.senior.novasoft.http.camel.utils.enums.ServiceEnum;
import br.com.senior.novasoft.http.camel.exceptions.AuthenticationException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationAPI {

    private static final String AUTHENTICATE = "authenticate-novasoft";
    private static final String HEADERS_LOG = "${in.headers}";

    private static final String DIRECT_TOKEN_FOUND = "direct:impl-token-found-novasoft";
    private static final String DIRECT_TOKEN_NOT_FOUND = "direct:impl-token-not-found-novasoft";
    private static final String DIRECT_LOGIN = "direct:impl-login-novasoft";
    private static final String TOKEN_CACHE_KEY = "token-cache-key-novasoft";
    private static final String TOKEN = "token-novasoft";

    private static final String TOKEN_CACHE_NAME = "tokenCacheNovasoft";
    // Token cache size in bytes.
    private static final long TOKEN_CACHE_SIZE = 64000000;
    // Refresh token TTL in seconds (See environment variable KONG_REFRESH_TOKEN_TTL at https://git.senior.com.br/arquitetura/kong-rest-client/-/wikis/home).
    private static final int REFRESH_TOKEN_TTL = 15552000;
    // Token expiration time margin in seconds.
    private static final int TOKEN_EXPIRATION_MARGIN = 60;

    private static final CacheManager CACHE_MANAGER = newCacheManagerBuilder().build(true);

    private static final Cache<String, LoginOutput> TOKEN_CACHE = CACHE_MANAGER.createCache(TOKEN_CACHE_NAME, //
            newCacheConfigurationBuilder(String.class, LoginOutput.class, //
                    ResourcePoolsBuilder.newResourcePoolsBuilder().heap(TOKEN_CACHE_SIZE, B).build()) //
            .withExpiry(timeToLiveExpiration(Duration.ofSeconds(REFRESH_TOKEN_TTL))) //
            .build());

    private final RouteBuilder builder;
    private final UUID id = UUID.randomUUID();
    private final String route = "direct:novasoft-impl-" + id.toString();
    private final String to = "direct:novasoft-impl-response-" + id.toString();

    public String url;

    public AuthenticationAPI(RouteBuilder builder) {
        this.builder = builder;
    }

    public String route() {
        return route;
    }

    public String responseRoute() {
        return to;
    }

    public void prepare() {
        prepare(exchange -> {
        });
    }

    public void prepare(Processor enrichWithToken) {
        tokenFound();

        tokenNotFound();

        login();

        builder //
        .from(route) //
        .routeId(AUTHENTICATE) //
        .to("log:authenticate") //
        .log(HEADERS_LOG) //

        .process(this::searchToken) //

        .choice() // Token found
        .when(//
                builder.method(//
                        this,//
                        "tokenFound"//
                )//
        )//

        .setExchangePattern(InOut) //
        .to(DIRECT_TOKEN_FOUND) //

        .otherwise() // Token not found

        .setExchangePattern(InOut) //
        .to(DIRECT_TOKEN_NOT_FOUND) //

        .end() // Token found

        .process(enrichWithToken) //
        .to(to) //
        ;
    }

    public static void addAuthorization(Exchange exchange) {
        LoginOutput loginOutput = (LoginOutput) exchange.getProperty(TOKEN);
        exchange.getMessage().setHeader("Authorization", "Bearer " + loginOutput.getToken());
    }

    private void tokenFound() {
        builder //
            .from(DIRECT_TOKEN_FOUND) //
            .routeId("token-found-novasoft") //
            .to("log:tokenFound") //
            .log(HEADERS_LOG) //

            .choice() // Expired token
            .when(builder.method(this, "isExpiredToken")) //

            .to("log:tokenExpired") //
            .log(HEADERS_LOG) //

            .setExchangePattern(InOut) //
            .to(DIRECT_TOKEN_NOT_FOUND) //

            .to("log:refreshedToken") //
            .log(HEADERS_LOG) //
            .unmarshal(LoginOutput.LOGIN_OUTPUT_FORMAT) //
            .process(this::unmarshallToken) //

            .end() // Expired token
        ;
    }

    private void tokenNotFound() {
        builder //
            .from(DIRECT_TOKEN_NOT_FOUND) //
            .routeId("token-not-found-novasoft") //
            .to("log:tokenNotFound") //
            .log(HEADERS_LOG) //
            .setExchangePattern(InOut) //
            .to(DIRECT_LOGIN) //
            .to("log:authenticated") //
            .log(HEADERS_LOG) //
            .unmarshal(LoginOutput.LOGIN_OUTPUT_FORMAT) //
            .process(this::unmarshallToken) //
        ;
    }

    private void login() {
        NovasoftHTTPRouteBuilder login = new NovasoftHTTPRouteBuilder(builder);

        if (url != null) {
            login.url(url);
        }

        login.method("post").service(ServiceEnum.CUENTA).primitive(PrimitiveEnums.LOGIN);

        builder //
            .from(DIRECT_LOGIN) //
            .routeId("login-novasoft") //
            .marshal(LOGIN_INPUT_FORMAT) //
            .to("log:login") //
            .log(HEADERS_LOG) //
            .setExchangePattern(InOut) //
            .process(login::request) //
            .to("log:logged") //
            .log(HEADERS_LOG) //
        ;
    }

    private void searchToken(Exchange exchange) {
        String key = null;
        LoginOutput loginOutput = null;
        Object body = exchange.getMessage().getBody();
        if (body instanceof LoginInput) {
            LoginInput loginInput = (LoginInput) body;
            key = "user:" + loginInput.getUserLogin() + '$' + loginInput.getPassword();
        } else {
            throw new AuthenticationException("Unknown login payload: " + body.getClass().getName());
        }
        exchange.setProperty(TOKEN_CACHE_KEY, key);
        loginOutput = TOKEN_CACHE.get(key);
        if (loginOutput != null) {
            exchange.getMessage().setBody(loginOutput);
        }
    }

    public boolean tokenFound(Object body) {
        return body instanceof LoginOutput;
    }

    public boolean isExpiredToken(Object body) {
        LoginOutput loginOutput = (LoginOutput) body;
        return now() >= loginOutput.getExpireTime();
    }

    public boolean isUserLogin(Object body) {
        return body instanceof LoginInput;
    }

    private void unmarshallToken(Exchange exchange) {
        Exception exception = exchange.getException();
        if (exception != null) {
            throw new AuthenticationException(exception);
        }
        LoginOutput output = (LoginOutput) exchange.getMessage().getBody();
        if (output.getToken() == null) {
            throw new AuthenticationException(output.getStatus() + ": " + output.getTitle());
        }
        ObjectMapper mapper = new ObjectMapper();
        OffsetDateTime odt = OffsetDateTime.parse(output.getExpiration());
        Date date = new Date(odt.getYear(), odt.getMonthValue(), odt.getDayOfMonth());
        output.setExpireTime(now() + ((date.getTime() - TOKEN_EXPIRATION_MARGIN) * 1000));
        TOKEN_CACHE.put(exchange.getProperty(TOKEN_CACHE_KEY).toString(), output);
        exchange.setProperty(TOKEN, output);
        exchange.getMessage().setBody(output);
        addAuthorization(exchange);
    }

    private long now() {
        return new Date().getTime();
    }
}
