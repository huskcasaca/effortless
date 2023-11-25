package dev.huskuraft.effortless.config.serializer;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.building.pattern.array.Array;
import dev.huskuraft.effortless.building.pattern.mirror.Mirror;
import dev.huskuraft.effortless.building.pattern.raidal.Radial;
import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.pattern.randomize.Randomizer;
import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.tag.TagElement;
import dev.huskuraft.effortless.tag.TagSerializer;

import java.util.Objects;

public class TransformerSerializer extends TagSerializer<Transformer> {

    private static final String TAG_TYPE = "Type";

    @Override
    public Transformer read(TagElement tag) {
        return switch (tag.getAsRecord().getEnum(TAG_TYPE, Transformers.class)) {
            case ARRAY -> tag.getAsRecord().get(ArraySerializer::new);
            case MIRROR -> tag.getAsRecord().get(MirrorSerializer::new);
            case RADIAL -> tag.getAsRecord().get(RadialSerializer::new);
            case ITEM_RANDOM -> tag.getAsRecord().get(ItemRandomizerSerializer::new);
        };
    }

    @Override
    public void write(TagElement tag, Transformer transformer) {
        tag.getAsRecord().putEnum(TAG_TYPE, transformer.getType());
        switch (transformer.getType()) {
            case ARRAY -> tag.getAsRecord().put((Array) transformer, ArraySerializer::new);
            case MIRROR -> tag.getAsRecord().put((Mirror) transformer, MirrorSerializer::new);
            case RADIAL -> tag.getAsRecord().put((Radial) transformer, RadialSerializer::new);
            case ITEM_RANDOM -> tag.getAsRecord().put((ItemRandomizer) transformer, ItemRandomizerSerializer::new);
        }
    }


    public static class ArraySerializer extends TagSerializer<Array> {

        private static final String TAG_OFFSET = "Offset";
        private static final String TAG_COUNT = "Count";

        @Override
        public Array read(TagElement tag) {
            return new Array(
                    tag.getAsRecord().getElement(TAG_OFFSET, Vector3dSerializer::new),
                    tag.getAsRecord().getInt(TAG_COUNT)
            );
        }

        @Override
        public void write(TagElement tag, Array array) {
            tag.getAsRecord().putElement(TAG_OFFSET, array.offset(), Vector3dSerializer::new);
            tag.getAsRecord().putInt(TAG_COUNT, array.count());
        }

    }

    public static class MirrorSerializer extends TagSerializer<Mirror> {

        private static final String TAG_OFFSET = "Offset";
        private static final String TAG_AXIS = "Axis";

        @Override
        public Mirror read(TagElement tag) {
            return new Mirror(
                    tag.getAsRecord().getElement(TAG_OFFSET, Vector3dSerializer::new),
                    tag.getAsRecord().getEnum(TAG_AXIS, Axis.class)
            );
        }

        @Override
        public void write(TagElement tag, Mirror mirror) {
            tag.getAsRecord().putElement(TAG_OFFSET, mirror.position(), Vector3dSerializer::new);
            tag.getAsRecord().putEnum(TAG_AXIS, mirror.axis());
        }


    }

    public static class RadialSerializer extends TagSerializer<Radial> {

        private static final String TAG_POSITION = "Position";
        private static final String TAG_SLICE = "Slice";

        @Override
        public Radial read(TagElement tag) {
            return new Radial(
                    tag.getAsRecord().getElement(TAG_POSITION, Vector3dSerializer::new),
                    tag.getAsRecord().getInt(TAG_SLICE)
            );
        }

        @Override
        public void write(TagElement tag, Radial radial) {
            tag.getAsRecord().putElement(TAG_POSITION, radial.position(), Vector3dSerializer::new);
            tag.getAsRecord().putInt(TAG_SLICE, radial.slice());
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
                    tag.getAsRecord().getString(TAG_NAME),
                    tag.getAsRecord().getEnum(TAG_ORDER, Randomizer.Order.class),
                    tag.getAsRecord().getEnum(TAG_SUPPLIER, Randomizer.Target.class),
                    tag.getAsRecord().getEnum(TAG_CATEGORY, Randomizer.Category.class),
                    tag.getAsRecord().getList(TAG_CHANCES, () -> tag1 -> {
                        return Chance.itemStack(tag1.getAsRecord().getItemStack(TAG_CONTENT), tag1.getAsRecord().getByte(TAG_CHANCE));
                    })
            );
        }

        @Override
        public void write(TagElement tag, ItemRandomizer randomizer) {
            tag.getAsRecord().putString(TAG_NAME, randomizer.getName());
            tag.getAsRecord().putEnum(TAG_ORDER, randomizer.getOrder());
            tag.getAsRecord().putEnum(TAG_SUPPLIER, randomizer.getTarget());
            tag.getAsRecord().putEnum(TAG_CATEGORY, randomizer.getCategory());
            tag.getAsRecord().putList(TAG_CHANCES, randomizer.getChances(), () -> (tag1, chance) -> {
                tag1.getAsRecord().putItemStack(TAG_CONTENT, chance.content());
                tag1.getAsRecord().putByte(TAG_CHANCE, chance.chance());
            });
        }

    }

}