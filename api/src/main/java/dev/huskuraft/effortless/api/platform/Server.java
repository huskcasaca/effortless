package dev.huskuraft.effortless.api.platform;

import java.util.List;

import dev.huskuraft.effortless.api.core.Player;

public interface Server extends PlatformReference{

    List<Player> getPlayers();

    void execute(Runnable runnable);

}
