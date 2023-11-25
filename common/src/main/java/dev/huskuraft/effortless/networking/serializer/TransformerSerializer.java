package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.pattern.randomize.Randomizer;
import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.networking.BufferSerializer;

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
                    buffer.readDouble(),
                    buffer.readDouble(),
                    buffer.readDouble(),
                    buffer.readInt()
            );
        }

        @Override
        public void write(Buffer buffer, ArrayTransformer arrayTransformer) {
            buffer.writeDouble(arrayTransformer.x());
            buffer.writeDouble(arrayTransformer.y());
            buffer.writeDouble(arrayTransformer.z());
            buffer.writeInt(arrayTransformer.count());
        }

    }

    static class MirrorTransformerSerializer extends BufferSerializer<MirrorTransformer> {

        @Override
        public MirrorTransformer read(Buffer buffer) {
            return new MirrorTransformer(
                    new Vector3d(
                            buffer.readDouble(),
                            buffer.readDouble(),
                            buffer.readDouble()
                    ),
                    buffer.readEnum(Axis.class)
            );
        }

        @Override
        public void write(Buffer buffer, MirrorTransformer mirrorTransformer) {
            buffer.writeDouble(mirrorTransformer.position().getX());
            buffer.writeDouble(mirrorTransformer.position().getY());
            buffer.writeDouble(mirrorTransformer.position().getZ());
            buffer.writeEnum(mirrorTransformer.axis());
        }

    }

    static class RadialTransformerSerializer extends BufferSerializer<RadialTransformer> {

        @Override
        public RadialTransformer read(Buffer buffer) {
            return new RadialTransformer(
                    new Vector3d(
                            buffer.readDouble(),
                            buffer.readDouble(),
                            buffer.readDouble()
                    ),
                    buffer.readInt()
            );
        }

        @Override
        public void write(Buffer buffer, RadialTransformer radialTransformer) {
            buffer.writeDouble(radialTransformer.position().getX());
            buffer.writeDouble(radialTransformer.position().getY());
            buffer.writeDouble(radialTransformer.position().getZ());
            buffer.writeInt(radialTransformer.slice());
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
            return ItemRandomizer.create(
                    buffer.readNullable(Buffer::readString),
                    buffer.readEnum(Randomizer.Order.class),
                    buffer.readEnum(Randomizer.Target.class),
                    buffer.readEnum(Randomizer.Category.class),
                    buffer.readCollection(buffer1 -> {
                        return Chance.itemStack(buffer1.readItemStack(), buffer1.readByte());
                    })
            );
        }

        @Override
        public void write(Buffer buffer, ItemRandomizer randomizer) {
            buffer.writeNullable(randomizer.getName(), Buffer::writeString);
            buffer.writeEnum(randomizer.getOrder());
            buffer.writeEnum(randomizer.getTarget());
            buffer.writeEnum(randomizer.getCategory());
            buffer.writeCollection(randomizer.getChances(), (buf, chance) -> {
                buf.writeItemStack(chance.content());
                buf.writeByte(chance.chance());
            });
        }
    }


}
