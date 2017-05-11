package fr.sw.fwk.common;

public class SwException extends RuntimeException {
    public SwException(String message) {
        super(message);
    }

    public SwException(String message, Throwable cause) {
        super(message, cause);
    }

    public SwException(Throwable cause) {
        super(cause);
    }
}
