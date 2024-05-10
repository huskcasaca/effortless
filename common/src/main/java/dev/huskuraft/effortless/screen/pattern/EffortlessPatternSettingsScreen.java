package dev.huskuraft.effortless.screen.pattern;

import java.util.List;
import java.util.function.Consumer;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractPanelScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.ChatFormatting;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.config.ClientConfig;
import dev.huskuraft.effortless.building.config.PatternConfig;
import dev.huskuraft.effortless.building.pattern.Pattern;

public class EffortlessPatternSettingsScreen extends AbstractPanelScreen {

    private final Consumer<PatternConfig> consumer;
    private PatternConfig config;
    private TextWidget titleTextWidget;
    private PatternList entries;
    private Button upButton;
    private Button downButton;
    private Button editButton;
    private Button deleteButton;
    private Button duplicateButton;
    private Button newButton;
    private Button resetButton;
    private Button doneButton;
    private Button cancelButton;

    public EffortlessPatternSettingsScreen(Entrance entrance) {
        super(entrance, Text.translate("effortless.pattern.settings.title"), Dimens.Screen.CONTAINER_WIDTH_NORMAL, Dimens.Screen.CONTAINER_HEIGHT_NORMAL);
        this.consumer = pattern -> {
            getEntrance().getStructureBuilder().setPattern(getEntrance().getClient().getPlayer(), Pattern.DISABLED);
            getEntrance().getConfigStorage().update(config -> new ClientConfig(config.renderConfig(), this.config, config.transformerPresets(), config.passiveMode()));

        };
        this.config = getEntrance().getConfigStorage().get().patternConfig();
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getLeft() + getWidth() / 2, getTop() + Dimens.Screen.TITLE_CONTAINER - 12, getScreenTitle().withStyle(ChatFormatting.DARK_GRAY), TextWidget.Gravity.CENTER));

        this.entries = addWidget(new PatternList(getEntrance(), getLeft() + 6, getTop() + 20, getWidth() - 20, getHeight() - Dimens.Screen.TITLE_CONTAINER - Dimens.Screen.BUTTON_CONTAINER_ROW_2));
        this.entries.reset(config.patterns());

        this.upButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.up"), button -> {
            if (entries.hasSelected()) {
                entries.moveUpSelected();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 0.25f).build());
        this.downButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.down"), button -> {
            if (entries.hasSelected()) {
                entries.moveDownSelected();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.25f, 0.25f).build());

        this.editButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.edit"), button -> {
            if (entries.hasSelected()) {
                new EffortlessPatternEditScreen(
                        getEntrance(),
                        pattern -> {
                            entries.replaceSelect(pattern);
                            onReload();
                        },
                        entries.getSelected().getItem(),
                        entries.indexOfSelected()
                ).attach();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0f, 0.25f).build());

        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.delete"), button -> {
            if (entries.hasSelected()) {
                entries.deleteSelected();
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.25f, 0.25f).build());

        this.duplicateButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.duplicate"), button -> {
            if (entries.hasSelected()) {
                entries.insertSelected(entries.getSelected().getItem().withRandomId());
            }
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.5f, 0.25f).build());

        this.newButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.new"), button -> {
            new EffortlessPatternEditScreen(
                    getEntrance(),
                    pattern -> {
                        entries.insertSelected(pattern);
                        onReload();
                    },
                    Pattern.DISABLED,
                    entries.indexOfSelected() == -1 ? entries.children().size() : entries.indexOfSelected() + 1
            ).attach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.75f, 0.25f).build());

        this.resetButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.reset"), button -> {
            entries.reset(List.of());
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 1f, 0.5f, 0.5f).build());

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.cancel"), button -> {
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.doneButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.button.save"), button -> {
            consumer.accept(config);
            detach();
        }).setBoundsGrid(getLeft(), getTop(), getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());
    }

    @Override
    public void onReload() {
        super.onReload();
        this.upButton.setActive(entries.hasSelected() && entries.indexOfSelected() > 0);
        this.downButton.setActive(entries.hasSelected() && entries.indexOfSelected() < entries.children().size() - 1);
        this.editButton.setActive(entries.hasSelected());
        this.deleteButton.setActive(entries.hasSelected());
        this.duplicateButton.setActive(entries.hasSelected());

        this.upButton.setVisible(getEntrance().getClient().getWindow().isAltDown());
        this.downButton.setVisible(getEntrance().getClient().getWindow().isAltDown());
        this.editButton.setVisible(!getEntrance().getClient().getWindow().isAltDown());
        this.deleteButton.setVisible(!getEntrance().getClient().getWindow().isAltDown());
        this.duplicateButton.setVisible(!getEntrance().getClient().getWindow().isAltDown());
        this.newButton.setVisible(!getEntrance().getClient().getWindow().isAltDown());
        this.resetButton.setVisible(getEntrance().getClient().getWindow().isAltDown());

        this.config = new PatternConfig(entries.items());
    }


}
