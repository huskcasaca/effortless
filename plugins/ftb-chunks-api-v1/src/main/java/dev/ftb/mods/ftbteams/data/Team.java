package dev.ftb.mods.ftbteams.data;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.minecraft.Util;
import net.minecraft.server.level.ServerPlayer;

public abstract class Team extends TeamBase {

    @Override
    public boolean isValid() {
        throw new RuntimeException("stub!");
    }


    void onCreated(@Nullable ServerPlayer p) {
        throw new RuntimeException("stub!");
    }

    void updateCommands(ServerPlayer player) {
        throw new RuntimeException("stub!");
    }

    void changedTeam(@Nullable Team prev, UUID player, @Nullable ServerPlayer p, boolean deleted) {
        throw new RuntimeException("stub!");
    }

    public UUID getOwner() {
        return Util.NIL_UUID;
    }

}
