package dev.huskuraft.effortless.screen.clipboard;

import java.util.function.Consumer;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.MessageTextWidget;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.gui.tooltip.TooltipHelper;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.clipboard.Clipboard;
import dev.huskuraft.effortless.building.clipboard.Snapshot;
import dev.huskuraft.effortless.networking.packets.player.PlayerSnapshotSharePacket;
import dev.huskuraft.effortless.screen.player.EffortlessOnlinePlayersScreen;

public class EffortlessClipboardScreen extends AbstractPanelScreen {

    private final Consumer<Clipboard> consumer;
    private Clipboard clipboard;
    private AbstractWidget titleTextWidget;
    private MessageTextWidget textWidget;
    private StructureSnapshotWidget snapshotWidget;
    private Button enableButton;
    private Button doneButton;
    private Button historyButton;
    private Button clearButton;
    private Button editButton;
    private Button shareButton;
    private Button exportButton;
    private Button importButton;
//    private EditBox nameEditBox;

    public EffortlessClipboardScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.clipboard.edit.title"), PANEL_WIDTH_50, PANEL_HEIGHT_FULL);
        this.consumer = clipboard -> {
            getEntrance().getStructureBuilder().setClipboard(getEntrance().getClient().getPlayer(), this.clipboard);
        };
        this.clipboard = getEntrance().getStructureBuilder().getContext(getEntrance().getClient().getPlayer()).clipboard();
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
//        setHeight(clipboard.enabled() ? PANEL_HEIGHT_3_4 : PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_2);
//        setWidth(clipboard.enabled() ? PANEL_WIDTH_EXPANDED : PANEL_WIDTH);

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

        this.enableButton = addWidget(Button.builder(getEntrance(), clipboard.enabled() ? Text.translate("effortless.clipboard.button.disable") : Text.translate("effortless.clipboard.button.enable"), button -> {
            this.clipboard = clipboard.withEnabled(!clipboard.enabled());
            recreate();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_1, 0f, 0f, 1f).build());


        this.historyButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.history"), button -> {
            new EffortlessClipboardHistoryScreen(getEntrance(), snapshot -> {
                this.clipboard = this.clipboard.withSnapshot(snapshot);
            }).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0 / 4f, 1 / 4f).build());

        this.snapshotWidget = addWidget(new StructureSnapshotWidget(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_1N, getWidth() - PADDINGS_H * 2, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_1N - PANEL_BUTTON_ROW_HEIGHT_2, clipboard.snapshot()));
        this.textWidget = addWidget(new MessageTextWidget(getEntrance(), getLeft() + AbstractPanelScreen.PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_1N, getWidth() - AbstractPanelScreen.PADDINGS_H * 2, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_1N - PANEL_BUTTON_ROW_HEIGHT_2, Text.translate("effortless.clipboard.no_structure"), MessageTextWidget.Gravity.CENTER));

        this.clearButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.clear"), button -> {
            this.clipboard = this.clipboard.withSnapshot(Snapshot.EMPTY);

        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 2 / 4f, 1 / 4f).build());

        this.editButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.edit"), button -> {

        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 3 / 4f, 1 / 4f).build());

        this.shareButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.share"), button -> {
            new EffortlessOnlinePlayersScreen(getEntrance(), playerInfo -> {
                getEntrance().getChannel().sendPacket(new PlayerSnapshotSharePacket(getPlayer().getId(), playerInfo.getId(), clipboard.snapshot()));
            }).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 1 / 4f, 1 / 4f).build());
        this.exportButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.export"), button -> {
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 1 / 3f, 1 / 3f).build());

        this.importButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.import"), button -> {
            new EffortlessClipboardHistoryScreen(getEntrance(), snapshot -> {
                this.clipboard = this.clipboard.withSnapshot(snapshot);
            }).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 2 / 3f, 1 / 3f).build());

        this.doneButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detachAll();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0 / 3f, 1 / 3f).build());

    }

    @Override
    public void onReload() {
        this.snapshotWidget.setSnapshot(this.clipboard.snapshot());
        this.consumer.accept(this.clipboard);
        this.clearButton.setTooltip(TooltipHelper.makeSummary(getTypeface(), Text.translate("effortless.button.clear"), Text.translate("Clear the structure in your current clipboard.")));
        this.shareButton.setTooltip(TooltipHelper.makeSummary(getTypeface(), Text.translate("effortless.button.share"), Text.translate("Share this structure with an online player on this sever.")));
        this.exportButton.setTooltip(TooltipHelper.makeSummary(getTypeface(), Text.translate("effortless.button.export"), Text.translate("Export this structure to your structure collections or system clipboard.")));
        this.importButton.setTooltip(TooltipHelper.makeSummary(getTypeface(), Text.translate("effortless.button.import"), Text.translate("Import a structure from your structure collections or system clipboard.")));
//        this.exportButton.setTooltip(
//                TooltipHelper.wrapLines(getTypeface(), Text.translate("Export this structure to your system clipboard. You can paste this structure text to share with anyone else."))
//        );
//        this.importButton.setTooltip(
//                TooltipHelper.wrapLines(getTypeface(), Text.translate("Import a structure from your system clipboard. You can copy a structure text and import here."))
//        );
        this.textWidget.setVisible(clipboard.isEmpty());
        this.editButton.setActive(!clipboard.isEmpty());
        this.clearButton.setActive(!clipboard.isEmpty());
        this.shareButton.setActive(!clipboard.isEmpty());
        this.exportButton.setActive(!clipboard.isEmpty());
    }

}
