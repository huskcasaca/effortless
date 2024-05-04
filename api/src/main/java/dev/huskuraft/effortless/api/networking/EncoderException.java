package dev.huskuraft.effortless.api.networking;

public class EncoderException extends CodecException {

    public EncoderException() {
    }

    public EncoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncoderException(String message) {
        super(message);
    }

    public EncoderException(Throwable cause) {
        super(cause);
    }
}
