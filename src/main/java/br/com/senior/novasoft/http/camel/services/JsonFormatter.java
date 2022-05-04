package br.com.senior.novasoft.http.camel.services;

import io.vertx.core.json.JsonArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;

import java.util.List;
import java.util.Map;

/**
 * Formata o Json para
 * o padrão da Novasoft.
 *
 * @author lucas.nunes
 */
@Slf4j
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
}
