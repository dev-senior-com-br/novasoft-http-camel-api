package br.com.senior.novasoft.http.camel.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

@Slf4j
public class ForwardProcessor implements Processor {

    private final Exchange source;

    public ForwardProcessor(Exchange source) {
        this.source = source;
    }

    @Override
    public void process(Exchange exchange) {
        prepare(source, exchange);
    }

    private void prepare(Exchange source, Exchange exchange) {
        Message sourceMessage = source.getMessage();
        Message message = exchange.getMessage();
        message.setBody(sourceMessage.getBody());
        sourceMessage
            .getHeaders()
            .entrySet()
            .forEach(stringObjectEntry -> {//
                message.setHeader(//
                    stringObjectEntry.getKey(),//
                    stringObjectEntry.getValue()//
                );
            });
        log.info("Body {}", message.getBody());
        log.info("Headers {}", message.getHeaders());
    }

    public void reverse(Exchange exchange) {
        prepare(exchange, source);
    }
}
