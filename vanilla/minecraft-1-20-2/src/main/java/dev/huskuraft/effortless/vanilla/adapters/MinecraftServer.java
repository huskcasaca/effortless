package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.platform.Server;

import java.util.List;
import java.util.stream.Collectors;

public class MinecraftServer extends Server {

    private final net.minecraft.server.MinecraftServer minecraftServer;

    MinecraftServer(net.minecraft.server.MinecraftServer minecraftServer) {
        this.minecraftServer = minecraftServer;
    }

    public net.minecraft.server.MinecraftServer getRef() {
        return minecraftServer;
    }

    @Override
    public List<Player> getPlayers() {
        return getRef().getPlayerList().getPlayers().stream().map(MinecraftPlayer::new).collect(Collectors.toList());
    }

    @Override
    public void execute(Runnable runnable) {
        getRef().executeIfPossible(runnable);
    }
}
