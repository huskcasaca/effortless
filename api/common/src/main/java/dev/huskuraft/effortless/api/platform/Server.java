package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.core.Player;

import java.util.List;

public interface Server extends PlatformReference{

    List<Player> getPlayers();

    void execute(Runnable runnable);

}
