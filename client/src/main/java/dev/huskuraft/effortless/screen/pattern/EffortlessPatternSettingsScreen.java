package dev.huskuraft.effortless.screen.pattern;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.settings.PatternSettings;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractScreen;
import dev.huskuraft.effortless.gui.button.Button;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.text.Text;

import java.util.function.Consumer;

public class EffortlessPatternSettingsScreen extends AbstractScreen {

    private final Consumer<PatternSettings> applySettings;
    private PatternSettings lastSettings;
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

    public EffortlessPatternSettingsScreen(Entrance entrance, Consumer<PatternSettings> consumer, PatternSettings patternSettings) {
        super(entrance, Text.translate("effortless.pattern.settings.title"));
        this.applySettings = consumer;
        this.lastSettings = patternSettings;
    }

    @Override
    public void onCreate() {
        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, 35 - 16, getScreenTitle(), TextWidget.Gravity.CENTER));

        this.entries = addWidget(new PatternList(getEntrance(), 0, 32, getWidth(), getHeight() - 32 - 60));
        this.entries.reset(lastSettings.patterns());

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
            entries.reset(getEntrance().getContentCreator().getDefaultPatterns());
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.5f, 0.5f).build());

        this.doneButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern.settings.done"), button -> {
            applySettings.accept(lastSettings);
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern.settings.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());
    }

    @Override
    public void onReload() {
        upButton.setActive(entries.hasSelected() && entries.indexOfSelected() > 0);
        downButton.setActive(entries.hasSelected() && entries.indexOfSelected() < entries.children().size() - 1);
        editButton.setActive(entries.hasSelected());
        deleteButton.setActive(entries.hasSelected());
        duplicateButton.setActive(entries.hasSelected());

        upButton.setVisible(getEntrance().getClient().hasAltDown());
        downButton.setVisible(getEntrance().getClient().hasAltDown());
        editButton.setVisible(!getEntrance().getClient().hasAltDown());
        deleteButton.setVisible(!getEntrance().getClient().hasAltDown());
        duplicateButton.setVisible(!getEntrance().getClient().hasAltDown());
        newButton.setVisible(!getEntrance().getClient().hasAltDown());
        resetButton.setVisible(getEntrance().getClient().hasAltDown());

        lastSettings = new PatternSettings(
                entries.items()
        );
    }

}
