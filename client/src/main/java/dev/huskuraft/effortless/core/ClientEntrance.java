package dev.huskuraft.effortless.core;

import dev.huskuraft.effortless.config.ClientConfigManager;
import dev.huskuraft.effortless.events.ClientEventsRegistry;
import dev.huskuraft.effortless.platform.Client;
import dev.huskuraft.effortless.platform.ClientManager;

public abstract class ClientEntrance extends Entrance {

    public abstract ClientEventsRegistry getEventRegistry();

    public abstract ClientManager getClientManager();

    public abstract ClientConfigManager getConfigManager();

    public Client getClient() {
        return getClientManager().getRunningClient();
    }

}

