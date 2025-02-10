package dev.huskuraft.effortless.screen.transformer;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import dev.huskuraft.universal.api.core.Axis;
import dev.huskuraft.universal.api.gui.AbstractWidget;
import dev.huskuraft.universal.api.gui.Dimens;
import dev.huskuraft.universal.api.gui.EntryList;
import dev.huskuraft.universal.api.gui.container.EditableEntryList;
import dev.huskuraft.universal.api.gui.slot.SlotContainer;
import dev.huskuraft.universal.api.gui.slot.SlotData;
import dev.huskuraft.universal.api.gui.slot.TextSlot;
import dev.huskuraft.universal.api.gui.text.TextWidget;
import dev.huskuraft.universal.api.platform.Entrance;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public final class TransformerList extends EditableEntryList<Transformer> {

    public TransformerList(Entrance entrance, int x, int y, int width, int height) {
        super(entrance, x, y, width, height);
    }

    @Override
    protected EditableEntryList.Entry createHolder(Transformer transformer) {
        return switch (transformer.getType()) {
            case ARRAY -> new ArrayEntry(getEntrance(), this, (ArrayTransformer) transformer);
            case MIRROR -> new MirrorEntry(getEntrance(), this, (MirrorTransformer) transformer);
            case RADIAL -> new RadialEntry(getEntrance(), this, (RadialTransformer) transformer);
            case RANDOMIZER -> new ItemRandomizerEntry(getEntrance(), this, (ItemRandomizer) transformer);
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
                case RANDOMIZER -> Text.text("IR");
            };
        }

        @Override
        public void onCreate() {
            this.textSlot = addWidget(new TextSlot(getEntrance(), getX(), getY(), Dimens.ICON_WIDTH, Dimens.ICON_HEIGHT, getSymbol()));
            this.titleTextWidget = addWidget(new TextWidget(getEntrance(), getX() + Dimens.ICON_WIDTH + 2, getY() + 2, getItem().getName()));
            this.slotContainer = addWidget(new SlotContainer(getEntrance(), getX() + Dimens.ICON_WIDTH + 2, getY() + 12, 0, 0));
        }

        @Override
        public void onReload() {
            slotContainer.setWrapLines(getEntryList().getSelected() == this);
            textSlot.setMessage(getSymbol());
            slotContainer.setEntries(getData());
        }

        protected abstract List<SlotData> getData();

        protected Text getSymbol() {
            return getSymbol(getItem());
        }

        @Override
        public int getHeight() {
            return Dimens.ICON_HEIGHT + 4;
        }
    }


    public static class ArrayEntry extends Entry<ArrayTransformer> {

        public ArrayEntry(Entrance entrance, EntryList entryList, ArrayTransformer arrayTransformer) {
            super(entrance, entryList, arrayTransformer);
        }

        @Override
        protected List<SlotData> getData() {
            return List.of(
                    new SlotData.TextSymbol(formatDouble(getItem().offset().x()), Axis.X.getDisplayName()),
                    new SlotData.TextSymbol(formatDouble(getItem().offset().y()), Axis.Y.getDisplayName()),
                    new SlotData.TextSymbol(formatDouble(getItem().offset().z()), Axis.Z.getDisplayName()),
                    new SlotData.TextSymbol(formatDouble(getItem().count()), Text.text("C"))
            );
        }

    }

    public static class ItemRandomizerEntry extends Entry<ItemRandomizer> {

        public ItemRandomizerEntry(Entrance entrance, EntryList entryList, ItemRandomizer randomizer) {
            super(entrance, entryList, randomizer);
        }

        @Override
        protected List<SlotData> getData() {
            if (getItem().getSource() == ItemRandomizer.Source.CUSTOMIZE) {
                return getItem().getChances().stream().map(chance -> new SlotData.ItemStackSymbol(chance.content().getDefaultStack(), Text.text(String.valueOf(chance.chance())))).collect(Collectors.toList());
            }
            return List.of();
        }

    }

    public static class MirrorEntry extends Entry<MirrorTransformer> {

        public MirrorEntry(Entrance entrance, EntryList entryList, MirrorTransformer mirrorTransformer) {
            super(entrance, entryList, mirrorTransformer);
        }

        @Override
        protected List<SlotData> getData() {
            return List.of(
                    new SlotData.TextSymbol(formatDouble(getItem().position().x()), Axis.X.getDisplayName()),
                    new SlotData.TextSymbol(formatDouble(getItem().position().y()), Axis.Y.getDisplayName()),
                    new SlotData.TextSymbol(formatDouble(getItem().position().z()), Axis.Z.getDisplayName()),
                    new SlotData.TextSymbol(getItem().axis().getDisplayName(), Text.text("A")),
                    new SlotData.TextSymbol(formatDouble(getItem().size()), Text.text("S"))
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
                    new SlotData.TextSymbol(formatDouble(getItem().position().x()), Axis.X.getDisplayName()),
                    new SlotData.TextSymbol(formatDouble(getItem().position().y()), Axis.Y.getDisplayName()),
                    new SlotData.TextSymbol(formatDouble(getItem().position().z()), Axis.Z.getDisplayName()),
                    new SlotData.TextSymbol(getItem().axis().getDisplayName(), Text.text("A")),
                    new SlotData.TextSymbol(formatDouble(getItem().slices()), Text.text("S")),
                    new SlotData.TextSymbol(formatDouble(getItem().radius()), Text.text("R")),
                    new SlotData.TextSymbol(formatDouble(getItem().length()), Text.text("L"))
            );
        }

    }
}
