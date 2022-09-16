package dev.huskuraft.effortless.networking;

public interface NetworkRegistry {

    BufferSender register(BufferReceiver receiver);

}
