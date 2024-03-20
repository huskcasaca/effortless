package dev.huskuraft.effortless.api.core;

import java.util.UUID;

import dev.huskuraft.effortless.api.platform.PlatformReference;
import dev.huskuraft.effortless.api.text.Text;

public interface PlayerInfo extends PlatformReference {

    UUID getId();

    String getName();

    Text getDisplayName();

    PlayerSkin getSkin();

}
