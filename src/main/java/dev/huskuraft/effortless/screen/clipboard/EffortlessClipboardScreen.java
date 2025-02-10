package dev.huskuraft.effortless.screen.clipboard;

import java.util.function.Consumer;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.universal.api.core.Player;
import dev.huskuraft.universal.api.gui.AbstractPanelScreen;
import dev.huskuraft.universal.api.gui.AbstractWidget;
import dev.huskuraft.universal.api.gui.button.Button;
import dev.huskuraft.universal.api.gui.text.MessageTextWidget;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.gui.tooltip.TooltipHelper;
import dev.huskuraft.universal.api.input.OptionKeys;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.Text;
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
    private Button libraryButton;
    private Button historyButton;
    private Button playerSharingButton;
    private Button clearButton;
    private Button editButton;
    private Button shareButton;
//    private EditBox nameEditBox;

    public EffortlessClipboardScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.clipboard.edit.title"), PANEL_WIDTH_60, PANEL_HEIGHT_FULL);
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
        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + PANEL_TITLE_HEIGHT_1 - 10, getScreenTitle().withColor(AbstractPanelScreen.TITLE_COLOR), TextWidget.Gravity.CENTER));

//        this.enableButton = addWidget(Button.builder(getEntrance(), clipboard.enabled() ? Text.translate("effortless.clipboard.button.disable") : Text.translate("effortless.clipboard.button.enable"), button -> {
//            this.clipboard = clipboard.withEnabled(!clipboard.enabled());
//            recreate();
//        }).setBoundsGrid(getLeft(), getTop(), getWidth(), PANEL_TITLE_HEIGHT_1 + PANEL_BUTTON_ROW_HEIGHT_1, 0f, 0f, 1f).build());

//        this.collectionsButton = addWidget(Button.builder(getEntrance(), Icons.FAVOURITE, button -> {
//            new EffortlessClipboardHistoryScreen(getEntrance(), snapshot -> {
//                this.clipboard = this.clipboard.withSnapshot(snapshot);
//            }).attach();
//        }).setBounds(getRight() - Button.DEFAULT_HEIGHT - PADDINGS_H - Button.DEFAULT_HEIGHT * 0 - Button.VERTICAL_PADDING * 0, getTop() + PANEL_TITLE_HEIGHT_1 + 4, Button.DEFAULT_HEIGHT, Button.DEFAULT_HEIGHT).build());
//
//        this.historyButton = addWidget(Button.builder(getEntrance(), Icons.HISTORY, button -> {
//            new EffortlessClipboardHistoryScreen(getEntrance(), snapshot -> {
//                this.clipboard = this.clipboard.withSnapshot(snapshot);
//            }).attach();
//        }).setBounds(getRight() - Button.DEFAULT_HEIGHT - PADDINGS_H - Button.DEFAULT_HEIGHT * 1 - Button.VERTICAL_PADDING * 1, getTop() + PANEL_TITLE_HEIGHT_1 + 4, Button.DEFAULT_HEIGHT, Button.DEFAULT_HEIGHT).build());
//
//        this.playerSharingButton = addWidget(Button.builder(getEntrance(), Icons.PLAYERS, button -> {
//            new EffortlessClipboardHistoryScreen(getEntrance(), snapshot -> {
//                this.clipboard = this.clipboard.withSnapshot(snapshot);
//            }).attach();
//        }).setBounds(getRight() - Button.DEFAULT_HEIGHT - PADDINGS_H - Button.DEFAULT_HEIGHT * 2 - Button.VERTICAL_PADDING * 2, getTop() + PANEL_TITLE_HEIGHT_1 + 4, Button.DEFAULT_HEIGHT, Button.DEFAULT_HEIGHT).build());

        this.snapshotWidget = addWidget(new StructureSnapshotWidget(getEntrance(), getLeft() + PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - PADDINGS_H * 2, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_2, clipboard.snapshot()));
        this.textWidget = addWidget(new MessageTextWidget(getEntrance(), getLeft() + AbstractPanelScreen.PADDINGS_H, getTop() + PANEL_TITLE_HEIGHT_1, getWidth() - AbstractPanelScreen.PADDINGS_H * 2, getHeight() - PANEL_TITLE_HEIGHT_1 - PANEL_BUTTON_ROW_HEIGHT_2, Text.translate("effortless.clipboard.no_structure"), MessageTextWidget.Gravity.CENTER));

