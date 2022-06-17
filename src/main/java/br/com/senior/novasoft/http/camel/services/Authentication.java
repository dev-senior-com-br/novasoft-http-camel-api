package br.com.senior.novasoft.http.camel.services;

import br.com.senior.novasoft.http.camel.entities.login.LoginInput;
import br.com.senior.novasoft.http.camel.entities.login.LoginOutput;
import br.com.senior.novasoft.http.camel.utils.constants.AuthenticationApiConstants;
import br.com.senior.novasoft.http.camel.utils.enums.MethodEnum;
import br.com.senior.novasoft.http.camel.utils.enums.PrimitiveEnum;
import br.com.senior.novasoft.http.camel.utils.enums.ServiceEnum;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public class Authentication
{
    @NonNull
    private final RouteBuilder routeBuilder;
    private final UUID id = UUID.randomUUID();
    @Getter
    private final String directImpl = AuthenticationApiConstants.DIRECT_NOVASOFT_IMPL.concat(id.toString());
    @Getter
    private final String directResponse = AuthenticationApiConstants.DIRECT_NOVASOFT_IMPL_RESPONSE.concat(id.toString());
    @Setter
    private String url;

    void prepare()
    {
        prepareLogin();

        routeBuilder
            .from(directImpl)
            .choice()
            .when(this::settedLoginInputInBody)
            .to(AuthenticationApiConstants.DIRECT_LOGIN)
            .otherwise()
            .log("LoginInput from Novasoft not found")
            .endRest()
        ;
    }

    private void prepareLogin()
    {
        NovasoftHTTPRouteBuilder login = new NovasoftHTTPRouteBuilder();
        login.setMethod(MethodEnum.POST);
        login.setServiceEnum(ServiceEnum.CUENTA);
        login.setPrimitiveEnum(PrimitiveEnum.LOGIN);

        routeBuilder
            .from(AuthenticationApiConstants.DIRECT_LOGIN)
            .process(exchange -> login.setUrl(url))
            .process(login::request)
            .unmarshal(LoginOutput.LOGIN_OUTPUT_FORMAT)
            .choice()
            .when(this::isRequestGenerateTokenSuccessful)
            .process(exchange -> {
                exchange.setProperty(AuthenticationApiConstants.TOKEN, exchange.getMessage().getBody(LoginOutput.class));
                addAuthorization(exchange);
            })
            .end()
            .to(directResponse)
        ;
    }

    private boolean settedLoginInputInBody(Exchange exchange)
    {
        Message message = exchange.getMessage();
        return message.getBody() instanceof LoginInput;
    }

    private boolean isRequestGenerateTokenSuccessful(Exchange exchange)
    {
        return exchange.getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class).equals(200);
    }

    public static void addAuthorization(Exchange exchange)
    {
        LoginOutput loginOutput = exchange.getProperty(AuthenticationApiConstants.TOKEN, LoginOutput.class);
        exchange.getMessage().setHeader("Authorization", "Bearer " + loginOutput.getToken());
    }
}
