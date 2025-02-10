package dev.huskuraft.effortless.building.config.tag;

import java.util.Arrays;
import java.util.UUID;

import dev.huskuraft.universal.api.core.Axis;
import dev.huskuraft.universal.api.core.Item;
import dev.huskuraft.universal.api.core.Items;
import dev.huskuraft.universal.api.tag.ListTag;
import dev.huskuraft.universal.api.tag.RecordTag;
import dev.huskuraft.universal.api.tag.StringTag;
import dev.huskuraft.universal.api.tag.Tag;
import dev.huskuraft.universal.api.tag.TagSerializer;
import dev.huskuraft.effortless.building.PositionType;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public class TransformerTagSerializer implements TagSerializer<Transformer> {

    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "Name";
    private static final String TAG_TYPE = "Type";

    @Override
    public Transformer decode(Tag tag) {
        return switch (tag.asRecord().getEnum(TAG_TYPE, Transformers.class)) {
            case ARRAY -> new ArrayTransformerTagSerializer().decode(tag);
            case MIRROR -> new MirrorTransformerTagSerializer().decode(tag);
            case RADIAL -> new RadialTransformerTagSerializer().decode(tag);
            case RANDOMIZER -> new ItemRandomizerTagSerializer().decode(tag);
        };
    }

    @Override
    public Tag encode(Transformer transformer) {
        var tag = switch (transformer.getType()) {
            case ARRAY -> new ArrayTransformerTagSerializer().encode((ArrayTransformer) transformer);
            case MIRROR -> new MirrorTransformerTagSerializer().encode((MirrorTransformer) transformer);
            case RADIAL -> new RadialTransformerTagSerializer().encode((RadialTransformer) transformer);
            case RANDOMIZER -> new ItemRandomizerTagSerializer().encode((ItemRandomizer) transformer);
        };
        tag.asRecord().putEnum(TAG_TYPE, transformer.getType());
        return tag;
    }

    @Override
    public Transformer validate(Transformer value) {
        if (value == null) return null;

        return switch (value.getType()) {
            case ARRAY -> new ArrayTransformerTagSerializer().validate((ArrayTransformer) value);
            case MIRROR -> new MirrorTransformerTagSerializer().validate((MirrorTransformer) value);
            case RADIAL -> new RadialTransformerTagSerializer().validate((RadialTransformer) value);
            case RANDOMIZER -> new ItemRandomizerTagSerializer().validate((ItemRandomizer) value);
        };
    }

    public static class ArrayTransformerTagSerializer implements TagSerializer<ArrayTransformer> {

        private static final String TAG_OFFSET = "Offset";
        private static final String TAG_COUNT = "Count";

        @Override
        public ArrayTransformer decode(Tag tag) {
            return new ArrayTransformer(
                    tag.asRecord().getUUID(TAG_ID),
                    tag.asRecord().getText(TAG_NAME),
                    tag.asRecord().getVector3d(TAG_OFFSET).toVector3i(),
                    tag.asRecord().getInt(TAG_COUNT)
            );
        }

        @Override
        public Tag encode(ArrayTransformer transformer) {
            var tag = RecordTag.newRecord();
            tag.putUUID(TAG_ID, transformer.getId());
            tag.putText(TAG_NAME, transformer.getName());
            tag.putVector3d(TAG_OFFSET, transformer.offset().toVector3d());
            tag.putInt(TAG_COUNT, transformer.count());
            return tag;
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

        @Override
        public PositionType[] decode(Tag tag) {
            return tag.asList().stream().map((tag1) -> tag1.asString().getAsEnum(PositionType.class)).toArray(PositionType[]::new);
        }

        @Override
        public Tag encode(PositionType[] positionTypeList) {
            return ListTag.of(Arrays.stream(positionTypeList).map(StringTag::of).toList());
        }

        @Override
        public PositionType[] validate(PositionType[] value) {
            return value != null && value.length == 3 ? value : new PositionType[]{PositionType.RELATIVE, PositionType.RELATIVE, PositionType.RELATIVE};
        }
    }

    public static class MirrorTransformerTagSerializer implements TagSerializer<MirrorTransformer> {

        private static final String TAG_POSITION = "Position";
        private static final String TAG_SIZE = "Size";
        private static final String TAG_AXIS = "Axis";


        @Override
        public MirrorTransformer decode(Tag tag) {
            return new MirrorTransformer(
                    tag.asRecord().getUUID(TAG_ID),
                    tag.asRecord().getText(TAG_NAME),
                    tag.asRecord().getVector3d(TAG_POSITION),
                    tag.asRecord().getEnum(TAG_AXIS, Axis.class),
                    tag.asRecord().getInt(TAG_SIZE)
            );
        }

        @Override
        public Tag encode(MirrorTransformer transformer) {
            var tag = RecordTag.newRecord();
            tag.asRecord().putUUID(TAG_ID, transformer.getId());
            tag.asRecord().putText(TAG_NAME, transformer.getName());
            tag.asRecord().putVector3d(TAG_POSITION, transformer.position());
            tag.asRecord().putEnum(TAG_AXIS, transformer.axis());
            tag.asRecord().putInt(TAG_AXIS, transformer.size());
            return tag;
        }

        @Override
        public MirrorTransformer validate(MirrorTransformer value) {
            if (value == null) return MirrorTransformer.ZERO_Y;

            return new MirrorTransformer(
                    value.getId() != null ? value.getId() : UUID.randomUUID(),
                    value.getName() != null ? value.getName() : MirrorTransformer.ZERO_Y.getName(),
                    value.position() != null ? value.position() : MirrorTransformer.ZERO_Y.position(),
                    value.axis() != null ? value.axis() : MirrorTransformer.ZERO_Y.axis(),
                    value.size()
            );
        }
    }

    public static class RadialTransformerTagSerializer implements TagSerializer<RadialTransformer> {

        private static final String TAG_POSITION = "Position";
        private static final String TAG_POSITION_TYPE = "PositionType";
        private static final String TAG_SLICE = "Slice";
        private static final String TAG_RADIUS = "Radius";
        private static final String TAG_LENGTH = "Length";

        @Override
        public RadialTransformer decode(Tag tag) {
            return new RadialTransformer(
                    tag.asRecord().getUUID(TAG_ID),
                    tag.asRecord().getText(TAG_NAME),
                    tag.asRecord().getVector3d(TAG_POSITION),
                    tag.asRecord().getEnum(TAG_POSITION_TYPE, Axis.class),
                    tag.asRecord().getInt(TAG_SLICE),
                    tag.asRecord().getInt(TAG_RADIUS),
                    tag.asRecord().getInt(TAG_LENGTH)
            );
        }

        @Override
        public Tag encode(RadialTransformer transformer) {
            var tag = RecordTag.newRecord();
            tag.asRecord().putUUID(TAG_ID, transformer.getId());
            tag.asRecord().putText(TAG_NAME, transformer.getName());
            tag.asRecord().putVector3d(TAG_POSITION, transformer.position());
            tag.asRecord().putInt(TAG_SLICE, transformer.slices());
            tag.asRecord().putInt(TAG_RADIUS, transformer.radius());
            tag.asRecord().putInt(TAG_LENGTH, transformer.length());
            return tag;
        }

        @Override
        public RadialTransformer validate(RadialTransformer value) {
            if (value == null) return RadialTransformer.ZERO;

            return new RadialTransformer(
                    value.getId() != null ? value.getId() : UUID.randomUUID(),
                    value.getName() != null ? value.getName() : RadialTransformer.ZERO.getName(),
                    value.position() != null ? value.position() : RadialTransformer.ZERO.position(),
                    value.axis() != null ? value.axis() : RadialTransformer.ZERO.axis(),
                    value.slices(),
                    value.radius(),
                    value.length()
            );
        }
    }


    public static class ItemRandomizerTagSerializer implements TagSerializer<ItemRandomizer> {

        private static final String TAG_ID = "id";
        private static final String TAG_NAME = "Name";
        private static final String TAG_ORDER = "Order";
        private static final String TAG_TARGET = "Supplier";
        private static final String TAG_CATEGORY = "Category";
        private static final String TAG_SOURCE = "Source";
        private static final String TAG_CHANCES = "Chances";

        @Override
        public ItemRandomizer decode(Tag tag) {
            return new ItemRandomizer(
                    tag.asRecord().getUUID(TAG_ID),
                    tag.asRecord().getText(TAG_NAME),
                    tag.asRecord().getEnum(TAG_ORDER, ItemRandomizer.Order.class),
                    tag.asRecord().getEnum(TAG_TARGET, ItemRandomizer.Target.class),
                    tag.asRecord().getEnum(TAG_SOURCE, ItemRandomizer.Source.class),
                    tag.asRecord().getList(TAG_CHANCES, new ItemChanceTagSerializer())
            );
        }

        @Override
        public Tag encode(ItemRandomizer randomizer) {
            var tag = RecordTag.newRecord();
            tag.asRecord().putUUID(TAG_ID, randomizer.getId());
            tag.asRecord().putText(TAG_NAME, randomizer.getName());
            tag.asRecord().putEnum(TAG_ORDER, randomizer.getOrder());
            tag.asRecord().putEnum(TAG_TARGET, randomizer.getTarget());
//            tag.asRecord().putEnum(TAG_CATEGORY, randomizer.getCategory());
            tag.asRecord().putEnum(TAG_SOURCE, randomizer.getSource());
            tag.asRecord().putList(TAG_CHANCES, randomizer.getChances(), new ItemChanceTagSerializer());
            return tag;
        }

        @Override
        public ItemRandomizer validate(ItemRandomizer value) {
            if (value == null) return ItemRandomizer.EMPTY;

            return new ItemRandomizer(
                    value.getId() != null ? value.getId() : UUID.randomUUID(),
                    value.getName() != null ? value.getName() : ItemRandomizer.EMPTY.getName(),
                    value.getOrder() != null ? value.getOrder() : ItemRandomizer.EMPTY.getOrder(),
                    value.getTarget() != null ? value.getTarget() : ItemRandomizer.EMPTY.getTarget(),
                    value.getSource() != null ? value.getSource() : ItemRandomizer.EMPTY.getSource(),
                    value.getChances() != null ? value.getChances() : ItemRandomizer.EMPTY.getChances()
            );
        }
    }

    public static class ItemChanceTagSerializer implements TagSerializer<Chance<Item>> {

        private static final String TAG_CONTENT = "Content";
        private static final String TAG_CHANCE = "Chance";

        @Override
        public Chance<Item> decode(Tag tag) {
            return Chance.of(tag.asRecord().getItem(TAG_CONTENT), tag.asRecord().getInt(TAG_CHANCE));
        }

        @Override
        public Tag encode(Chance<Item> chance) {
            var tag = RecordTag.newRecord();
            tag.asRecord().putItem(TAG_CONTENT, chance.content());
            tag.asRecord().putInt(TAG_CHANCE, chance.chance());
            return tag;
        }

        @Override
        public Chance<Item> validate(Chance<Item> value) {
            if (value == null || value.content() == null) return Chance.of(Items.AIR.item(), (byte) 0);
            return value;
        }
    }

}
