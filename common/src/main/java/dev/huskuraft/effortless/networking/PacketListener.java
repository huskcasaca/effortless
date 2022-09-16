package dev.huskuraft.effortless.networking;

public interface PacketListener {

    default boolean shouldPropagateHandlingExceptions() {
        return true;
    }

}
