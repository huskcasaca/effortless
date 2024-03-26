package dev.huskuraft.effortless.screen.pattern;

import java.util.function.Consumer;

import dev.huskuraft.effortless.EffortlessClient;
import dev.huskuraft.effortless.api.gui.AbstractScreen;
import dev.huskuraft.effortless.api.gui.Dimens;
import dev.huskuraft.effortless.api.gui.button.Button;
import dev.huskuraft.effortless.api.gui.text.TextWidget;
import dev.huskuraft.effortless.api.platform.Entrance;
import dev.huskuraft.effortless.api.text.Text;
import dev.huskuraft.effortless.building.config.PatternSettings;
import dev.huskuraft.effortless.building.config.RootSettings;
import dev.huskuraft.effortless.building.pattern.Pattern;

public class EffortlessPatternSettingsScreen extends AbstractScreen {

    private final Consumer<PatternSettings> consumer;
    private PatternSettings config;
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
        super(entrance, Text.translate("effortless.pattern.settings.title"));
        this.consumer = pattern -> {
            getEntrance().getStructureBuilder().setPattern(getEntrance().getClient().getPlayer(), Pattern.DISABLED);
            getEntrance().getTagConfigStorage().update(config -> new RootSettings(config.renderSettings(), this.config, config.transformerPresets()));

        };
        this.config = getEntrance().getTagConfigStorage().get().patternSettings();
    }

    @Override
    protected EffortlessClient getEntrance() {
        return (EffortlessClient) super.getEntrance();
    }

    @Override
    public void onCreate() {
        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, Dimens.Title.CONTAINER_36 - 12, getScreenTitle(), TextWidget.Gravity.CENTER));

        this.entries = addWidget(new PatternList(getEntrance(), 0, Dimens.Title.CONTAINER_36, getWidth(), getHeight() - Dimens.Title.CONTAINER_36 - 60));
        this.entries.reset(config.patterns());

        this.upButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.up"), button -> {
            if (entries.hasSelected()) {
                entries.moveUpSelected();
            }
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0f, 0.25f).build());
        this.downButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.edit.down"), button -> {
            if (entries.hasSelected()) {
                entries.moveDownSelected();
            }
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.25f, 0.25f).build());

        this.editButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern.settings.edit"), button -> {
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
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0f, 0.25f).build());

        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern.settings.delete"), button -> {
            if (entries.hasSelected()) {
                entries.deleteSelected();
            }
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.25f, 0.25f).build());

        this.duplicateButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern.settings.duplicate"), button -> {
            if (entries.hasSelected()) {
                entries.insertSelected(entries.getSelected().getItem().withRandomId());
            }
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.5f, 0.25f).build());

        this.newButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern.settings.new"), button -> {
            new EffortlessPatternEditScreen(
                    getEntrance(),
                    pattern -> {
                        entries.insertSelected(pattern);
                        onReload();
                    },
                    Pattern.DEFAULT,
                    entries.indexOfSelected() == -1 ? entries.children().size() : entries.indexOfSelected() + 1
            ).attach();
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.75f, 0.25f).build());

        this.resetButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern.settings.reset"), button -> {
            entries.reset(Pattern.getDefaultPatterns());
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.5f, 0.5f).build());

        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern.settings.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.doneButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern.settings.done"), button -> {
            consumer.accept(config);
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());
    }

    @Override
    public void onReload() {
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

        this.config = new PatternSettings(
                entries.items()
        );
    }

}
