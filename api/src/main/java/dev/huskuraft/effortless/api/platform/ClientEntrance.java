package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.events.ClientEventRegistry;

public interface ClientEntrance extends Entrance {

    static ClientEntrance getInstance() {
        return PlatformLoader.getSingleton();
    }

    ClientManager getClientManager();

    ClientEventRegistry getEventRegistry();

    default Client getClient() {
        return getClientManager().getRunningClient();
    }

}

