package dev.huskuraft.effortless.screen.common;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.universal.api.gui.AbstractScreen;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.Text;

public abstract class EffortlessScreen extends AbstractScreen {
    protected EffortlessScreen(Entrance entrance, Text title) {
        super(entrance, title);
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }
}
