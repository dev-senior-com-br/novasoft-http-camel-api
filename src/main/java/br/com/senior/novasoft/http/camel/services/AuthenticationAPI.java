package br.com.senior.novasoft.http.camel.services;

import br.com.senior.novasoft.http.camel.entities.login.LoginInput;
import br.com.senior.novasoft.http.camel.entities.login.LoginOutput;
import br.com.senior.novasoft.http.camel.exceptions.AuthenticationException;
import br.com.senior.novasoft.http.camel.utils.enums.PrimitiveEnums;
import br.com.senior.novasoft.http.camel.utils.enums.ServiceEnum;
import lombok.*;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import static br.com.senior.novasoft.http.camel.entities.login.LoginInput.LOGIN_INPUT_FORMAT;
import static br.com.senior.novasoft.http.camel.utils.constants.CrmConstants.*;
import static org.apache.camel.ExchangePattern.InOut;
import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;

@RequiredArgsConstructor
public class AuthenticationAPI {

    @NonNull
    private final RouteBuilder routeBuilder;
    private final UUID id = UUID.randomUUID();
    private final String route = DIRECT_NOVASOFT_IMPL.concat(id.toString());
    private final String to = DIRECT_NOVASOFT_IMPL_RESPONSE.concat(id.toString());

    @Getter
    @Setter
    private String url;

    public String getRoute() {
        return route;
    }

    public String getResponseRoute() {
        return to;
    }

    public void prepare() {
        prepare(exchange -> {});
    }

    public void prepare(Processor processor) {
        tokenFound();
        tokenNotFound();
        login();

        routeBuilder //
            .from(route) //
            .routeId(AUTHENTICATE) //
            .to("log:authenticate") //
            .log(HEADERS_LOG) //

            .process(this::searchToken) //

            .choice() // Token found
            .when(//
                routeBuilder.method(//
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

            .process(processor) //
            .to(to) //
        ;
    }

    public static void addAuthorization(Exchange exchange) {
        LoginOutput loginOutput = (LoginOutput) exchange.getProperty(TOKEN);
        exchange//
            .getMessage()//
            .setHeader(//
                "Authorization",//
                "Bearer " + loginOutput.getToken()//
            );
    }

    private void tokenFound() {
        routeBuilder //
            .from(DIRECT_TOKEN_FOUND) //
            .routeId("token-found-novasoft") //
            .to("log:tokenFound") //
            .log(HEADERS_LOG) //

            .choice() // Expired token
            .when(routeBuilder.method(this, "isExpiredToken")) //

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
        routeBuilder //
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
        String urlNovasoft = url != null ? url : null;

        NovasoftHTTPRoute novasoftHTTPRoute = NovasoftHTTPRoute
            .builder()
            .url(urlNovasoft)
            .method("post")
            .serviceEnum(ServiceEnum.CUENTA)
            .primitiveEnums(PrimitiveEnums.LOGIN)
            .build();

        routeBuilder//
            .from(DIRECT_LOGIN)//
            .routeId("login-novasoft")//
            .marshal(LOGIN_INPUT_FORMAT)//
            .to("log:login")//
            .log(HEADERS_LOG)//
            .setExchangePattern(InOut)//
            .process(novasoftHTTPRoute::request)//
            .to("log:logged")//
            .log(HEADERS_LOG)//
        ;
    }

    private void searchToken(Exchange exchange) {
        Object body = exchange.getMessage().getBody();
        if (!(body instanceof LoginInput))
            throw new AuthenticationException("Unknown login payload: " + body.getClass().getName());

        LoginInput loginInput = (LoginInput) body;
        String key =
            "user:"
            .concat(loginInput.getUserLogin())
            .concat("$")
            .concat(loginInput.getPassword());
        exchange.setProperty(TOKEN_CACHE_KEY, key);
        LoginOutput loginOutput = TOKEN_CACHE.get(key);

        if (loginOutput != null)
            exchange.getMessage().setBody(loginOutput);
    }

    private void unmarshallToken(Exchange exchange) {
        validateExchangeLogin(exchange);
        LoginOutput output = (LoginOutput) exchange.getMessage().getBody();
        OffsetDateTime odt = OffsetDateTime.parse(output.getExpiration());
        Date date = new Date(odt.getYear(), odt.getMonthValue(), odt.getDayOfMonth());
        Long expireTime = now() + ((date.getTime() - TOKEN_EXPIRATION_MARGIN) * 1000);
        output.setExpireTime(expireTime);
        TOKEN_CACHE.put(exchange.getProperty(TOKEN_CACHE_KEY).toString(), output);
        exchange.setProperty(TOKEN, output);
        exchange.getMessage().setBody(output);
        addAuthorization(exchange);
    }

    private Long now() {
        return new Date().getTime();
    }

    private void validateExchangeLogin(Exchange exchange) {
        LoginOutput output = (LoginOutput) exchange.getMessage().getBody();
        if (exchange.getException() != null) {
            throw new AuthenticationException(exchange.getException());
        } else if(output.getToken() == null) {
            throw new AuthenticationException(//
                output//
                    .getStatus()//
                    .toString()//
                    .concat(": ")//
                    .concat(output.getTitle())//
            );
        }
    }
}
