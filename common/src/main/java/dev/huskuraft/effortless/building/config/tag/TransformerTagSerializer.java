package dev.huskuraft.effortless.building.config.tag;

import java.util.Arrays;
import java.util.UUID;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.Items;
import dev.huskuraft.effortless.api.tag.TagElement;
import dev.huskuraft.effortless.api.tag.TagList;
import dev.huskuraft.effortless.api.tag.TagLiteral;
import dev.huskuraft.effortless.api.tag.TagRecord;
import dev.huskuraft.effortless.api.tag.TagSerializer;
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
    public Transformer decode(TagElement tag) {
        return switch (tag.asRecord().getEnum(TAG_TYPE, Transformers.class)) {
            case ARRAY -> new ArrayTransformerTagSerializer().decode(tag);
            case MIRROR -> new MirrorTransformerTagSerializer().decode(tag);
            case RADIAL -> new RadialTransformerTagSerializer().decode(tag);
            case ITEM_RAND -> new ItemRandomizerTagSerializer().decode(tag);
        };
    }

    @Override
    public TagElement encode(Transformer transformer) {
        var tag = switch (transformer.getType()) {
            case ARRAY -> new ArrayTransformerTagSerializer().encode((ArrayTransformer) transformer);
            case MIRROR -> new MirrorTransformerTagSerializer().encode((MirrorTransformer) transformer);
            case RADIAL -> new RadialTransformerTagSerializer().encode((RadialTransformer) transformer);
            case ITEM_RAND -> new ItemRandomizerTagSerializer().encode((ItemRandomizer) transformer);
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
            case ITEM_RAND -> new ItemRandomizerTagSerializer().validate((ItemRandomizer) value);
        };
    }

    public static class ArrayTransformerTagSerializer implements TagSerializer<ArrayTransformer> {

        private static final String TAG_OFFSET = "Offset";
        private static final String TAG_COUNT = "Count";

        @Override
        public ArrayTransformer decode(TagElement tag) {
            return new ArrayTransformer(
                    tag.asRecord().getUUID(TAG_ID),
                    tag.asRecord().getText(TAG_NAME),
                    tag.asRecord().getVector3d(TAG_OFFSET),
                    tag.asRecord().getInt(TAG_COUNT)
            );
        }

        @Override
        public TagElement encode(ArrayTransformer transformer) {
            var tag = TagRecord.newRecord();
            tag.putUUID(TAG_ID, transformer.getId());
            tag.putText(TAG_NAME, transformer.getName());
            tag.putVector3d(TAG_OFFSET, transformer.offset());
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
        public PositionType[] decode(TagElement tag) {
            return tag.asList().stream().map((tag1) -> tag1.asLiteral().getAsEnum(PositionType.class)).toArray(PositionType[]::new);
        }

        @Override
        public TagElement encode(PositionType[] positionTypeList) {
            var tag = TagList.newList();
            Arrays.stream(positionTypeList).map(TagLiteral::of).forEach(tag::addTag);
            return tag;
        }

        @Override
        public PositionType[] validate(PositionType[] value) {
            return value != null && value.length == 3 ? value : new PositionType[]{PositionType.RELATIVE_ONCE, PositionType.RELATIVE_ONCE, PositionType.RELATIVE_ONCE};
        }
    }

    public static class MirrorTransformerTagSerializer implements TagSerializer<MirrorTransformer> {

        private static final String TAG_POSITION = "Position";
        private static final String TAG_POSITION_TYPE = "PositionType";
        private static final String TAG_AXIS = "Axis";


        @Override
        public MirrorTransformer decode(TagElement tag) {
            return new MirrorTransformer(
                    tag.asRecord().getUUID(TAG_ID),
                    tag.asRecord().getText(TAG_NAME),
                    tag.asRecord().getVector3d(TAG_POSITION),
                    tag.asRecord().getTag(TAG_POSITION_TYPE, new PositionTypeArrayTagSerializer()),
                    tag.asRecord().getEnum(TAG_AXIS, Axis.class)
            );
        }

        @Override
        public TagElement encode(MirrorTransformer transformer) {
            var tag = TagRecord.newRecord();
            tag.asRecord().putUUID(TAG_ID, transformer.getId());
            tag.asRecord().putText(TAG_NAME, transformer.getName());
            tag.asRecord().putVector3d(TAG_POSITION, transformer.position());
            tag.asRecord().putTag(TAG_POSITION_TYPE, transformer.getPositionType(), new PositionTypeArrayTagSerializer());
            tag.asRecord().putEnum(TAG_AXIS, transformer.axis());
            return tag;
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
        public RadialTransformer decode(TagElement tag) {
            return new RadialTransformer(
                    tag.asRecord().getUUID(TAG_ID),
                    tag.asRecord().getText(TAG_NAME),
                    tag.asRecord().getVector3d(TAG_POSITION),
                    tag.asRecord().getTag(TAG_POSITION_TYPE, new PositionTypeArrayTagSerializer()),
                    tag.asRecord().getInt(TAG_SLICE)
            );
        }

        @Override
        public TagElement encode(RadialTransformer transformer) {
            var tag = TagRecord.newRecord();
            tag.asRecord().putUUID(TAG_ID, transformer.getId());
            tag.asRecord().putText(TAG_NAME, transformer.getName());
            tag.asRecord().putVector3d(TAG_POSITION, transformer.position());
            tag.asRecord().putTag(TAG_POSITION_TYPE, transformer.getPositionType(), new PositionTypeArrayTagSerializer());
            tag.asRecord().putInt(TAG_SLICE, transformer.slices());
            return tag;
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


    public static class ItemRandomizerTagSerializer implements TagSerializer<ItemRandomizer> {

        private static final String TAG_ID = "id";
        private static final String TAG_NAME = "Name";
        private static final String TAG_ORDER = "Order";
        private static final String TAG_TARGET = "Supplier";
        private static final String TAG_CATEGORY = "Category";
        private static final String TAG_SOURCE = "Source";
        private static final String TAG_CHANCES = "Chances";

        @Override
        public ItemRandomizer decode(TagElement tag) {
            return new ItemRandomizer(
                    tag.asRecord().getUUID(TAG_ID),
                    tag.asRecord().getText(TAG_NAME),
                    tag.asRecord().getEnum(TAG_ORDER, ItemRandomizer.Order.class),
                    tag.asRecord().getEnum(TAG_TARGET, ItemRandomizer.Target.class),
                    tag.asRecord().getEnum(TAG_CATEGORY, ItemRandomizer.Category.class),
                    tag.asRecord().getEnum(TAG_SOURCE, ItemRandomizer.Source.class),
                    tag.asRecord().getList(TAG_CHANCES, new ItemChanceTagSerializer())
            );
        }

        @Override
        public TagElement encode(ItemRandomizer randomizer) {
            var tag = TagRecord.newRecord();
            tag.asRecord().putUUID(TAG_ID, randomizer.getId());
            tag.asRecord().putText(TAG_NAME, randomizer.getName());
            tag.asRecord().putEnum(TAG_ORDER, randomizer.getOrder());
            tag.asRecord().putEnum(TAG_TARGET, randomizer.getTarget());
            tag.asRecord().putEnum(TAG_CATEGORY, randomizer.getCategory());
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
                    value.getCategory() != null ? value.getCategory() : ItemRandomizer.EMPTY.getCategory(),
                    value.getSource() != null ? value.getSource() : ItemRandomizer.EMPTY.getSource(),
                    value.getChances() != null ? value.getChances() : ItemRandomizer.EMPTY.getChances()
            );
        }
    }

    public static class ItemChanceTagSerializer implements TagSerializer<Chance<Item>> {

        private static final String TAG_CONTENT = "Content";
        private static final String TAG_CHANCE = "Chance";

        @Override
        public Chance<Item> decode(TagElement tag) {
            return Chance.of(tag.asRecord().getItem(TAG_CONTENT), tag.asRecord().getInt(TAG_CHANCE));
        }

        @Override
        public TagElement encode(Chance<Item> chance) {
            var tag = TagRecord.newRecord();
            tag.asRecord().putItem(TAG_CONTENT, chance.content());
            tag.asRecord().putInt(TAG_CHANCE, chance.chance());
            return tag;
        }

        @Override
        public Chance<Item> validate(Chance<Item> value) {
            if (value == null || value.content() == null) return Chance.of(Items.AIR, (byte) 0);
            return value;
        }
    }

}
