package dev.huskuraft.effortless.building.config.tag;

import java.util.Arrays;
import java.util.UUID;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.Items;
import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagSerializer;
import dev.huskuraft.effortless.building.PositionType;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.pattern.randomize.Randomizer;

public class TransformerTagSerializer implements TagSerializer<Transformer> {

    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "Name";
    private static final String TAG_TYPE = "Type";

    @Override
    public Transformer read(TagElement tag) {
        return switch (tag.asRecord().getEnum(TAG_TYPE, Transformers.class)) {
            case ARRAY -> tag.asRecord().get(new ArrayTransformerTagSerializer());
            case MIRROR -> tag.asRecord().get(new MirrorTransformerTagSerializer());
            case RADIAL -> tag.asRecord().get(new RadialTransformerTagSerializer());
            case ITEM_RAND -> tag.asRecord().get(new ItemRandomizerTagSerializer());
        };
    }

    @Override
    public void write(TagElement tag, Transformer transformer) {
        tag.asRecord().putEnum(TAG_TYPE, transformer.getType());
        switch (transformer.getType()) {
            case ARRAY -> tag.asRecord().put((ArrayTransformer) transformer, new ArrayTransformerTagSerializer());
            case MIRROR -> tag.asRecord().put((MirrorTransformer) transformer, new MirrorTransformerTagSerializer());
            case RADIAL -> tag.asRecord().put((RadialTransformer) transformer, new RadialTransformerTagSerializer());
            case ITEM_RAND -> tag.asRecord().put((ItemRandomizer) transformer, new ItemRandomizerTagSerializer());
        }
    }

    @Override
    public Transformer validate(Transformer value) {
        if (value == null) return null;

        return switch (value.getType()) {
            case ARRAY -> new ArrayTransformerTagSerializer().validate((ArrayTransformer) value);
            case MIRROR -> new MirrorTransformerTagSerializer().validate((MirrorTransformer) value);
            case RADIAL -> new RadialTransformerTagSerializer().validate((RadialTransformer) value);
            case ITEM_RAND -> new ItemRandomizerTagSerializer().validate((ItemRandomizer) value);
        };
    }

    public static class ArrayTransformerTagSerializer implements TagSerializer<ArrayTransformer> {

        private static final String TAG_OFFSET = "Offset";
        private static final String TAG_COUNT = "Count";

        @Override
        public ArrayTransformer read(TagElement tag) {
            return new ArrayTransformer(
                    tag.asRecord().getUUID(TAG_ID),
                    tag.asRecord().getText(TAG_NAME),
                    tag.asRecord().getVector3d(TAG_OFFSET),
                    tag.asRecord().getInt(TAG_COUNT)
            );
        }

        @Override
        public void write(TagElement tag, ArrayTransformer transformer) {
            tag.asRecord().putUUID(TAG_ID, transformer.getId());
            tag.asRecord().putText(TAG_NAME, transformer.getName());
            tag.asRecord().putVector3d(TAG_OFFSET, transformer.offset());
            tag.asRecord().putInt(TAG_COUNT, transformer.count());
        }

        @Override
        public ArrayTransformer validate(ArrayTransformer value) {
            if (value == null) return ArrayTransformer.ZERO;

            return new ArrayTransformer(
                    value.getId() != null ? value.getId() : UUID.randomUUID(),
                    value.getName() != null ? value.getName() : ArrayTransformer.ZERO.getName(),
                    value.offset() != null ? value.offset() : ArrayTransformer.ZERO.offset(),
                    value.count()
            );
        }
    }

    public static class PositionTypeArrayTagSerializer implements TagSerializer<PositionType[]> {

        private static final String TAG_POSITION_TYPE = "PositionType";

        @Override
        public PositionType[] read(TagElement tag) {
            return tag.asRecord().getList(TAG_POSITION_TYPE, (tag1) -> tag1.asPrimitive().getEnum(PositionType.class)).toArray(PositionType[]::new);
        }

