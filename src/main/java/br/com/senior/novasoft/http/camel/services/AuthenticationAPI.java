package br.com.senior.novasoft.http.camel.services;

import br.com.senior.novasoft.http.camel.entities.login.LoginInput;
import br.com.senior.novasoft.http.camel.entities.login.LoginOutput;
import br.com.senior.novasoft.http.camel.exceptions.AuthenticationException;
import br.com.senior.novasoft.http.camel.utils.enums.MethodEnum;
import br.com.senior.novasoft.http.camel.utils.enums.PrimitiveEnum;
import br.com.senior.novasoft.http.camel.utils.enums.ServiceEnum;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import static br.com.senior.novasoft.http.camel.entities.login.LoginInput.LOGIN_INPUT_FORMAT;
import static br.com.senior.novasoft.http.camel.utils.constants.AuthenticationApiConstants.*;
import static org.apache.camel.ExchangePattern.InOut;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationAPI {

    @NonNull
    private final RouteBuilder routeBuilder;
    private final UUID id = UUID.randomUUID();

    @Getter
    @Setter
    private final String directImpl = DIRECT_NOVASOFT_IMPL.concat(id.toString());

    @Getter
    @Setter
    private final String directResponse = DIRECT_NOVASOFT_IMPL_RESPONSE.concat(id.toString());

    @Getter
    @Setter
    private String url;

    public void prepare() {
        prepare(exchange -> {
        });
    }

    public void prepare(Processor enrichWithToken) {
        tokenFound();
        tokenNotFound();
        login();
        routeBuilder //
            .from(directImpl) //
            .routeId(AUTHENTICATE) //
            .to("log:authenticate") //
            .process(this::searchToken) //
//            .choice() // Token found
//            .when(routeBuilder.method(this, "tokenFound"))//
//            .setExchangePattern(InOut) //
//            .to(DIRECT_TOKEN_FOUND) //
//            .otherwise() // Token not found
//            .setExchangePattern(InOut) //
            .to(DIRECT_TOKEN_NOT_FOUND) //
//            .end() // Token found
            .process(enrichWithToken) //
            .to(directResponse) //
        ;
    }

    public static void addAuthorization(Exchange exchange) {
        LoginOutput loginOutput = (LoginOutput) exchange.getProperty(TOKEN);
        exchange.getMessage().setHeader("Authorization", "Bearer " + loginOutput.getToken());
    }

    private void tokenFound() {
        routeBuilder //
            .from(DIRECT_TOKEN_FOUND) //
            .routeId("token-found-novasoft") //
            .to("log:tokenFound") //
            .choice() // Expired token
            .when(routeBuilder.method(this, "isExpiredToken")) //
            .to("log:tokenExpired") //
            .setExchangePattern(InOut) //
            .to(DIRECT_TOKEN_NOT_FOUND) //
            .to("log:refreshedToken") //
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
            .setExchangePattern(InOut) //
            .to(DIRECT_LOGIN) //
            .to("log:authenticated") //
            .unmarshal(LoginOutput.LOGIN_OUTPUT_FORMAT) //
            .process(this::unmarshallToken) //
        ;
    }

    private void login() {
        NovasoftHTTPRouteBuilder login = new NovasoftHTTPRouteBuilder();
        login.setMethod(MethodEnum.POST);
        login.setServiceEnum(ServiceEnum.CUENTA);
        login.setPrimitiveEnum(PrimitiveEnum.LOGIN);

        routeBuilder //
            .from(DIRECT_LOGIN) //
            .process(exchange -> login.setUrl(url)) //
            .routeId("login-novasoft") //
            .marshal(LOGIN_INPUT_FORMAT) //
            .to("log:login") //
            .setExchangePattern(InOut) //
            .process(login::request) //
            .to("log:logged") //
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
            exchange.setProperty(TOKEN, loginOutput);
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