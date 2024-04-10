package dev.huskuraft.effortless;

import java.util.concurrent.atomic.AtomicReference;

import dev.huskuraft.effortless.api.platform.Server;

public final class EffortlessServerManager {

    private final Effortless entrance;

    private final AtomicReference<Server> runningServer = new AtomicReference<>();

    private int interactionCooldown = 0;

    public EffortlessServerManager(Effortless entrance) {
        this.entrance = entrance;

        getEntrance().getEventRegistry().getServerStartedEvent().register(this::onServerStarted);
        getEntrance().getEventRegistry().getServerStoppedEvent().register(this::onServerStarted);
    }

    private Effortless getEntrance() {
        return entrance;
    }

    public void onServerStarted(Server server) {
        this.runningServer.set(server);
    }

    public void onServerStopped(Server server) {
        this.runningServer.set(null);
    }

    public Server getRunningServer() {
        return runningServer.get();
    }

}
