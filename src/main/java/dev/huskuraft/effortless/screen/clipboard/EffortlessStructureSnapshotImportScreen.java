package dev.huskuraft.effortless.screen.clipboard;

import java.util.function.Consumer;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.clipboard.Snapshot;

public class EffortlessStructureSnapshotImportScreen extends AbstractPanelScreen {

    private final Consumer<Snapshot> consumer;

    public EffortlessStructureSnapshotImportScreen(Entrance entrance, Consumer<Snapshot> consumer) {
        super(entrance, Text.translate("effortless.structure.import.title"), PANEL_WIDTH_42, PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_3);
        this.consumer = consumer;
    }

    private Button importFromLitematicButton;
    private Button importFromEffortlessButton;

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {
        addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        this.importFromLitematicButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.structure.import.import_litematic", ".litematic"), button -> {
            detach();


        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 2f, 0f, 1f).build());
        this.importFromEffortlessButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.structure.import.import_effortless", ".effortless"), button -> {
            detach();

        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 1f).build());

        addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 1f).build());

    }

    @Override
    public void onReload() {

    }
}
