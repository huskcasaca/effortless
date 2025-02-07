package dev.ftb.mods.ftbteams.data;

import java.util.Set;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

public abstract class TeamBase {

    public TeamBase() {
    }

    public abstract boolean isValid();

    @Override
    public String toString() {
        return getStringID();
    }

    public UUID getId() {
        throw new RuntimeException("stub!");
    }

    public CompoundTag getExtraData() {
        throw new RuntimeException("stub!");
    }

    public String getDisplayName() {
        throw new RuntimeException("stub!");
    }

    public String getDescription() {
        throw new RuntimeException("stub!");
    }

    public int getColor() {
        throw new RuntimeException("stub!");
    }

    public boolean isFreeToJoin() {
        throw new RuntimeException("stub!");
    }

    public int getMaxMessageHistorySize() {
        throw new RuntimeException("stub!");
    }

    public String getStringID() {
        throw new RuntimeException("stub!");
    }

    public Component getName() {
        throw new RuntimeException("stub!");
    }

    public void save() {
    }

    public boolean isMember(UUID uuid) {
        throw new RuntimeException("stub!");
    }


    public Set<UUID> getMembers() {
        throw new RuntimeException("stub!");
    }

    public boolean isAlly(UUID profile) {
        throw new RuntimeException("stub!");
    }

    public boolean isOfficer(UUID profile) {
        throw new RuntimeException("stub!");
    }

    public boolean isInvited(UUID profile) {
        throw new RuntimeException("stub!");
    }

}
