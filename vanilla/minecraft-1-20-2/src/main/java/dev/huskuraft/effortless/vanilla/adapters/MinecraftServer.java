package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.platform.Server;

import java.util.List;

public class MinecraftServer implements Server {

    private final net.minecraft.server.MinecraftServer reference;

    MinecraftServer(net.minecraft.server.MinecraftServer reference) {
        this.reference = reference;
    }

    public static Server fromMinecraftServer(net.minecraft.server.MinecraftServer reference) {
        return new MinecraftServer(reference);
    }

    @Override
    public net.minecraft.server.MinecraftServer referenceValue() {
        return reference;
    }

    @Override
    public List<Player> getPlayers() {
        return reference.getPlayerList().getPlayers().stream().map(MinecraftPlayer::fromMinecraftPlayer).toList();
    }

    @Override
    public void execute(Runnable runnable) {
        reference.executeIfPossible(runnable);
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
