package dev.huskuraft.effortless.screen.transformer;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.core.Entrance;
import dev.huskuraft.effortless.gui.AbstractWidget;
import dev.huskuraft.effortless.gui.Dimens;
import dev.huskuraft.effortless.gui.EntryList;
import dev.huskuraft.effortless.gui.container.EditableEntryList;
import dev.huskuraft.effortless.gui.slot.SlotContainer;
import dev.huskuraft.effortless.gui.slot.SlotData;
import dev.huskuraft.effortless.gui.slot.TextSlot;
import dev.huskuraft.effortless.gui.text.TextWidget;
import dev.huskuraft.effortless.text.Text;
import dev.huskuraft.effortless.text.TextStyle;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

public final class TransformerList extends EditableEntryList<Transformer> {

    public TransformerList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getWidth() / 2 + 160;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected EditableEntryList.Entry<Transformer> createHolder(Transformer transformer) {
        return (EditableEntryList.Entry<Transformer>) switch (transformer.getType()) {
            case ARRAY -> new ArrayEntry(getEntrance(), this, (ArrayTransformer) transformer);
            case MIRROR -> new MirrorEntry(getEntrance(), this, (MirrorTransformer) transformer);
            case RADIAL -> new RadialEntry(getEntrance(), this, (RadialTransformer) transformer);
            case ITEM_RAND -> new ItemRandomizerEntry(getEntrance(), this, (ItemRandomizer) transformer);
        };
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    public abstract static class Entry<T extends Transformer> extends EditableEntryList.Entry<T> {

        protected TextSlot textSlot;
        protected AbstractWidget titleTextWidget;
        protected SlotContainer slotContainer;

        public Entry(Entrance entrance, EntryList entryList, T transformer) {
            super(entrance, entryList, transformer);
        }

        protected static String formatDouble(double number) {
            var decimalFormat = new DecimalFormat("#.#");
            return decimalFormat.format(number);
        }

        public static Text getSymbol(Transformer transformer) {
            return switch (transformer.getType()) {
                case ARRAY -> Text.text("AT");
                case MIRROR -> Text.text("MT");
                case RADIAL -> Text.text("RT");
                case ITEM_RAND -> Text.text("IR");
            };
        }

        @Override
        public void onCreate() {
            this.textSlot = addWidget(new TextSlot(getEntrance(), getX(), getY(), Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, getSymbol()));
            this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getX() + Dimens.ICON_WIDTH + 2, getY() + 2, getDisplayName()));
            this.slotContainer = addWidget(new SlotContainer(getEntrance(), getX() + Dimens.ICON_WIDTH + 2, getY() + 12, 0, 0));
        }

        @Override
        public void onReload() {
            slotContainer.setWrapLines(getEntryList().getSelected() == this);
        }

        @Override
        public void onBindItem() {
            textSlot.setMessage(getSymbol());
            slotContainer.setEntries(getData());
        }

        @Override
        public Text getNarration() {
            return Text.translate("narrator.select", getDisplayName());
        }

        protected abstract List<SlotData> getData();

        protected Text getSymbol() {
            return getSymbol(getItem());
        }

        @Override
        public int getWidth() {
            return Dimens.RegularEntry.ROW_WIDTH;
        }

        @Override
        public int getHeight() {
            return Dimens.ICON_HEIGHT + 4;
        }

        protected Text getDisplayName() {
            if (getItem().getName().isBlank()) {
                return Text.translate("effortless.transformer.no_name").withStyle(TextStyle.GRAY);
            }
            return getItem().getName();
        }
    }


    public static class ArrayEntry extends Entry<ArrayTransformer> {

        public ArrayEntry(Entrance entrance, EntryList entryList, ArrayTransformer arrayTransformer) {
            super(entrance, entryList, arrayTransformer);
        }

        @Override
        protected List<SlotData> getData() {
            return List.of(
                    new SlotData.TextSymbol(Text.text(formatDouble(getItem().offset().getX())), Text.translate("effortless.axis.x")),
                    new SlotData.TextSymbol(Text.text(formatDouble(getItem().offset().getY())), Text.translate("effortless.axis.y")),
                    new SlotData.TextSymbol(Text.text(formatDouble(getItem().offset().getZ())), Text.translate("effortless.axis.z")),
                    new SlotData.TextSymbol(Text.text(formatDouble(getItem().count())), Text.translate("effortless.transformer.array.count"))
            );
        }

    }

    public static class ItemRandomizerEntry extends Entry<ItemRandomizer> {

        public ItemRandomizerEntry(Entrance entrance, EntryList entryList, ItemRandomizer randomizer) {
            super(entrance, entryList, randomizer);
        }

        @Override
        protected List<SlotData> getData() {
            return getItem().getChances().stream().map(chance -> new SlotData.ItemStackSymbol(chance.content().getDefaultStack(), Text.text(String.valueOf(chance.chance())))).collect(Collectors.toList());
        }

    }

    public static class MirrorEntry extends Entry<MirrorTransformer> {

        public MirrorEntry(Entrance entrance, EntryList entryList, MirrorTransformer mirrorTransformer) {
            super(entrance, entryList, mirrorTransformer);
        }

        @Override
        protected List<SlotData> getData() {
            return List.of(
                    new SlotData.TextSymbol(Text.text(formatDouble(getItem().getPosition(getItem().axis()))), Text.text("D")),
                    new SlotData.TextSymbol(getItem().axis().getDisplayName(), Text.translate("effortless.transformer.mirreor.axis"))
            );
        }


    }

    public static class RadialEntry extends Entry<RadialTransformer> {

        public RadialEntry(Entrance entrance, EntryList entryList, RadialTransformer radialTransformer) {
            super(entrance, entryList, radialTransformer);
        }

        @Override
        protected List<SlotData> getData() {
            return List.of(
                    new SlotData.TextSymbol(Text.text(formatDouble(getItem().position().getX())), Axis.X.getDisplayName()),
                    new SlotData.TextSymbol(Text.text(formatDouble(getItem().position().getY())), Axis.Y.getDisplayName()),
                    new SlotData.TextSymbol(Text.text(formatDouble(getItem().position().getZ())), Axis.Z.getDisplayName()),
                    new SlotData.TextSymbol(Text.text(formatDouble(getItem().slices())), Text.translate("effortless.transformer.radial.slices"))
            );
        }

    }
}
