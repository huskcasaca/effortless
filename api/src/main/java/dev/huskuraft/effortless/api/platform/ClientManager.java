package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.gui.Screen;

public interface ClientManager {

    Client getRunningClient();

    void setRunningClient(Client client);

    void pushScreen(Screen screen);

    void popScreen(Screen screen);

}
