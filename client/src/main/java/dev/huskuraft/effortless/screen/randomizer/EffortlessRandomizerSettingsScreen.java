package dev.huskuraft.effortless.screen.randomizer;

import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.settings.RandomizerSettings;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractScreen;
import dev.huskuraft.effortless.gui.button.Button;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.text.Text;

import java.util.function.Consumer;

public class EffortlessRandomizerSettingsScreen extends AbstractScreen {

    private final Consumer<RandomizerSettings> applySettings;
    private RandomizerSettings lastSettings;
    private TextWidget titleTextWidget;
    private ItemRandomizerList entries;
    private Button editButton;
    private Button deleteButton;
    private Button duplicateButton;
    private Button newButton;
    private Button resetButton;
    private Button doneButton;
    private Button cancelButton;

    public EffortlessRandomizerSettingsScreen(Entrance entrance, Consumer<RandomizerSettings> consumer, RandomizerSettings randomizerSettings) {
        super(entrance, Text.translate("effortless.randomizer.settings.title"));
        this.applySettings = consumer;
        this.lastSettings = randomizerSettings;
    }

    private void updateSettings() {
        lastSettings = new RandomizerSettings(
                entries.items()
        );
    }

    @Override
    public void onCreate() {
        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, 35 - 16, getScreenTitle(), TextWidget.Gravity.CENTER));

        this.editButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.settings.edit"), button -> {
            if (entries.hasSelected()) {
                new EffortlessRandomizerEditScreen(
                        getEntrance(),
                        randomizer -> {
                            entries.replaceSelect(randomizer);
                            updateSettings();
                        },
                        entries.getSelected().getItem()
                ).attach();
            }
        }).bounds(getWidth() / 2 - 154, getHeight() - 52, 72, 20).build());

        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.settings.delete"), button -> {
            if (entries.hasSelected()) {
                entries.deleteSelected();
                updateSettings();
            }
        }).bounds(getWidth() / 2 - 76, getHeight() - 52, 72, 20).build());

        this.duplicateButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.settings.duplicate"), button -> {
            if (entries.hasSelected()) {
                entries.insertSelected(entries.getSelected().getItem());
                updateSettings();
            }
        }).bounds(getWidth() / 2 + 4, getHeight() - 52, 72, 20).build());

        this.newButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.settings.new"), button -> {
            new EffortlessRandomizerEditScreen(
                    getEntrance(),
                    randomizer -> {
                        entries.insertSelected(randomizer);
                        updateSettings();
                    },
                    ItemRandomizer.EMPTY.rename("")
            ).attach();
        }).bounds(getWidth() / 2 + 82, getHeight() - 52, 72, 20).build());

        this.resetButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.settings.reset"), button -> {
            entries.reset(getEntrance().getContentCreator().getDefaultRandomizers());
        }).bounds(getWidth() / 2 - 154, getHeight() - 52, 308, 20).build());

        this.doneButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.settings.done"), button -> {
            updateSettings();
            applySettings.accept(lastSettings);
            detach();
        }).bounds(getWidth() / 2 - 154, getHeight() - 28, 150, 20).build());
        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.settings.cancel"), button -> {
            detach();
        }).bounds(getWidth() / 2 + 4, getHeight() - 28, 150, 20).build());
        this.entries = addWidget(new ItemRandomizerList(getEntrance(), 0, 32, getWidth(), getHeight() - 32 - 60));
        this.entries.reset(lastSettings.randomizers());
    }

    @Override
    public void onReload() {
        editButton.setActive(entries.hasSelected());
        deleteButton.setActive(entries.hasSelected());
        duplicateButton.setActive(entries.hasSelected());

        editButton.setVisible(!getEntrance().getClient().hasAltDown());
        deleteButton.setVisible(!getEntrance().getClient().hasAltDown());
        duplicateButton.setVisible(!getEntrance().getClient().hasAltDown());
        newButton.setVisible(!getEntrance().getClient().hasAltDown());
        resetButton.setVisible(getEntrance().getClient().hasAltDown());
    }

}
