package dev.huskuraft.effortless.platform;

import dev.huskuraft.effortless.gui.Screen;
import dev.huskuraft.effortless.renderer.opertaion.OperationsRenderer;
import dev.huskuraft.effortless.renderer.outliner.OutlineRenderer;
import dev.huskuraft.effortless.renderer.transformer.TransformerRenderer;

public abstract class ClientManager {

    public abstract Client getRunningClient();

    public abstract void setRunningClient(Client client);

    @Deprecated
    public abstract void pushPanel(Screen screen);

    @Deprecated
    public abstract void popPanel(Screen screen);

    public abstract SubtitleManager getSubtitleManager();

    public abstract OperationsRenderer getOperationsRenderer();

    public abstract OutlineRenderer getOutlineRenderer();

    public abstract TransformerRenderer getTransformerRenderer();

}
