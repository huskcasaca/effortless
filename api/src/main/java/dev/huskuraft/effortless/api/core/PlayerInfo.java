package dev.huskuraft.effortless.api.core;

import java.util.UUID;

import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.text.Text;

public interface PlayerInfo extends PlatformReference {

    PlayerProfile getProfile();

    Text getDisplayName();

    PlayerSkin getSkin();

    default UUID getId() {
        return getProfile().getId();
    }

    default String getName() {
        return getProfile().getName();
    }

}
