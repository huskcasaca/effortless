package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.platform.PlayerList;
import dev.huskuraft.effortless.api.platform.Server;

public record MinecraftServer(
        net.minecraft.server.MinecraftServer refs
) implements Server {

    @Override
    public PlayerList getPlayerList() {
        return new MinecraftPlayerList(refs.getPlayerList());
    }

    @Override
    public void execute(Runnable runnable) {
        refs.execute(runnable);
    }

}
