package br.com.senior.novasoft.http.camel.services;

import static br.com.senior.novasoft.http.camel.entities.LoginInput.LOGIN_INPUT_FORMAT;
import static org.apache.camel.ExchangePattern.InOut;
import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;
import static org.ehcache.config.builders.ExpiryPolicyBuilder.timeToLiveExpiration;
import static org.ehcache.config.units.MemoryUnit.B;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import br.com.senior.novasoft.http.camel.entities.LoginInput;
import br.com.senior.novasoft.http.camel.entities.LoginOutput;
import br.com.senior.novasoft.http.camel.Utils.Enums.ServiceEnum;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationAPI {

    private static final String PLATFORM = "platform";
    private static final String AUTHENTICATION = "impl";

    private static final String AUTHENTICATE = "authenticate";
    private static final String HEADERS_LOG = "${in.headers}";

    private static final String DIRECT_TOKEN_FOUND = "direct:impl-token-found";
    private static final String DIRECT_TOKEN_NOT_FOUND = "direct:impl-token-not-found";
    private static final String DIRECT_LOGIN = "direct:impl-login";
    private static final String DIRECT_LOGIN_WITH_KEY = "direct:impl-login-with-key";
    private static final String DIRECT_REFRESH_TOKEN = "direct:impl-refresh-token";
    private static final String TOKEN_CACHE_KEY = "token-cache-key";
    private static final String TOKEN = "token";

    private static final String TOKEN_CACHE_NAME = "tokenCache";
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
        .when(builder.method(this, "tokenFound")) //

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
        exchange.getMessage().setHeader("Authorization", "Bearer " + loginOutput.token);
    }

    private void tokenFound() {
        builder //
            .from(DIRECT_TOKEN_FOUND) //
            .routeId("token-found") //
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
            .routeId("token-not-found") //
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

        login.method("post").service(ServiceEnum.CUENTA).primitive("login");

        builder //
            .from(DIRECT_LOGIN) //
            .routeId("login") //
            .marshal(LOGIN_INPUT_FORMAT) //
            .to("log:login") //
            .log(HEADERS_LOG) //
            .setExchangePattern(InOut) //
            .process(login::route) //
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
            key = "user:" + loginInput.userLogin + '$' + loginInput.password;
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
        return now() >= loginOutput.expireTime;
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
        if (output.token == null) {
            throw new AuthenticationException(output.status + ": " + output.title);
        }
        ObjectMapper mapper = new ObjectMapper();
        OffsetDateTime odt = OffsetDateTime.parse(output.expiration);
        Date date = new Date(odt.getYear(), odt.getMonthValue(), odt.getDayOfMonth());
        output.expireTime = now() + ((date.getTime() - TOKEN_EXPIRATION_MARGIN) * 1000);
        TOKEN_CACHE.put(exchange.getProperty(TOKEN_CACHE_KEY).toString(), output);
        exchange.setProperty(TOKEN, output);
        exchange.getMessage().setBody(output);
        addAuthorization(exchange);
    }

    private long now() {
        return new Date().getTime();
    }
}
