package dev.huskuraft.effortless.screen.clipboard;

import java.util.List;
import java.util.function.Consumer;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.clipboard.Snapshot;

public class EffortlessClipboardHistoryScreen extends AbstractPanelScreen {

    private final Consumer<Snapshot> consumer;
    private List<Snapshot> history;
    private AbstractWidget titleTextWidget;
    private StructureSnapshotList entries;

    private Button cancelButton;
    private Button selectButton;

    public EffortlessClipboardHistoryScreen(Entrance entrance, Consumer<Snapshot> consumer) {
        super(entrance, Text.translate("effortless.clipboard.history.title"), AbstractPanelScreen.PANEL_WIDTH_64, PANEL_HEIGHT_FULL);
        this.consumer = consumer;
        this.history = getEntrance().getConfigStorage().get().clipboardConfig().history();
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        this.entries = addWidget(new StructureSnapshotList(getEntrance(), getLeft() + AbstractPanelScreen.PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - AbstractPanelScreen.PADDINGS_H * 2 - 8 /* scrollbar */, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_1));
        this.entries.setAlwaysShowScrollbar(true);
        this.entries.reset(history);

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 1 / 2f).build());

        this.selectButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.select"), button -> {
            if (this.entries.hasSelected()) {
                this.consumer.accept(this.entries.getSelected().getItem());
            }
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 1 / 2f, 1 / 2f).build());

    }

    @Override
    public void onReload() {
        this.selectButton.setActive(this.entries.hasSelected());
        if (entries.consumeDoubleClick() && entries.hasSelected()) {
            this.consumer.accept(this.entries.getSelected().getItem());
            detach();
        }
    }

}
