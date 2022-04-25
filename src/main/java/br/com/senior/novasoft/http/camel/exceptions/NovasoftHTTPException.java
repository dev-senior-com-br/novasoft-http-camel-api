package br.com.senior.novasoft.http.camel.exceptions;

public class NovasoftHTTPException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NovasoftHTTPException(String message) {
        super(message);
    }

    public NovasoftHTTPException(Throwable cause) {
        super(cause);
    }
}
