package br.com.senior.novasoft.http.camel.services;

import br.com.senior.novasoft.http.camel.entities.client.Cliente;
import br.com.senior.novasoft.http.camel.utils.enums.FromToFieldsAccountEnum;
import io.vertx.core.json.JsonArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static br.com.senior.novasoft.http.camel.utils.constants.ResponseConstant.MENSAJE;
import static br.com.senior.novasoft.http.camel.utils.constants.ResponseConstant.NO_INTEGRADO;

/**
 * Formata o Json para
 * o padrão da Novasoft.
 *
 * @author lucas.nunes
 */
@Slf4j
@Service
public class JsonFormatter {

    private JsonFormatter() {
    }

    /**
     * Formatada o body da {@link Exchange}
     * para o padrão de Json da Novasoft.
     * Coloca o Json em forma de Array
     * [{...}]
     *
     * @param exchange {@link Exchange} - Exchange do camel.
     */
    public static void formatter(Exchange exchange) {
        try {
            Map<Object, Object> map = (Map<Object, Object>) exchange.getMessage().getBody();
            List<Map<Object, Object>> listaMap = List.of(map);
            JsonArray jsonArray = new JsonArray(listaMap);
            log.info("Json Formatter: {}", jsonArray.toString());
            exchange.getMessage().setBody(jsonArray.toString());
        } catch (Exception exception) {
            log.error("Error JsonFormatter: {}", exception);
        }
    }

    /**
     * Pega o output de erro
     *
     * @param exchange {@link Exchange} - Exchange do camel.
     */
    public static void formatterArray(Exchange exchange) {
        try {
            Map<String, Object> mapBody = (Map<String, Object>) exchange.getMessage().getBody();
            log.info("JsonFormatter Array: {}", mapBody.toString());

            String type = (String) mapBody.get("type");
            String title = (String) mapBody.get("title");
            Integer status = (Integer) mapBody.get("status");
            String traceId = (String) mapBody.get("traceId");
            String errors = (String) mapBody.get("errors");

            Cliente cliente = new Cliente();
            cliente.setType(type);
            cliente.setTitle(title);
            cliente.setStatus(status);
            cliente.setTraceId(traceId);
            cliente.setErrors(errors);

            exchange.getMessage().setBody(cliente);
        } catch (Exception exception) {
            log.error("Error JsonFormatter Array: ", exception);
        }
    }

    /**
     * <h1>Formata resposta de erro.</h1>
     *
     * <p>Formata a reposta de erro da
     * API CXC/Senior/Accounts, onde existem dois
     * retornos de erros possíveis.</p>
     *
     * <p>O primeiro retorno possível
     * é o seguinte:</p>
     *
     * <p>"errors":[{"campo1": "valor", "campo2": "valor"}]</p>
     *
     * <hr>
     *
     * <p>O segundo retorno possível
     * é o seguinte:</p>
     *
     * <p>"errors": {"campo": ["valor1"]}</p>
     *
     * <hr>
     *
     * <p>Independente do retorno ele será tratado
     * e retornado a {@link String} formatada com
     * os erros traduzidos e organizados.</p>
     *
     * @param responseError {@link Object} - Reposta de
     *                      erro da requisição para
     *                      a API Account da Novasoft.
     * @return {@link String} - Referente a {@link String}
     * formatada com as mensagens de erro organizadas.
     */
    public static String formatErrorResponse(Object responseError) {
        List<String> contents = new ArrayList<>();
        try {
            List<Map<String, String>> errorsArray = (List<Map<String, String>>) responseError;

            errorsArray.forEach(
                stringStringMap -> {
                    if (stringStringMap.get(MENSAJE) != null) {
                        contents.add(stringStringMap.get(MENSAJE));
                    }
                }
            );
        } catch (Exception exception) {
            Map<String, List<String>> errorsObject = (Map<String, List<String>>) responseError;

            Arrays.stream(FromToFieldsAccountEnum.values()).forEach(
                fromToFieldsAccountEnum -> {
                    String errorName = fromToFieldsAccountEnum.getErrorName();
                    List<String> contentError = errorsObject.getOrDefault(errorName, List.of());

                    if (!(contentError.isEmpty())) {
                        contentError.forEach(
                            error -> contents.add(error.replace(errorName, fromToFieldsAccountEnum.getName()))
                        );
                    }
                }
            );
        }
        return formatContent(contents);
    }

    /**
     * <h1>Formata conteúdo da resposta.</h1>
     *
     * <p>Formata conteúdo da resposta
     * de erro. Recebe a lista de mensagens
     * e faz um {@link String#join(CharSequence, CharSequence...)}
     * onde a cada intervalo ele coloca um \n, ou seja,
     * se tiver uma lista com os valores a e b
     * e irá colocar a \n b.</p>
     *
     * @param contents {@link List}<{@link String}> - Referente
     *                 a lista de mensagens de conteúdo.
     * @return {@link String} - Referente a {@link String}
     * formatada com as mensagens de erro organizadas.
     */
    private static String formatContent(List<String> contents) {
        log.info("Error response formatted successfully.");
        return contents.isEmpty()
            ? NO_INTEGRADO
            : String.join("\n", contents);
    }
}
