package dev.ftb.mods.ftbteams.data;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public abstract class Team {

    public abstract boolean isValid();

    public abstract UUID getId();

    public abstract CompoundTag getExtraData();

    public abstract String getDisplayName();

    public abstract String getDescription();

    public abstract int getColor();

    public abstract boolean isFreeToJoin();

    public abstract int getMaxMessageHistorySize();

    public abstract String getStringID();

    public abstract Component getName();

    public abstract boolean isMember(UUID uuid);

    public abstract Set<UUID> getMembers();

    public abstract boolean isAlly(UUID profile);

    public abstract boolean isOfficer(UUID profile);

    public abstract boolean isInvited(UUID profile);

    public abstract void onCreated(@Nullable ServerPlayer p);

    public abstract void updateCommands(ServerPlayer player);

    public abstract void changedTeam(@Nullable Team prev, UUID player, @Nullable ServerPlayer p, boolean deleted);

    public abstract UUID getOwner();

}
