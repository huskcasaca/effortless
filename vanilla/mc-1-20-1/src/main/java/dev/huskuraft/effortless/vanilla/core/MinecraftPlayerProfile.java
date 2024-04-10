package dev.huskuraft.effortless.vanilla.core;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import dev.huskuraft.effortless.api.core.PlayerProfile;

public class MinecraftPlayerProfile implements PlayerProfile {

    protected final GameProfile reference;

    public MinecraftPlayerProfile(GameProfile reference) {
        this.reference = reference;
    }

    @Override
    public GameProfile referenceValue() {
        return reference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftPlayerProfile obj1 && reference.equals(obj1.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

    @Override
    public UUID getId() {
        return reference.getId();
    }

    @Override
    public String getName() {
        return reference.getName();
    }

    @Override
    public Map<String, ? extends Collection<?>> getProperties() {
        return reference.getProperties().asMap();
    }
}
