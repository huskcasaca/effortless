package dev.huskuraft.effortless.api.core;

import dev.huskuraft.effortless.api.platform.Client;
import dev.huskuraft.effortless.api.platform.ClientManager;
import dev.huskuraft.effortless.api.platform.ClientPlatform;

public abstract class ClientEntrance extends Entrance {

    public abstract ClientManager getClientManager();

    public abstract ClientPlatform getPlatform();

    public Client getClient() {
        return getClientManager().getRunningClient();
    }

    protected static ClientEntrance instance;

    public static ClientEntrance getInstance() {
        return instance;
    }

}

