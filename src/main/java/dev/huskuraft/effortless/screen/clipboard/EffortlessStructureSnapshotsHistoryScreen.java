package dev.huskuraft.effortless.screen.clipboard;

import java.util.List;
import java.util.function.Consumer;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.AbstractWidget;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.clipboard.Snapshot;

public class EffortlessStructureSnapshotsHistoryScreen extends AbstractPanelScreen {

    private final Consumer<Snapshot> consumer;
    private List<Snapshot> history;
    private AbstractWidget titleTextWidget;
    private StructureSnapshotList entries;

    private Button cancelButton;
    private Button selectButton;
    private Button editButton;
    private Button shareButton;
    private Button deleteButton;
    private Button exportButton;
    private Button favButton;
    private Button addToLibraryButton;

    public EffortlessStructureSnapshotsHistoryScreen(Entrance entrance, Consumer<Snapshot> consumer) {
        super(entrance, Text.translate("effortless.structure_snapshots_history.title"), AbstractPanelScreen.PANEL_WIDTH_60, PANEL_HEIGHT_FULL);
        this.consumer = consumer;
        this.history = getEntrance().getConfigStorage().get().clipboardConfig().history();
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    protected Player getPlayer() {
        return getEntrance().getClient().getPlayer();
    }


    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        this.entries = addWidget(new StructureSnapshotList(getEntrance(), getLeft() + AbstractPanelScreen.PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - AbstractPanelScreen.PADDINGS_H * 2 - 8 /* scrollbar */, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_1));
        this.entries.setAlwaysShowScrollbar(true);
        this.entries.reset(history);

//        this.editButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.edit"), button -> {
//
//        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 4 / 5f, 1 / 5f).build());
//
//        this.shareButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.share"), button -> {
//            new EffortlessOnlinePlayersScreen(getEntrance(), playerInfo -> {
//                getEntrance().getChannel().sendPacket(new PlayerSnapshotSharePacket(getPlayer().getId(), playerInfo.getId(), entries.getSelected().getItem()));
//                detach();
//            }).attach();
//        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 2 / 5f, 1 / 5f).build());
//
//        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.delete"), button -> {
//            if (this.entries.hasSelected()) {
//                this.entries.deleteSelected();
//            }
//        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 3 / 5f, 1 / 5f).build());
//
//
//        this.exportButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.export"), button -> {
//
//        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 1 / 5f, 1 / 5f).build());
//
//
//        this.favButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.star"), button -> {
//
//        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0 / 5f, 1 / 5f).build());


//        this.addToLibraryButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save_to_library"), button -> {
//            if (this.entries.hasSelected()) {
//                getEntrance().getConfigStorage().update(clientConfig -> clientConfig.withClipboardConfig(clientConfig.clipboardConfig().appendCollection(this.entries.getSelected().getItem())));
//            }
//        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0 / 5f, 5 / 5f).build());

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 1 / 2f).build());


        this.selectButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.use_in_clipboard"), button -> {
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
//        this.editButton.setActive(this.entries.hasSelected());
//
//        this.shareButton.setTooltip(TooltipHelper.makeSummary(getTypeface(), Text.translate("effortless.button.share"), Text.translate("Share this structure with an online player on this sever.")));
//        this.shareButton.setActive(this.entries.hasSelected());
//
//        this.deleteButton.setActive(this.entries.hasSelected());
//
//        this.exportButton.setTooltip(TooltipHelper.makeSummary(getTypeface(), Text.translate("effortless.button.export"), Text.translate("Export this structure to your structure collections or system clipboard.")));
//        this.exportButton.setActive(this.entries.hasSelected());
//
//        this.favButton.setActive(this.entries.hasSelected());

//        addToLibraryButton.setActive(this.entries.hasSelected() && !getEntrance().getConfigStorage().get().clipboardConfig().collections().contains(this.entries.getSelected().getItem()));

    }

}
