package dev.huskuraft.effortless.api.networking;

public interface PacketListener {

    default boolean shouldPropagateHandlingExceptions() {
        return true;
    }

}
