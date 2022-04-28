package br.com.senior.novasoft.http.camel.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import java.util.Map;

@Slf4j
public class ForwardProcessor implements Processor {

    private final Exchange source;

    public ForwardProcessor(Exchange source) {
        this.source = source;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        prepare(source, exchange);
    }

    private void prepare(Exchange src, Exchange dest) {
        Message sourceMessage = src.getMessage();
        Message message = dest.getMessage();
        message.setBody(sourceMessage.getBody());
        for (Map.Entry<String, Object> entry : sourceMessage.getHeaders().entrySet()) {
            message.setHeader(entry.getKey(), entry.getValue());
        }
        log.info("Body {}", message.getBody());
        log.info("Headers {}", message.getHeaders());
    }

    public void reverse(Exchange exchange) {
        prepare(exchange, source);
    }
}
