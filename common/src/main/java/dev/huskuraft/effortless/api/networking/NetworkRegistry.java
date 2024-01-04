package dev.huskuraft.effortless.api.networking;

public interface NetworkRegistry {

    BufferSender register(BufferReceiver receiver);

}
