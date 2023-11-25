package dev.huskuraft.effortless.screen.pattern;

import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.settings.PatternSettings;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractScreen;
import dev.huskuraft.effortless.gui.AbstractWidget;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.button.Button;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.container.EditableEntryList;
import dev.huskuraft.effortless.gui.icon.RadialTextIcon;
import dev.huskuraft.effortless.gui.icon.TextIcon;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.screen.transformer.info.TransformerInfoEntry;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EffortlessPatternSettingsScreen extends AbstractScreen {

    private final Consumer<PatternSettings> applySettings;
    private PatternSettings lastSettings;
    private TextWidget titleTextWidget;
    private PatternList entries;
    private Button editButton;
    private Button deleteButton;
    private Button duplicateButton;
    private Button newButton;
    private Button resetButton;
    private Button doneButton;
    private Button cancelButton;

    public EffortlessPatternSettingsScreen(Entrance entrance, Consumer<PatternSettings> consumer, PatternSettings patternSettings) {
        super(entrance, Text.translate("pattern.settings.title"));
        this.applySettings = consumer;
        this.lastSettings = patternSettings;
    }

    private void updateSettings() {
        lastSettings = new PatternSettings(
                entries.items()
        );
    }

    @Override
    public void onCreate() {
        this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getWidth() / 2, 35 - 16, getScreenTitle(), TextWidget.Gravity.CENTER));

        this.editButton = addWidget(Button.builder(getEntrance(), Text.translate("pattern.settings.edit"), button -> {
            if (entries.hasSelected()) {
                new EffortlessPatternEditScreen(
                        getEntrance(),
                        pattern -> {
                            entries.replaceSelect(pattern);
                            updateSettings();
                        },
                        entries.getSelected().getItem(),
                        entries.indexOfSelected()
                ).attach();
            }
        }).bounds(getWidth() / 2 - 154, getHeight() - 52, 72, 20).build());

        this.deleteButton = addWidget(Button.builder(getEntrance(), Text.translate("pattern.settings.delete"), button -> {
            if (entries.hasSelected()) {
                entries.deleteSelected();
                updateSettings();
            }
        }).bounds(getWidth() / 2 - 76, getHeight() - 52, 72, 20).build());

        this.duplicateButton = addWidget(Button.builder(getEntrance(), Text.translate("pattern.settings.duplicate"), button -> {
            if (entries.hasSelected()) {
                entries.insertSelected(entries.getSelected().getItem());
                updateSettings();
            }
        }).bounds(getWidth() / 2 + 4, getHeight() - 52, 72, 20).build());

        this.newButton = addWidget(Button.builder(getEntrance(), Text.translate("pattern.settings.new"), button -> {
            new EffortlessPatternEditScreen(
                    getEntrance(),
                    pattern -> {
                        entries.insertSelected(pattern);
                        updateSettings();
                    },
                    Pattern.DEFAULT,
                    entries.indexOfSelected() == -1 ? entries.children().size() : entries.indexOfSelected() + 1
            ).attach();
        }).bounds(getWidth() / 2 + 82, getHeight() - 52, 72, 20).build());

        this.resetButton = addWidget(Button.builder(getEntrance(), Text.translate("pattern.settings.reset"), button -> {
            entries.reset(getEntrance().getContentCreator().getDefaultPatterns());
        }).bounds(getWidth() / 2 - 154, getHeight() - 52, 308, 20).build());

        this.doneButton = addWidget(Button.builder(getEntrance(), Text.translate("pattern.settings.done"), button -> {
            updateSettings();
            applySettings.accept(lastSettings);
            detach();
        }).bounds(getWidth() / 2 - 154, getHeight() - 28, 150, 20).build());
        this.cancelButton = addWidget(Button.builder(getEntrance(), Text.translate("pattern.settings.cancel"), button -> {
            detach();
        }).bounds(getWidth() / 2 + 4, getHeight() - 28, 150, 20).build());
        this.entries = addWidget(new PatternList(getEntrance(), 0, 32, getWidth(), getHeight() - 32 - 60));
        this.entries.reset(lastSettings.patterns());
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

    private static final class PatternList extends EditableEntryList<Pattern> {

        private static final int ROW_WIDTH = Dimens.RegularEntry.ROW_WIDTH;
        private static final int MAX_SLOT_COUNT = Dimens.RegularEntry.MAX_SLOT_COUNT;

        public PatternList(Entrance entrance, int x, int y, int width, int height) {
            super(entrance, x, y, width, height);
        }

        @Override
        protected EditableEntry<Pattern> createHolder(Pattern item) {
            return new PatternEntry(getEntrance(), item);
        }

        private class PatternEntry extends EditableEntry<Pattern> {

            private RadialTextIcon radialTextIcon;
            private TextWidget nameTextWidget;
            private List<AbstractWidget> slots;

            protected PatternEntry(Entrance entrance, Pattern pattern) {
                super(entrance, pattern);
            }

            @Override
            public void onCreate() {
                this.radialTextIcon = addWidget(new RadialTextIcon(getEntrance(), getX(), getY(), Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, 0, Text.text(String.valueOf(0))));
                this.nameTextWidget = addWidget(new TextWidget(getEntrance(), getX() + 2 + 32 + 1, getY() + 2, getDisplayName(getItem())));
                updateSlots();
            }

            @Override
            public void onPositionChange(int from, int to) {
                radialTextIcon.setIndex(to);
                radialTextIcon.setMessage(Text.text(String.valueOf(to + 1)));
            }

            @Override
            public void setItem(Pattern item) {
                super.setItem(item);
                nameTextWidget.setMessage(getDisplayName(getItem()));
            }

            @Override
            public Text getNarration() {
                return Text.translate("narrator.select", Text.empty());
            }

            @Override
            public int getWidth() {
                return Dimens.RegularEntry.ROW_WIDTH;
            }

            @Override
            public int getHeight() {
                return 24 + 12;
            }

            private void updateSlots() {
                var slots = new ArrayList<AbstractWidget>();
                var slot = 0;
                for (var transformer : getItem().transformers()) {
                    slots.add(addWidget(new TextIcon(getEntrance(), getX() + slot * Dimens.SLOT_OFFSET_X + Dimens.ICON_WIDTH + 2, getY() + 10 + 4, 18, 18, TransformerInfoEntry.getSymbol(transformer))));
                    if (slot++ == MAX_SLOT_COUNT) {
                        break;
                    }
                }
                this.slots = slots;
            }

            private Text getDisplayName(Pattern pattern) {
                if (pattern.name().getString().isBlank()) {
                    return Text.text("No Name").withStyle(TextStyle.GRAY, TextStyle.ITALIC);
                }
                return pattern.name();
            }


        }

    }
}
