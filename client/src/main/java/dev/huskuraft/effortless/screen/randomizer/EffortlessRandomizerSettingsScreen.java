package dev.huskuraft.effortless.screen.randomizer;

import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.settings.RandomizerSettings;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.Orientation;
import dev.huskuraft.effortless.gui.AbstractScreen;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.button.Button;
import dev.huskuraft.effortless.gui.button.MoveButton;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.container.EditableEntryList;
import dev.huskuraft.effortless.gui.icon.TextIcon;
import dev.huskuraft.effortless.gui.slot.ItemSlot;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EffortlessRandomizerSettingsScreen extends AbstractScreen {

    private final Consumer<RandomizerSettings> applySettings;
    private RandomizerSettings lastSettings;
    private TextWidget titleTextWidget;
    private RandomizerList entries;
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
        this.entries = addWidget(new RandomizerList(getEntrance(), 0, 32, getWidth(), getHeight() - 32 - 60));
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

    public static final class RandomizerList extends EditableEntryList<ItemRandomizer> {

        private static final int ROW_WIDTH = Dimens.RegularEntry.ROW_WIDTH;
        private static final int MAX_SLOT_COUNT = Dimens.RegularEntry.MAX_SLOT_COUNT;

        public RandomizerList(Entrance entrance, int x, int y, int width, int height) {
            super(entrance, x, y, width, height);
        }

        @Override
        protected EditableEntry<ItemRandomizer> createHolder(ItemRandomizer item) {
            return new ItemRandomizerEntry(getEntrance(), item);
        }

        private class ItemRandomizerEntry extends EditableEntry<ItemRandomizer> {

            private MoveButton button;
            private TextWidget nameTextWidget;
            //            private RadialTextIcon radialTextIcon;
            private TextIcon textIcon;
            private List<ItemSlot> slots;

            public ItemRandomizerEntry(Entrance entrance, ItemRandomizer randomizer) {
                super(entrance, randomizer);
            }

            @Override
            public void onCreate() {

                this.nameTextWidget = addWidget(new TextWidget(getEntrance(), getX() + 2 + 32 + 1, getY() + 2, getDisplayName(getItem())));
//                radialTextIcon = addWidget(new RadialTextIcon(minecraft, getLeft(), getTop(), Dimen.ICON_WIDTH, Dimen.ICON_HEIGHT, indexOf(this), String.valueOf(indexOf(this))));
                this.textIcon = addWidget(new TextIcon(getEntrance(), getX(), getY(), Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, Text.text("R")));
                this.button = addWidget(new MoveButton(getEntrance(), getX(), getX(), direction -> {
                    switch (direction) {
                        case UP -> {
                            var index = RandomizerList.this.indexOf(this);
                            if (index > 0) {
                                RandomizerList.this.moveUp(RandomizerList.this.indexOf(this));
                                RandomizerList.this.setScrollAmountNoClamp(getScrollAmount() - RandomizerList.this.getOrThrow(index).getHeight());
                            }
                        }
                        case DOWN -> {
                            var index = RandomizerList.this.indexOf(this);
                            if (index < RandomizerList.this.children().size() - 1) {
                                RandomizerList.this.moveDown(RandomizerList.this.indexOf(this));
                                RandomizerList.this.setScrollAmountNoClamp(getScrollAmount() + RandomizerList.this.getOrThrow(index).getHeight());
                            }
                        }
                        default -> {
                        }
                    }
                }, Orientation.UP, Orientation.DOWN));
                this.slots = new ArrayList<>();
                var index = 0;
                for (var holder : getItem().getChances()) {
                    this.slots.add(addWidget(new ItemSlot(getEntrance(), getX() + index * Dimens.SLOT_OFFSET_X + Dimens.ICON_WIDTH + 1, getY() + 10 + 1, Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, holder.content(), Text.text(String.valueOf(holder.chance())))));
                    if (index++ == MAX_SLOT_COUNT) {
                        break;
                    }
                }
            }

            @Override
            public void onReload() {
//                var index = RandomizerList.this.indexOf(this);
//                radialTextIcon.setIndex(index);
//                radialTextIcon.setName(String.valueOf(index));
            }

            @Override
            public Text getNarration() {
                return Text.translate("narrator.select", getDisplayName(getItem()));
            }

            @Override
            public int getWidth() {
                return Dimens.RegularEntry.ROW_WIDTH;
            }

            @Override
            public int getHeight() {
                return 24 + 12;
            }

            private Text getSymbol(ItemRandomizer randomizer) {
                if (randomizer.getName().isBlank()) {
                    return Text.empty();
                }
                return Text.text(randomizer.getName().substring(0, 1));
            }

            private Text getDisplayName(ItemRandomizer randomizer) {
                if (randomizer.getName().isBlank()) {
                    return Text.text("No Name").withStyle(TextStyle.GRAY, TextStyle.ITALIC);
                }
                return Text.text(randomizer.getName());
            }

        }

    }
}
