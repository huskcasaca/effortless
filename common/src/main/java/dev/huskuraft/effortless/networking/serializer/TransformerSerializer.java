package dev.huskuraft.effortless.networking.serializer;

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
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.networking.BufferSerializer;

import java.util.Arrays;

public class TransformerSerializer extends BufferSerializer<Transformer> {

    @Override
    public Transformer read(Buffer buffer) {
        return switch (buffer.readEnum(Transformers.class)) {
            case ARRAY -> buffer.read(new ArrayTransformerSerializer());
            case MIRROR -> buffer.read(new MirrorTransformerSerializer());
            case RADIAL -> buffer.read(new RadialTransformerSerializer());
            case ITEM_RAND -> buffer.read(new ItemRandomizerSerializer());
        };
    }

    @Override
    public void write(Buffer buffer, Transformer transformer) {
        buffer.writeEnum(transformer.getType());
        switch (transformer.getType()) {
            case ARRAY -> buffer.write((ArrayTransformer) transformer, new ArrayTransformerSerializer());
            case MIRROR -> buffer.write((MirrorTransformer) transformer, new MirrorTransformerSerializer());
            case RADIAL -> buffer.write((RadialTransformer) transformer, new RadialTransformerSerializer());
            case ITEM_RAND -> buffer.write((ItemRandomizer) transformer, new ItemRandomizerSerializer());
        }
    }

    static class ArrayTransformerSerializer extends BufferSerializer<ArrayTransformer> {

        @Override
        public ArrayTransformer read(Buffer buffer) {
            return new ArrayTransformer(
                    buffer.readUUID(),
                    buffer.readText(),
                    buffer.readVector3d(),
                    buffer.readInt()
            );
        }

        @Override
        public void write(Buffer buffer, ArrayTransformer transformer) {
            buffer.writeUUID(transformer.getId());
            buffer.writeText(transformer.getName());
            buffer.writeVector3d(transformer.offset());
            buffer.writeInt(transformer.count());
        }

    }

    static class MirrorTransformerSerializer extends BufferSerializer<MirrorTransformer> {

        @Override
        public MirrorTransformer read(Buffer buffer) {
            return new MirrorTransformer(
                    buffer.readUUID(),
                    buffer.readText(),
                    buffer.readVector3d(),
                    buffer.readList(buffer1 -> buffer1.readEnum(PositionType.class)).toArray(PositionType[]::new),
                    buffer.readEnum(Axis.class)
            );
        }

        @Override
        public void write(Buffer buffer, MirrorTransformer transformer) {
            buffer.writeUUID(transformer.getId());
            buffer.writeText(transformer.getName());
            buffer.writeVector3d(transformer.position());
            buffer.writeList(Arrays.asList(transformer.getPositionType()), Buffer::writeEnum);
            buffer.writeEnum(transformer.axis());
        }

    }

    static class RadialTransformerSerializer extends BufferSerializer<RadialTransformer> {

        @Override
        public RadialTransformer read(Buffer buffer) {
            return new RadialTransformer(
                    buffer.readUUID(),
                    buffer.readText(),
                    buffer.readVector3d(),
                    buffer.readList(buffer1 -> buffer1.readEnum(PositionType.class)).toArray(PositionType[]::new),
                    buffer.readInt()
            );
        }

        @Override
        public void write(Buffer buffer, RadialTransformer transformer) {
            buffer.writeUUID(transformer.getId());
            buffer.writeText(transformer.getName());
            buffer.writeVector3d(transformer.position());
            buffer.writeList(Arrays.asList(transformer.getPositionType()), Buffer::writeEnum);
            buffer.writeInt(transformer.slices());
        }

    }

//    static class RandomizerSerializer extends BufferSerializer<Randomizer<?>> {
//
//        @Override
//        public Randomizer<?> read(Buffer buffer) {
//            return switch (buffer.readEnum(Randomizer.Category.class)) {
//                case ITEM -> {
//                    yield buffer.read(new ItemRandomizerSerializer());
//                }
//            };
//        }
//
//        @Override
//        public void write(Buffer buffer, Randomizer<?> randomizer) {
//            buffer.writeEnum(randomizer.getCategory());
//            if (Objects.requireNonNull(randomizer.getCategory()) == Randomizer.Category.ITEM) {
//                buffer.write((ItemRandomizer) randomizer, new ItemRandomizerSerializer());
//            }
//        }
//    }

    static class ItemRandomizerSerializer extends BufferSerializer<ItemRandomizer> {

        @Override
        public ItemRandomizer read(Buffer buffer) {
            return new ItemRandomizer(
                    buffer.readUUID(),
                    buffer.readText(),
                    buffer.readEnum(Randomizer.Order.class),
                    buffer.readEnum(Randomizer.Target.class),
                    buffer.readEnum(Randomizer.Category.class),
                    buffer.readList(buffer1 -> {
                        return Chance.of(buffer1.readItem(), buffer1.readByte());
                    })
            );
        }

        @Override
        public void write(Buffer buffer, ItemRandomizer transformer) {
            buffer.writeUUID(transformer.getId());
            buffer.writeText(transformer.getName());
            buffer.writeEnum(transformer.getOrder());
            buffer.writeEnum(transformer.getTarget());
            buffer.writeEnum(transformer.getCategory());
            buffer.writeList(transformer.getChances(), (buf, chance) -> {
                buf.writeItem(chance.content());
                buf.writeByte(chance.chance());
            });
        }
    }


}
