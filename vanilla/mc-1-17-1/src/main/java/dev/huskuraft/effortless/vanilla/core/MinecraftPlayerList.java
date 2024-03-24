package dev.huskuraft.effortless.vanilla.core;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.PlayerProfile;
import dev.huskuraft.effortless.api.platform.PlayerList;

public class MinecraftPlayerList implements PlayerList {

    private final net.minecraft.server.players.PlayerList reference;

    public MinecraftPlayerList(net.minecraft.server.players.PlayerList reference) {
        this.reference = reference;
    }

    @Override
    public net.minecraft.server.players.PlayerList referenceValue() {
        return reference;
    }

    @Override
    public List<Player> getPlayers() {
        return reference.getPlayers().stream().map(MinecraftPlayer::ofNullable).collect(Collectors.toList());
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return MinecraftPlayer.ofNullable(reference.getPlayer(uuid));
    }

    @Override
    public boolean isOperator(PlayerProfile profile) {
        return reference.isOp(profile.reference());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftPlayerList obj1 && reference.equals(obj1.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }
}
