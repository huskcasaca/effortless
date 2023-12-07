package dev.huskuraft.effortless.config.serializer;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.pattern.randomize.Randomizer;
import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.tag.TagElement;
import dev.huskuraft.effortless.tag.TagSerializer;

public class TransformerSerializer extends TagSerializer<Transformer> {

    private static final String TAG_TYPE = "Type";

    @Override
    public Transformer read(TagElement tag) {
        return switch (tag.getAsRecord().getEnum(TAG_TYPE, Transformers.class)) {
            case ARRAY -> tag.getAsRecord().get(ArrayTransformerSerializer::new);
            case MIRROR -> tag.getAsRecord().get(MirrorTransformerSerializer::new);
            case RADIAL -> tag.getAsRecord().get(RadialTransformerSerializer::new);
            case ITEM_RAND -> tag.getAsRecord().get(ItemRandomizerSerializer::new);
        };
    }

    @Override
    public void write(TagElement tag, Transformer transformer) {
        tag.getAsRecord().putEnum(TAG_TYPE, transformer.getType());
        switch (transformer.getType()) {
            case ARRAY -> tag.getAsRecord().put((ArrayTransformer) transformer, ArrayTransformerSerializer::new);
            case MIRROR -> tag.getAsRecord().put((MirrorTransformer) transformer, MirrorTransformerSerializer::new);
            case RADIAL -> tag.getAsRecord().put((RadialTransformer) transformer, RadialTransformerSerializer::new);
            case ITEM_RAND -> tag.getAsRecord().put((ItemRandomizer) transformer, ItemRandomizerSerializer::new);
        }
    }

    public static class ArrayTransformerSerializer extends TagSerializer<ArrayTransformer> {

        private static final String TAG_OFFSET = "Offset";
        private static final String TAG_COUNT = "Count";

        @Override
        public ArrayTransformer read(TagElement tag) {
            return new ArrayTransformer(
                    tag.getAsRecord().getElement(TAG_OFFSET, Vector3dSerializer::new),
                    tag.getAsRecord().getInt(TAG_COUNT)
            );
        }

        @Override
        public void write(TagElement tag, ArrayTransformer arrayTransformer) {
            tag.getAsRecord().putElement(TAG_OFFSET, arrayTransformer.offset(), Vector3dSerializer::new);
            tag.getAsRecord().putInt(TAG_COUNT, arrayTransformer.count());
        }

    }

    public static class MirrorTransformerSerializer extends TagSerializer<MirrorTransformer> {

        private static final String TAG_OFFSET = "Offset";
        private static final String TAG_AXIS = "Axis";

        @Override
        public MirrorTransformer read(TagElement tag) {
            return new MirrorTransformer(
                    tag.getAsRecord().getElement(TAG_OFFSET, Vector3dSerializer::new),
                    tag.getAsRecord().getEnum(TAG_AXIS, Axis.class)
            );
        }

        @Override
        public void write(TagElement tag, MirrorTransformer mirrorTransformer) {
            tag.getAsRecord().putElement(TAG_OFFSET, mirrorTransformer.position(), Vector3dSerializer::new);
            tag.getAsRecord().putEnum(TAG_AXIS, mirrorTransformer.axis());
        }


    }

    public static class RadialTransformerSerializer extends TagSerializer<RadialTransformer> {

        private static final String TAG_POSITION = "Position";
        private static final String TAG_SLICE = "Slice";

        @Override
        public RadialTransformer read(TagElement tag) {
            return new RadialTransformer(
                    tag.getAsRecord().getElement(TAG_POSITION, Vector3dSerializer::new),
                    tag.getAsRecord().getInt(TAG_SLICE)
            );
        }

        @Override
        public void write(TagElement tag, RadialTransformer radialTransformer) {
            tag.getAsRecord().putElement(TAG_POSITION, radialTransformer.position(), Vector3dSerializer::new);
            tag.getAsRecord().putInt(TAG_SLICE, radialTransformer.slices());
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
                    tag.getAsRecord().getText(TAG_NAME),
                    tag.getAsRecord().getEnum(TAG_ORDER, Randomizer.Order.class),
                    tag.getAsRecord().getEnum(TAG_SUPPLIER, Randomizer.Target.class),
                    tag.getAsRecord().getEnum(TAG_CATEGORY, Randomizer.Category.class),
                    tag.getAsRecord().getList(TAG_CHANCES, () -> tag1 -> {
                        return Chance.of(tag1.getAsRecord().getItem(TAG_CONTENT), tag1.getAsRecord().getByte(TAG_CHANCE));
                    })
            );
        }

        @Override
        public void write(TagElement tag, ItemRandomizer randomizer) {
            tag.getAsRecord().putText(TAG_NAME, randomizer.getName());
            tag.getAsRecord().putEnum(TAG_ORDER, randomizer.getOrder());
            tag.getAsRecord().putEnum(TAG_SUPPLIER, randomizer.getTarget());
            tag.getAsRecord().putEnum(TAG_CATEGORY, randomizer.getCategory());
            tag.getAsRecord().putList(TAG_CHANCES, randomizer.getChances(), () -> (tag1, chance) -> {
                tag1.getAsRecord().putItem(TAG_CONTENT, chance.content());
                tag1.getAsRecord().putByte(TAG_CHANCE, chance.chance());
            });
        }

    }

}