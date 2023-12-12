package dev.huskuraft.effortless.screen.pattern;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractScreen;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.button.Button;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.icon.RadialTextIcon;
import dev.huskuraft.effortless.gui.input.EditBox;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.screen.transformer.EffortlessRandomizerEditScreen;
import dev.huskuraft.effortless.screen.transformer.EffortlessTransformerEditScreen;
import dev.huskuraft.effortless.screen.transformer.EffortlessTransformerTemplateSelectScreen;
import dev.huskuraft.effortless.screen.transformer.TransformerList;
import dev.huskuraft.effortless.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class EffortlessPatternEditScreen extends AbstractScreen {

    private final Consumer<Pattern> applySettings;
    private final Pattern defaultSettings;
    private final int index;
    private Pattern lastSettings;
    private TextWidget titleTextWidget;
    private EditBox nameEditBox;
    private TransformerList entries;
    private Button upButton;
    private Button downButton;
    private Button editButton;
    private Button deleteButton;
    private Button duplicateButton;
    private Button addButton;
    private Button saveButton;
    private Button cancelButton;
    private RadialTextIcon radialTextIcon;

    public EffortlessPatternEditScreen(Entrance entrance, Consumer<Pattern> consumer, Pattern pattern, int index) {
        super(entrance, Text.translate("effortless.pattern.edit.title"));
        this.applySettings = consumer;
        this.defaultSettings = pattern;
        this.lastSettings = pattern;
        this.index = index;
    }

    public static List<Text> getTransformerEntryTooltip(Transformer transformer) {
        return List.of();
    }

    @Override
    public void onCreate() {
        this.radialTextIcon = addWidget(new RadialTextIcon(getEntrance(), getWidth() / 2 - (Dimens.RegularEntry.ROW_WIDTH) / 2, 16 + 2, Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, index, Text.text(String.valueOf(index + 1))));

        this.nameEditBox = addWidget(
                new EditBox(getEntrance(), getWidth() / 2 - (Dimens.RegularEntry.ROW_WIDTH) / 2 + 40, 24, Dimens.RegularEntry.ROW_WIDTH - 40, 20, null)
        );
        this.nameEditBox.setMaxLength(Pattern.MAX_NAME_LENGTH);
        this.nameEditBox.setHint(Text.translate("effortless.pattern.edit.name_hint"));
        this.nameEditBox.setValue(lastSettings.name().getString());

        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, 24 - 16, getScreenTitle(), TextWidget.Gravity.CENTER));

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

        this.editButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern.edit.edit"), button -> {
            if (entries.hasSelected()) {
                var item = entries.getSelected().getItem();
                switch (item.getType()) {
                    case ARRAY, MIRROR, RADIAL -> {
                        new EffortlessTransformerEditScreen(
                                getEntrance(),
                                transformer -> {
                                    entries.replaceSelect(transformer);
                                    onReload();
                                },
                                item
                        ).attach();
                    }
                    case ITEM_RAND -> {
                        new EffortlessRandomizerEditScreen(
                                getEntrance(),
                                transformer -> {
                                    entries.replaceSelect(transformer);
                                    onReload();
                                },
                                (ItemRandomizer) item
                        ).attach();
                    }
                }

            }
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0f, 0.25f).build());

        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.randomizer.settings.delete"), button -> {
            if (entries.hasSelected()) {
                entries.deleteSelected();
            }
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.25f, 0.25f).build());

        this.duplicateButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern.edit.duplicate"), button -> {
            if (entries.hasSelected()) {
                entries.insertSelected(entries.getSelected().getItem());
            }
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.5f, 0.25f).build());

        this.addButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern.edit.add"), button -> {
            new EffortlessTransformerTemplateSelectScreen(
                    getEntrance(),
                    transformer -> {
                        entries.insertSelected(transformer);
                        onReload();
                    },
                    getEntrance().getContentCreator().getDefaultTransformers()
            ).attach();
        }).setBoundsGrid(getWidth(), getHeight(), 1f, 0.75f, 0.25f).build());

        this.saveButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern.edit.save"), button -> {
            applySettings.accept(lastSettings);
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0f, 0.5f).build());
        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("effortless.pattern.edit.cancel"), button -> {
            detach();
        }).setBoundsGrid(getWidth(), getHeight(), 0f, 0.5f, 0.5f).build());

        this.entries = addWidget(new TransformerList(getEntrance(), 0, 54, getWidth(), getHeight() - 54 - 60));
        this.entries.reset(lastSettings.transformers());
    }

    @Override
    public void onReload() {
        upButton.setActive(entries.hasSelected() && entries.indexOfSelected() > 0);
        downButton.setActive(entries.hasSelected() && entries.indexOfSelected() < entries.children().size() - 1);
        editButton.setActive(entries.hasSelected());
        deleteButton.setActive(entries.hasSelected());
        duplicateButton.setActive(entries.hasSelected());
        saveButton.setActive(isContentValid());

        upButton.setVisible(getEntrance().getClient().hasAltDown());
        downButton.setVisible(getEntrance().getClient().hasAltDown());
        editButton.setVisible(!getEntrance().getClient().hasAltDown());
        deleteButton.setVisible(!getEntrance().getClient().hasAltDown());

        lastSettings = new Pattern(lastSettings.id(), Text.text(nameEditBox.getValue()), entries.items());
    }

    private boolean isContentValid() {
        return !entries.children().isEmpty() && entries.children().stream().map(EditableEntry::getItem).allMatch(Transformer::isValid);
    }

}
