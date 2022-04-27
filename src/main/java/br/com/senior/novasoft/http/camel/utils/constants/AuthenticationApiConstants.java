package br.com.senior.novasoft.http.camel.utils.constants;

import br.com.senior.novasoft.http.camel.entities.login.LoginOutput;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.time.Duration;

import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;
import static org.ehcache.config.builders.ExpiryPolicyBuilder.timeToLiveExpiration;
import static org.ehcache.config.units.MemoryUnit.B;

public class AuthenticationApiConstants {

    private AuthenticationApiConstants() {
    }

    public static final String HTTPS = "https";

    //Autenticação
    public static final String AUTHENTICATE = "authenticate-novasoft";
    public static final String HEADERS_LOG = "${in.headers}";

    //Directs
    public static final String DIRECT_NOVASOFT_IMPL = "direct:novasoft-impl-";
    public static final String DIRECT_NOVASOFT_IMPL_RESPONSE = "direct:novasoft-impl-response-";
    public static final String DIRECT_TOKEN_FOUND = "direct:impl-token-found-novasoft";
    public static final String DIRECT_TOKEN_NOT_FOUND = "direct:impl-token-not-found-novasoft";
    public static final String DIRECT_LOGIN = "direct:impl-login-novasoft";

    // Token
    public static final String TOKEN_CACHE_KEY = "token-cache-key-novasoft";
    public static final String TOKEN = "token-novasoft";
    public static final String TOKEN_CACHE_NAME = "tokenCacheNovasoft";
    /**
     * Tamanho do cache do token em bytes
     */
    public static final Long TOKEN_CACHE_SIZE = 64000000L;
    /**
     * Tempo para atualizar
     * o Token em segundos.
     * Veja a variável
     * de ambiente
     * KONG_REFRESH_TOKEN_TTL
     * em <a href="https://git.senior.com.br/arquitetura/kong-rest-client/-/wikis/home">git.senior</a>.
     */
    public static final Integer REFRESH_TOKEN_TTL = 15552000;
    /**
     * Margem de tempo de
     * expiração do token em segundos.
     */
    public static final Integer TOKEN_EXPIRATION_MARGIN = 60;

    //Cache
    public static final CacheManager CACHE_MANAGER = newCacheManagerBuilder().build(true);
    public static final Cache<String, LoginOutput> TOKEN_CACHE = CACHE_MANAGER
        .createCache(
            TOKEN_CACHE_NAME, //
            newCacheConfigurationBuilder(//
                String.class,//
                LoginOutput.class, //
                ResourcePoolsBuilder.newResourcePoolsBuilder()//
                    .heap(TOKEN_CACHE_SIZE, B)//
                    .build()//
            ).withExpiry(//
                    timeToLiveExpiration(Duration.ofSeconds(//
                            REFRESH_TOKEN_TTL//
                        )//
                    )//
                )//
                .build());
}
