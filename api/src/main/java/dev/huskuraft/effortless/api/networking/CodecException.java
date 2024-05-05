package dev.huskuraft.effortless.api.networking;

public class CodecException extends RuntimeException {

    public CodecException() {
    }

    public CodecException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodecException(String message) {
        super(message);
    }

    public CodecException(Throwable cause) {
        super(cause);
    }
}