        @Override
        public void write(TagElement tag, PositionType[] positionTypeList) {
            tag.asRecord().putList(TAG_POSITION_TYPE, Arrays.asList(positionTypeList), (tag1, positionType) -> tag1.asPrimitive().putEnum(positionType));
        }

        @Override
        public PositionType[] validate(PositionType[] value) {
            return value != null && value.length == 3 ? value : new PositionType[]{PositionType.RELATIVE_ONCE, PositionType.RELATIVE_ONCE, PositionType.RELATIVE_ONCE};
        }
    }

    public static class MirrorTransformerTagSerializer implements TagSerializer<MirrorTransformer> {

        private static final String TAG_POSITION = "Position";
        private static final String TAG_AXIS = "Axis";

        @Override
        public MirrorTransformer read(TagElement tag) {
            return new MirrorTransformer(
                    tag.asRecord().getUUID(TAG_ID),
                    tag.asRecord().getText(TAG_NAME),
                    tag.asRecord().getVector3d(TAG_POSITION),
                    tag.asRecord().get(new PositionTypeArrayTagSerializer()),
                    tag.asRecord().getEnum(TAG_AXIS, Axis.class)
            );
        }

        @Override
        public void write(TagElement tag, MirrorTransformer transformer) {
            tag.asRecord().putUUID(TAG_ID, transformer.getId());
            tag.asRecord().putText(TAG_NAME, transformer.getName());
            tag.asRecord().putVector3d(TAG_POSITION, transformer.position());
            tag.asRecord().put(transformer.getPositionType(), new PositionTypeArrayTagSerializer());
            tag.asRecord().putEnum(TAG_AXIS, transformer.axis());
        }

        @Override
        public MirrorTransformer validate(MirrorTransformer value) {
            if (value == null) return MirrorTransformer.ZERO_Y;

            return new MirrorTransformer(
                    value.getId() != null ? value.getId() : UUID.randomUUID(),
                    value.getName() != null ? value.getName() : MirrorTransformer.ZERO_Y.getName(),
                    value.position() != null ? value.position() : MirrorTransformer.ZERO_Y.position(),
                    new PositionTypeArrayTagSerializer().validate(value.getPositionType()),
                    value.axis() != null ? value.axis() : MirrorTransformer.ZERO_Y.axis()
            );
        }
    }

    public static class RadialTransformerTagSerializer implements TagSerializer<RadialTransformer> {

        private static final String TAG_POSITION = "Position";
        private static final String TAG_POSITION_TYPE = "PositionType";
        private static final String TAG_SLICE = "Slice";

        @Override
        public RadialTransformer read(TagElement tag) {
            return new RadialTransformer(
                    tag.asRecord().getUUID(TAG_ID),
                    tag.asRecord().getText(TAG_NAME),
                    tag.asRecord().getVector3d(TAG_POSITION),
                    tag.asRecord().get(new PositionTypeArrayTagSerializer()),
                    tag.asRecord().getInt(TAG_SLICE)
            );
        }

        @Override
        public void write(TagElement tag, RadialTransformer transformer) {
            tag.asRecord().putUUID(TAG_ID, transformer.getId());
            tag.asRecord().putText(TAG_NAME, transformer.getName());
            tag.asRecord().putVector3d(TAG_POSITION, transformer.position());
            tag.asRecord().put(transformer.getPositionType(), new PositionTypeArrayTagSerializer());
            tag.asRecord().putInt(TAG_SLICE, transformer.slices());
        }

