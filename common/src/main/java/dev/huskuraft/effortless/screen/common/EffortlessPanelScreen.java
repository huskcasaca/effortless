package dev.huskuraft.effortless.screen.common;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;

public abstract class EffortlessPanelScreen extends AbstractPanelScreen {
    protected EffortlessPanelScreen(Entrance entrance, Text title) {
        super(entrance, title);
    }



    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }
}
