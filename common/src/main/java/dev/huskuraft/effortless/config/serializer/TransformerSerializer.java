package dev.huskuraft.effortless.config.serializer;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.building.PositionType;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.pattern.randomize.Randomizer;
import dev.huskuraft.effortless.tag.TagElement;
import dev.huskuraft.effortless.tag.TagSerializer;

import java.util.Arrays;

public class TransformerSerializer extends TagSerializer<Transformer> {

    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "Name";
    private static final String TAG_TYPE = "Type";

    @Override
    public Transformer read(TagElement tag) {
        return switch (tag.asRecord().getEnum(TAG_TYPE, Transformers.class)) {
            case ARRAY -> tag.asRecord().get(new ArrayTransformerSerializer());
            case MIRROR -> tag.asRecord().get(new MirrorTransformerSerializer());
            case RADIAL -> tag.asRecord().get(new RadialTransformerSerializer());
            case ITEM_RAND -> tag.asRecord().get(new ItemRandomizerSerializer());
        };
    }

    @Override
    public void write(TagElement tag, Transformer transformer) {
        tag.asRecord().putEnum(TAG_TYPE, transformer.getType());
        switch (transformer.getType()) {
            case ARRAY -> tag.asRecord().put((ArrayTransformer) transformer, new ArrayTransformerSerializer());
            case MIRROR -> tag.asRecord().put((MirrorTransformer) transformer, new MirrorTransformerSerializer());
            case RADIAL -> tag.asRecord().put((RadialTransformer) transformer, new RadialTransformerSerializer());
            case ITEM_RAND -> tag.asRecord().put((ItemRandomizer) transformer, new ItemRandomizerSerializer());
        }
    }

    public static class ArrayTransformerSerializer extends TagSerializer<ArrayTransformer> {

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

    }

    public static class MirrorTransformerSerializer extends TagSerializer<MirrorTransformer> {

        private static final String TAG_POSITION = "Position";
        private static final String TAG_POSITION_TYPE = "PositionType";
        private static final String TAG_AXIS = "Axis";

        @Override
        public MirrorTransformer read(TagElement tag) {
            return new MirrorTransformer(
                    tag.asRecord().getUUID(TAG_ID),
                    tag.asRecord().getText(TAG_NAME),
                    tag.asRecord().getVector3d(TAG_POSITION),
                    tag.asRecord().getList(TAG_POSITION_TYPE, (tag1) -> {
                        return tag1.asPrimitive().getEnum(PositionType.class);
                    }).toArray(PositionType[]::new),
                    tag.asRecord().getEnum(TAG_AXIS, Axis.class)
            );
        }

        @Override
        public void write(TagElement tag, MirrorTransformer transformer) {
            tag.asRecord().putUUID(TAG_ID, transformer.getId());
            tag.asRecord().putText(TAG_NAME, transformer.getName());
            tag.asRecord().putVector3d(TAG_POSITION, transformer.position());
            tag.asRecord().putList(TAG_POSITION_TYPE, Arrays.asList(transformer.getPositionType()), (tag1, positionType) -> {
                tag1.asPrimitive().putEnum(positionType);
            });
            tag.asRecord().putEnum(TAG_AXIS, transformer.axis());
        }

    }

    public static class RadialTransformerSerializer extends TagSerializer<RadialTransformer> {

        private static final String TAG_POSITION = "Position";
        private static final String TAG_POSITION_TYPE = "PositionType";
        private static final String TAG_SLICE = "Slice";

        @Override
        public RadialTransformer read(TagElement tag) {
            return new RadialTransformer(
                    tag.asRecord().getUUID(TAG_ID),
                    tag.asRecord().getText(TAG_NAME),
                    tag.asRecord().getVector3d(TAG_POSITION),
                    tag.asRecord().getList(TAG_POSITION_TYPE, (tag1) -> {
                        return tag1.asPrimitive().getEnum(PositionType.class);
                    }).toArray(PositionType[]::new),
                    tag.asRecord().getInt(TAG_SLICE)
            );
        }

        @Override
        public void write(TagElement tag, RadialTransformer transformer) {
            tag.asRecord().putUUID(TAG_ID, transformer.getId());
            tag.asRecord().putText(TAG_NAME, transformer.getName());
            tag.asRecord().putVector3d(TAG_POSITION, transformer.position());
            tag.asRecord().putList(TAG_POSITION_TYPE, Arrays.asList(transformer.getPositionType()), (tag1, positionType) -> {
                tag1.asPrimitive().putEnum(positionType);
            });
            tag.asRecord().putInt(TAG_SLICE, transformer.slices());
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

    public static class ItemRandomizerSerializer extends TagSerializer<ItemRandomizer> {

        private static final String TAG_ID = "id";
        private static final String TAG_NAME = "Name";
        private static final String TAG_ORDER = "Order";
        private static final String TAG_SUPPLIER = "Supplier";
        private static final String TAG_CATEGORY = "Category";
        private static final String TAG_CHANCES = "Chances";
        private static final String TAG_CONTENT = "Content";
        private static final String TAG_CHANCE = "Chance";

        @Override
        public ItemRandomizer read(TagElement tag) {
            return new ItemRandomizer(
                    tag.asRecord().getUUID(TAG_ID),
                    tag.asRecord().getText(TAG_NAME),
                    tag.asRecord().getEnum(TAG_ORDER, Randomizer.Order.class),
                    tag.asRecord().getEnum(TAG_SUPPLIER, Randomizer.Target.class),
                    tag.asRecord().getEnum(TAG_CATEGORY, Randomizer.Category.class),
                    tag.asRecord().getList(TAG_CHANCES, tag1 -> {
                        return Chance.of(tag1.asRecord().getItem(TAG_CONTENT), tag1.asRecord().getByte(TAG_CHANCE));
                    })
            );
        }

        @Override
        public void write(TagElement tag, ItemRandomizer randomizer) {
            tag.asRecord().putUUID(TAG_ID, randomizer.getId());
            tag.asRecord().putText(TAG_NAME, randomizer.getName());
            tag.asRecord().putEnum(TAG_ORDER, randomizer.getOrder());
            tag.asRecord().putEnum(TAG_SUPPLIER, randomizer.getTarget());
            tag.asRecord().putEnum(TAG_CATEGORY, randomizer.getCategory());
            tag.asRecord().putList(TAG_CHANCES, randomizer.getChances(), (tag1, chance) -> {
                tag1.asRecord().putItem(TAG_CONTENT, chance.content());
                tag1.asRecord().putByte(TAG_CHANCE, chance.chance());
            });
        }

    }

}