package br.com.senior.novasoft.http.camel.utils.https;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class AllowHost implements HostnameVerifier {

    private static Logger LOGGER = LoggerFactory.getLogger(AllowHost.class);

    private final String allowedInsecureHost;

    public AllowHost(String allowedInsecureHost) {
        this.allowedInsecureHost = allowedInsecureHost;
    }

    @Override
    public boolean verify(String hostname, SSLSession session) {
        boolean allowed = allowedInsecureHost.equals(hostname);
        if (allowed)
            LOGGER.info("Allowing {}", hostname);
        else
            LOGGER.error("Blocking {}", hostname);
        return allowed;
    }

}
