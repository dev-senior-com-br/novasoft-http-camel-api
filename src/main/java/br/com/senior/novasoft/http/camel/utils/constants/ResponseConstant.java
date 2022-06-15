package br.com.senior.novasoft.http.camel.utils.constants;

/**
 * Constantes usadas para
 * formatação de resposta da
 * API da Novasoft
 *
 * @author lucas.nunes
 */
public class ResponseConstant {

    private ResponseConstant() {
    }

    /**
     * Mensajem padrão caso
     * não retorne nenhuma
     * mesagem de erro.
     */
    public static final String NO_INTEGRADO = "No Integrado";

    /**
     * Campo do JSON
     * em um cenário de erros.
     */
    public static final String MENSAJE = "mensaje";
}
