package dev.huskuraft.effortless.vanilla.core;

import java.util.UUID;

import dev.huskuraft.effortless.api.core.PlayerInfo;
import dev.huskuraft.effortless.api.text.Text;

public class MinecraftPlayerInfo implements PlayerInfo {

    protected final net.minecraft.client.multiplayer.PlayerInfo reference;

    public MinecraftPlayerInfo(net.minecraft.client.multiplayer.PlayerInfo reference) {
        this.reference = reference;
    }

    @Override
    public net.minecraft.client.multiplayer.PlayerInfo referenceValue() {
        return reference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftPlayerInfo player && reference.equals(player.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

    @Override
    public UUID id() {
        return reference.getProfile().getId();
    }

    @Override
    public String name() {
        return reference.getProfile().getName();
    }

    @Override
    public Text displayName() {
        return MinecraftText.ofNullable(reference.getTabListDisplayName());
    }
}
