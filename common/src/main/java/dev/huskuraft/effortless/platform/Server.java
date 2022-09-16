package dev.huskuraft.effortless.platform;

import dev.huskuraft.effortless.core.Player;

import java.util.List;

public abstract class Server {

    public abstract List<Player> getPlayers();

    public abstract void execute(Runnable runnable);

}
