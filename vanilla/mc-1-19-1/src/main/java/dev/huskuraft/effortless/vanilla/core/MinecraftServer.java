package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.platform.PlayerList;
import dev.huskuraft.effortless.api.platform.Server;

public class MinecraftServer implements Server {

    private final net.minecraft.server.MinecraftServer reference;

    public MinecraftServer(net.minecraft.server.MinecraftServer reference) {
        this.reference = reference;
    }

    @Override
    public net.minecraft.server.MinecraftServer referenceValue() {
        return reference;
    }

    @Override
    public PlayerList getPlayerList() {
        return new MinecraftPlayerList(reference.getPlayerList());
    }

    @Override
    public void execute(Runnable runnable) {
        reference.execute(runnable);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftServer server && reference.equals(server.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }
}
