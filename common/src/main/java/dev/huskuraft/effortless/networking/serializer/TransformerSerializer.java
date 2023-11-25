package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.building.pattern.array.Array;
import dev.huskuraft.effortless.building.pattern.mirror.Mirror;
import dev.huskuraft.effortless.building.pattern.raidal.Radial;
import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;
import dev.huskuraft.effortless.building.pattern.randomize.Randomizer;
import dev.huskuraft.effortless.core.Axis;
import dev.huskuraft.effortless.math.Vector3d;
import dev.huskuraft.effortless.networking.Buffer;
import dev.huskuraft.effortless.networking.BufferSerializer;

import java.util.Objects;

public class TransformerSerializer extends BufferSerializer<Transformer> {

    @Override
    public Transformer read(Buffer buffer) {
        return switch (buffer.readEnum(Transformers.class)) {
            case ARRAY -> buffer.read(new ArraySerializer());
            case MIRROR -> buffer.read(new MirrorSerializer());
            case RADIAL -> buffer.read(new RadialSerializer());
            case ITEM_RANDOM -> buffer.read(new ItemRandomizerSerializer());
        };
    }

    @Override
    public void write(Buffer buffer, Transformer transformer) {
        buffer.writeEnum(transformer.getType());
        switch (transformer.getType()) {
            case ARRAY -> buffer.write((Array) transformer, new ArraySerializer());
            case MIRROR -> buffer.write((Mirror) transformer, new MirrorSerializer());
            case RADIAL -> buffer.write((Radial) transformer, new RadialSerializer());
            case ITEM_RANDOM -> buffer.write((ItemRandomizer) transformer, new ItemRandomizerSerializer());
        }
    }

    static class ArraySerializer extends BufferSerializer<Array> {

        @Override
        public Array read(Buffer buffer) {
            return new Array(
                    buffer.readDouble(),
                    buffer.readDouble(),
                    buffer.readDouble(),
                    buffer.readInt()
            );
        }

        @Override
        public void write(Buffer buffer, Array array) {
            buffer.writeDouble(array.x());
            buffer.writeDouble(array.y());
            buffer.writeDouble(array.z());
            buffer.writeInt(array.count());
        }

    }

    static class MirrorSerializer extends BufferSerializer<Mirror> {

        @Override
        public Mirror read(Buffer buffer) {
            return new Mirror(
                    new Vector3d(
                            buffer.readDouble(),
                            buffer.readDouble(),
                            buffer.readDouble()
                    ),
                    buffer.readEnum(Axis.class)
            );
        }

        @Override
        public void write(Buffer buffer, Mirror mirror) {
            buffer.writeDouble(mirror.position().getX());
            buffer.writeDouble(mirror.position().getY());
            buffer.writeDouble(mirror.position().getZ());
            buffer.writeEnum(mirror.axis());
        }

    }

    static class RadialSerializer extends BufferSerializer<Radial> {

        @Override
        public Radial read(Buffer buffer) {
            return new Radial(
                    new Vector3d(
                            buffer.readDouble(),
                            buffer.readDouble(),
                            buffer.readDouble()
                    ),
                    buffer.readInt()
            );
        }

        @Override
        public void write(Buffer buffer, Radial radial) {
            buffer.writeDouble(radial.position().getX());
            buffer.writeDouble(radial.position().getY());
            buffer.writeDouble(radial.position().getZ());
            buffer.writeInt(radial.slice());
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
