package dev.huskuraft.effortless.api.platform;

import dev.huskuraft.effortless.api.gui.Screen;
import dev.huskuraft.effortless.client.SubtitleManager;
import dev.huskuraft.effortless.renderer.opertaion.OperationsRenderer;
import dev.huskuraft.effortless.renderer.outliner.OutlineRenderer;
import dev.huskuraft.effortless.renderer.pattern.PatternRenderer;

public interface ClientManager {

    Client getRunningClient();

    void setRunningClient(Client client);

    @Deprecated
    void pushPanel(Screen screen);

    @Deprecated
    void popPanel(Screen screen);

    SubtitleManager getSubtitleManager();

    OperationsRenderer getOperationsRenderer();

    OutlineRenderer getOutlineRenderer();

    PatternRenderer getPatternRenderer();

}
