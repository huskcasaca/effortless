package dev.huskuraft.effortless.vanilla.core;

import java.util.UUID;

import dev.huskuraft.effortless.api.core.PlayerInfo;
import dev.huskuraft.effortless.api.core.PlayerSkin;
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
    public UUID getId() {
        return reference.getProfile().getId();
    }

    @Override
    public PlayerSkin getSkin() {
        var skin = reference.getSkin();
        return new PlayerSkin(
                MinecraftResourceLocation.ofNullable(skin.texture()),
                MinecraftResourceLocation.ofNullable(skin.capeTexture()),
                MinecraftResourceLocation.ofNullable(skin.elytraTexture()),
                switch (skin.model()) {
                    case SLIM -> PlayerSkin.Model.SLIM;
                    case WIDE -> PlayerSkin.Model.WIDE;
                }
        );
    }

    @Override
    public String getName() {
        return reference.getProfile().getName();
    }

    @Override
    public Text getDisplayName() {
        return MinecraftText.ofNullable(reference.getTabListDisplayName());
    }
}
