package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.platform.Server;

import java.util.List;

public class MinecraftServer extends Server {

    private final net.minecraft.server.MinecraftServer reference;

    MinecraftServer(net.minecraft.server.MinecraftServer reference) {
        this.reference = reference;
    }

    @Override
    public List<Player> getPlayers() {
        return reference.getPlayerList().getPlayers().stream().map(MinecraftPlayer::fromMinecraftPlayer).toList();
    }

    @Override
    public void execute(Runnable runnable) {
        reference.executeIfPossible(runnable);
    }
}
