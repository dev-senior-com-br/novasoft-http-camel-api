package br.com.senior.novasoft.http.camel.services;

import br.com.senior.novasoft.http.camel.entities.client.ClienteErrorOutput;
import br.com.senior.novasoft.http.camel.entities.client.ClienteOutput;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;

import java.util.ArrayList;
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

    /**
     * Pega o output de erro
     *
     * @param exchange {@link Exchange} - Exchange do camel.
     */
    public static void formatterArray(Exchange exchange) {
        try {
            List<ClienteErrorOutput> clienteErrorOutputs = new ArrayList<>();
            List<Map<String, Object>> mapArray = (List<Map<String, Object>>) exchange.getMessage().getBody();

            mapArray.forEach(map -> {
                JsonObject jsonObject = new JsonObject(map);
                log.info("Json Formatter Array: {}", jsonObject.toString());

                clienteErrorOutputs.add(
                    new ClienteErrorOutput(
                        jsonObject.getString("cliente"),
                        jsonObject.getString("mensaje"),
                        jsonObject.getBoolean("indError")
                    )
                );
            });

            exchange.getMessage().setBody(new ClienteOutput(clienteErrorOutputs));
        } catch (Exception exception) {
            log.error("Error JsonFormatter Array: {}", exception);
        }
    }
}
