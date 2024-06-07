package dev.huskuraft.effortless.screen.clipboard;

import java.util.function.Consumer;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.AbstractWidget;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.slot.TextSlot;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.gui.tooltip.TooltipHelper;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.clipboard.Clipboard;
import dev.huskuraft.effortless.screen.player.EffortlessOnlinePlayersScreen;
import dev.huskuraft.effortless.screen.settings.SettingOptionsList;

public class EffortlessClipboardScreen extends AbstractPanelScreen {

    private final Consumer<Clipboard> consumer;
    private Clipboard clipboard;
    private AbstractWidget titleTextWidget;
    private SettingOptionsList entries;
    private Button moveToPlayerButton;
    private Button moveToCornerOrCenterButton;
    private Button cancelButton;
    private TextSlot textSlot;
    private Button enableButton;
    private Button doneButton;
    private Button shareButton;
    private Button historyButton;
    private Button exportButton;
    private Button importButton;
//    private EditBox nameEditBox;

    public EffortlessClipboardScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.clipboard.edit.title"), PANEL_WIDTH_EXPANDED, PANEL_HEIGHT_3_4);
        this.consumer = clipboard -> {
            getEntrance().getStructureBuilder().setClipboard(getEntrance().getClient().getPlayer(), this.clipboard);
        };
        this.clipboard = getEntrance().getStructureBuilder().getContext(getEntrance().getClient().getPlayer()).clipboard();
    }
    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
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

        this.shareButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.share"), button -> {
            new EffortlessOnlinePlayersScreen(getEntrance(), playerInfo -> {
                clipboard.snapshot();

            }).attach();
            detachAll();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 1/4f).build());

        this.historyButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.history"), button -> {
            detachAll();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 1/4f, 1/4f).build());
        this.exportButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.export"), button -> {
            detachAll();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 2/4f, 1/4f).build());

        this.importButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.import"), button -> {
            detachAll();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 3/4f, 1/4f).build());


        this.doneButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.done"), button -> {
            detachAll();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 1f).build());

    }

    @Override
    public void onReload() {
        this.consumer.accept(this.clipboard);
        this.shareButton.setTooltip(
                TooltipHelper.wrapLines(getTypeface(), Text.translate("Share this structure with an online player on this sever."))
        );
        this.historyButton.setTooltip(
                TooltipHelper.wrapLines(getTypeface(), Text.translate("Pick a structure template from your build/copy history."))
        );
        this.exportButton.setTooltip(
                TooltipHelper.wrapLines(getTypeface(), Text.translate("Export this structure to your structure collections or system clipboard."))
        );
        this.importButton.setTooltip(
                TooltipHelper.wrapLines(getTypeface(), Text.translate("Import a structure from your structure collections or system clipboard."))
        );
//        this.exportButton.setTooltip(
//                TooltipHelper.wrapLines(getTypeface(), Text.translate("Export this structure to your system clipboard. You can paste this structure text to share with anyone else."))
//        );
//        this.importButton.setTooltip(
//                TooltipHelper.wrapLines(getTypeface(), Text.translate("Import a structure from your system clipboard. You can copy a structure text and import here."))
//        );
    }

}
