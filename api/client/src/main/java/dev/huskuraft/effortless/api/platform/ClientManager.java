package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.gui.Screen;

public interface ClientManager {

    Client getRunningClient();

    void setRunningClient(Client client);

    @Deprecated
    void pushPanel(Screen screen);

    @Deprecated
    void popPanel(Screen screen);

}