        @Override
        public RadialTransformer validate(RadialTransformer value) {
            if (value == null) return RadialTransformer.ZERO;

            return new RadialTransformer(
                    value.getId() != null ? value.getId() : UUID.randomUUID(),
                    value.getName() != null ? value.getName() : RadialTransformer.ZERO.getName(),
                    value.position() != null ? value.position() : RadialTransformer.ZERO.position(),
                    new PositionTypeArrayTagSerializer().validate(value.getPositionType()),
                    value.slices()
            );
        }
    }

//    public static class RandomizerSerializer extends TagSerializer<Randomizer<?>> {
//
//        private static final String TAG_CATEGORY = "Category";
//
//        @Override
//        public Randomizer<?> read(TagElement tag) {
//            return switch (tag.getAsRecord().getEnum(TAG_CATEGORY, Randomizer.Category.class)) {
//                case ITEM -> tag.getAsRecord().get(ItemRandomizerSerializer::new);
//            };
//        }
//
//        @Override
//        public void write(TagElement tag, Randomizer<?> randomizer) {
//            tag.getAsRecord().putEnum(TAG_CATEGORY, randomizer.getCategory());
//            if (Objects.requireNonNull(randomizer.getCategory()) == Randomizer.Category.ITEM) {
//                tag.getAsRecord().put((ItemRandomizer) randomizer, ItemRandomizerSerializer::new);
//            }
//        }
//    }

    public static class ItemRandomizerTagSerializer implements TagSerializer<ItemRandomizer> {

        private static final String TAG_ID = "id";
        private static final String TAG_NAME = "Name";
        private static final String TAG_ORDER = "Order";
        private static final String TAG_SUPPLIER = "Supplier";
        private static final String TAG_CATEGORY = "Category";
        private static final String TAG_CHANCES = "Chances";

        @Override
        public ItemRandomizer read(TagElement tag) {
            return new ItemRandomizer(
                    tag.asRecord().getUUID(TAG_ID),
                    tag.asRecord().getText(TAG_NAME),
                    tag.asRecord().getEnum(TAG_ORDER, Randomizer.Order.class),
                    tag.asRecord().getEnum(TAG_SUPPLIER, Randomizer.Target.class),
                    tag.asRecord().getEnum(TAG_CATEGORY, Randomizer.Category.class),
                    tag.asRecord().getList(TAG_CHANCES, new ItemChanceTagSerializer())
            );
        }

        @Override
        public void write(TagElement tag, ItemRandomizer randomizer) {
            tag.asRecord().putUUID(TAG_ID, randomizer.getId());
            tag.asRecord().putText(TAG_NAME, randomizer.getName());
            tag.asRecord().putEnum(TAG_ORDER, randomizer.getOrder());
            tag.asRecord().putEnum(TAG_SUPPLIER, randomizer.getTarget());
            tag.asRecord().putEnum(TAG_CATEGORY, randomizer.getCategory());
            tag.asRecord().putList(TAG_CHANCES, randomizer.getChances(), new ItemChanceTagSerializer());
        }

        @Override
        public ItemRandomizer validate(ItemRandomizer value) {
            if (value == null) return ItemRandomizer.EMPTY;

            return new ItemRandomizer(
                    value.getId() != null ? value.getId() : UUID.randomUUID(),
                    value.getName() != null ? value.getName() : ItemRandomizer.EMPTY.getName(),
                    value.getOrder() != null ? value.getOrder() : ItemRandomizer.EMPTY.getOrder(),
                    value.getTarget() != null ? value.getTarget() : ItemRandomizer.EMPTY.getTarget(),
                    value.getCategory() != null ? value.getCategory() : ItemRandomizer.EMPTY.getCategory(),
                    value.getChances() != null ? value.getChances() : ItemRandomizer.EMPTY.getChances()
            );
        }
    }

    public static class ItemChanceTagSerializer implements TagSerializer<Chance<Item>> {

        private static final String TAG_CONTENT = "Content";
        private static final String TAG_CHANCE = "Chance";

        @Override
        public Chance<Item> read(TagElement tag) {
            return Chance.of(tag.asRecord().getItem(TAG_CONTENT), tag.asRecord().getInt(TAG_CHANCE));
        }

        @Override
        public void write(TagElement tag, Chance<Item> chance) {
            tag.asRecord().putItem(TAG_CONTENT, chance.content());
            tag.asRecord().putInt(TAG_CHANCE, chance.chance());
        }

        @Override
        public Chance<Item> validate(Chance<Item> value) {
            if (value == null || value.content() == null) return Chance.of(Items.AIR.item(), (byte) 0);
            return value;
        }
    }

}
