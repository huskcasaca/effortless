package dev.huskuraft.effortless.screen.randomizer;

import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.core.Orientation;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.gui.button.MoveButton;
import dev.huskuraft.effortless.gui.container.EditableEntry;
import dev.huskuraft.effortless.gui.icon.TextIcon;
import dev.huskuraft.effortless.gui.slot.ItemSlot;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.util.ArrayList;
import java.util.List;

public class ItemRandomizerEntry extends EditableEntry<ItemRandomizer> {

    private static final int ROW_WIDTH = Dimens.RegularEntry.ROW_WIDTH;
    private static final int MAX_SLOT_COUNT = Dimens.RegularEntry.MAX_SLOT_COUNT;


    private MoveButton button;
    private TextWidget nameTextWidget;
    //            private RadialTextIcon radialTextIcon;
    private TextIcon textIcon;
    private List<ItemSlot> slots;

    public ItemRandomizerEntry(Entrance entrance, EntryList entryList, ItemRandomizer randomizer) {
        super(entrance, entryList, randomizer);
    }

    @Override
    public void onCreate() {

        this.nameTextWidget = addWidget(new TextWidget(getEntrance(), getX() + Dimens.ICON_WIDTH + 3, getY() + 2, getDisplayName(getItem())));
        this.textIcon = addWidget(new TextIcon(getEntrance(), getX() + 1, getY() + 1, Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, Text.text("IR")));
        this.button = addWidget(new MoveButton(getEntrance(), getX(), getX(), direction -> {
            switch (direction) {
                case UP -> getEntryList().moveUpNoClamp(this);
                case DOWN -> getEntryList().moveDownNoClamp(this);
            }
        }, Orientation.UP, Orientation.DOWN));
        this.slots = new ArrayList<>();
        var index = 0;
        for (var holder : getItem().getChances()) {
            this.slots.add(addWidget(new ItemSlot(getEntrance(), getX() + index * Dimens.SLOT_OFFSET_X + Dimens.ICON_WIDTH + 3, getY() + 13, Dimens.SLOT_WIDTH, Dimens.SLOT_HEIGHT, holder.content(), Text.text(String.valueOf(holder.chance())))));
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
        return Dimens.ICON_HEIGHT + 6;
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
