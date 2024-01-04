package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.api.platform.ClientManager;
import dev.huskuraft.effortless.api.platform.ClientPlatform;
import dev.huskuraft.effortless.client.ClientEventsRegistry;
import dev.huskuraft.effortless.config.ClientConfigManager;
import dev.huskuraft.effortless.core.Entrance;

public abstract class ClientEntrance extends Entrance {

    public abstract ClientEventsRegistry getEventRegistry();

    public abstract ClientManager getClientManager();

    public abstract ClientConfigManager getConfigManager();

    public abstract ClientPlatform getPlatform();

    public Client getClient() {
        return getClientManager().getRunningClient();
    }

}