//        addWidget(Button.builder(getEntrance(), Text.text("Edit"), button -> {
//        }).setBounds(this.snapshotWidget.getRight() - Button.DEFAULT_HEIGHT - 2, this.snapshotWidget.getTop() + 2 * 1 + Button.DEFAULT_HEIGHT * 0, Button.DEFAULT_HEIGHT, Button.DEFAULT_HEIGHT).build());
//
//        addWidget(Button.builder(getEntrance(), Text.text("Clear"), button -> {
//        }).setBounds(this.snapshotWidget.getRight() - Button.DEFAULT_HEIGHT - 2, this.snapshotWidget.getTop() + 2 * 2 + Button.DEFAULT_HEIGHT * 1, Button.DEFAULT_HEIGHT, Button.DEFAULT_HEIGHT).build());
//
//        addWidget(Button.builder(getEntrance(), Text.text("Share"), button -> {
//        }).setBounds(this.snapshotWidget.getRight() - Button.DEFAULT_HEIGHT - 2, this.snapshotWidget.getTop() + 2 * 3 + Button.DEFAULT_HEIGHT * 2, Button.DEFAULT_HEIGHT, Button.DEFAULT_HEIGHT).build());
//
//        addWidget(Button.builder(getEntrance(), Text.text("Fav"), button -> {
//        }).setBounds(this.snapshotWidget.getRight() - Button.DEFAULT_HEIGHT - 2, this.snapshotWidget.getTop() + 2 * 4 + Button.DEFAULT_HEIGHT * 3, Button.DEFAULT_HEIGHT, Button.DEFAULT_HEIGHT).build());


        this.editButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.edit"), button -> {

        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 3 / 4f, 1 / 4f).build());

        this.shareButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.share"), button -> {
            new EffortlessOnlinePlayersScreen(getEntrance(), playerInfo -> {
                getEntrance().getChannel().sendPacket(new PlayerSnapshotSharePacket(getPlayer().getId(), playerInfo.getId(), clipboard.snapshot()));
                detach();
            }).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 1 / 4f, 1 / 4f).build());

        this.clearButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.clear"), button -> {
            this.clipboard = this.clipboard.withSnapshot(Snapshot.EMPTY);

        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 2 / 4f, 1 / 4f).build());

        this.historyButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.history"), button -> {
            new EffortlessStructureSnapshotsHistoryScreen(getEntrance(), snapshot -> {
                this.clipboard = this.clipboard.withSnapshot(snapshot);
            }).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0 / 4f, 1 / 4f).build());

//        this.libraryButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.library"), button -> {
//            new EffortlessStructureSnapshotsLibraryScreen(getEntrance(), snapshot -> {
//                this.clipboard = this.clipboard.withSnapshot(snapshot);
//            }).attach();
//        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 1 / 4f, 1 / 4f).build());

        this.doneButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detachAll();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 2 / 4f).build());

        this.enableButton = addWidget(Button.builder(getEntrance(), clipboard.enabled() ? Text.translate("effortless.clipboard.button.disable") : Text.translate("effortless.clipboard.button.enable"), button -> {
            this.clipboard = clipboard.withEnabled(!clipboard.enabled());
            recreate();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 2 / 4f, 2 / 4f).build());


    }

    @Override
    public void onReload() {
        this.snapshotWidget.setSnapshot(this.clipboard.snapshot());
        this.consumer.accept(this.clipboard);
        this.textWidget.setVisible(clipboard.isEmpty());

        this.historyButton.setTooltip(TooltipHelper.makeSummary(getTypeface(), Text.translate("effortless.button.history"), Text.translate("effortless.clipboard.history.tooltip")));

//        this.libraryButton.setTooltip(TooltipHelper.makeSummary(getTypeface(), Text.translate("effortless.button.library"), Text.translate("effortless.clipboard.library.tooltip")));

        this.shareButton.setTooltip(TooltipHelper.makeSummary(getTypeface(), Text.translate("effortless.button.share"), Text.translate("effortless.clipboard.share.tooltip")));
        this.shareButton.setActive(!clipboard.isEmpty());

        this.clearButton.setTooltip(TooltipHelper.makeSummary(getTypeface(), Text.translate("effortless.button.clear"), Text.translate("effortless.clipboard.clear.tooltip")));
        this.clearButton.setActive(!clipboard.isEmpty());

        this.editButton.setTooltip(TooltipHelper.makeSummary(getTypeface(), Text.translate("effortless.button.edit"), Text.translate("effortless.clipboard.edit.tooltip")));
        this.editButton.setActive(!clipboard.isEmpty());

        this.enableButton.setTooltip(TooltipHelper.makeSummary(getTypeface(), clipboard.enabled() ? Text.translate("effortless.clipboard.button.disable") : Text.translate("effortless.clipboard.button.enable"), Text.translate("effortless.clipboard.enable_clipboard.tooltip", OptionKeys.KEY_ATTACK.getKeyBinding().getKey().getNameText(), OptionKeys.KEY_USE.getKeyBinding().getKey().getNameText())));


    }

}
