package br.com.senior.novasoft.http.camel.services;

import br.com.senior.novasoft.http.camel.entities.client.Cliente;
import io.vertx.core.json.JsonArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
            log.error("Error JsonFormatter Array: {}", exception);
        }
    }
}
