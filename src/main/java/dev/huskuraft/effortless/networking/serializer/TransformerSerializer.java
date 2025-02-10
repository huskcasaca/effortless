package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.universal.api.core.Axis;
import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.building.pattern.Transformer;
import dev.huskuraft.effortless.building.pattern.Transformers;
import dev.huskuraft.effortless.building.pattern.array.ArrayTransformer;
import dev.huskuraft.effortless.building.pattern.mirror.MirrorTransformer;
import dev.huskuraft.effortless.building.pattern.raidal.RadialTransformer;
import dev.huskuraft.effortless.building.pattern.randomize.Chance;
import dev.huskuraft.effortless.building.pattern.randomize.ItemRandomizer;

public class TransformerSerializer implements NetByteBufSerializer<Transformer> {

    @Override
    public Transformer read(NetByteBuf byteBuf) {
        return switch (byteBuf.readEnum(Transformers.class)) {
            case ARRAY -> byteBuf.read(new ArrayTransformerSerializer());
            case MIRROR -> byteBuf.read(new MirrorTransformerSerializer());
            case RADIAL -> byteBuf.read(new RadialTransformerSerializer());
            case RANDOMIZER -> byteBuf.read(new ItemRandomizerSerializer());
        };
    }

    @Override
    public void write(NetByteBuf byteBuf, Transformer transformer) {
        byteBuf.writeEnum(transformer.getType());
        switch (transformer.getType()) {
            case ARRAY -> byteBuf.write((ArrayTransformer) transformer, new ArrayTransformerSerializer());
            case MIRROR -> byteBuf.write((MirrorTransformer) transformer, new MirrorTransformerSerializer());
            case RADIAL -> byteBuf.write((RadialTransformer) transformer, new RadialTransformerSerializer());
            case RANDOMIZER -> byteBuf.write((ItemRandomizer) transformer, new ItemRandomizerSerializer());
        }
    }

    static class ArrayTransformerSerializer implements NetByteBufSerializer<ArrayTransformer> {

        @Override
        public ArrayTransformer read(NetByteBuf byteBuf) {
            return new ArrayTransformer(
                    byteBuf.readUUID(),
                    byteBuf.readText(),
                    byteBuf.readVector3i(),
                    byteBuf.readVarInt()
            );
        }

        @Override
        public void write(NetByteBuf byteBuf, ArrayTransformer transformer) {
            byteBuf.writeUUID(transformer.getId());
            byteBuf.writeText(transformer.getName());
            byteBuf.writeVector3i(transformer.offset());
            byteBuf.writeVarInt(transformer.count());
        }

    }

    static class MirrorTransformerSerializer implements NetByteBufSerializer<MirrorTransformer> {

        @Override
        public MirrorTransformer read(NetByteBuf byteBuf) {
            return new MirrorTransformer(
                    byteBuf.readUUID(),
                    byteBuf.readText(),
                    byteBuf.readVector3d(),
                    byteBuf.readEnum(Axis.class),
                    byteBuf.readVarInt()
            );
        }

        @Override
        public void write(NetByteBuf byteBuf, MirrorTransformer transformer) {
            byteBuf.writeUUID(transformer.getId());
            byteBuf.writeText(transformer.getName());
            byteBuf.writeVector3d(transformer.position());
            byteBuf.writeEnum(transformer.axis());
            byteBuf.writeVarInt(transformer.size());
        }

    }

    static class RadialTransformerSerializer implements NetByteBufSerializer<RadialTransformer> {

        @Override
        public RadialTransformer read(NetByteBuf byteBuf) {
            return new RadialTransformer(
                    byteBuf.readUUID(),
                    byteBuf.readText(),
                    byteBuf.readVector3d(),
                    byteBuf.readEnum(Axis.class),
                    byteBuf.readVarInt(),
                    byteBuf.readVarInt(),
                    byteBuf.readVarInt());
        }

        @Override
        public void write(NetByteBuf byteBuf, RadialTransformer transformer) {
            byteBuf.writeUUID(transformer.getId());
            byteBuf.writeText(transformer.getName());
            byteBuf.writeVector3d(transformer.position());
            byteBuf.writeEnum(transformer.axis());
            byteBuf.writeVarInt(transformer.slices());
            byteBuf.writeVarInt(transformer.radius());
            byteBuf.writeVarInt(transformer.length());
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

    static class ItemRandomizerSerializer implements NetByteBufSerializer<ItemRandomizer> {

        @Override
        public ItemRandomizer read(NetByteBuf byteBuf) {
            return new ItemRandomizer(
                    byteBuf.readUUID(),
                    byteBuf.readText(),
                    byteBuf.readEnum(ItemRandomizer.Order.class),
                    byteBuf.readEnum(ItemRandomizer.Target.class),
                    byteBuf.readEnum(ItemRandomizer.Source.class),
                    byteBuf.readList(buffer1 -> {
                        return Chance.of(buffer1.readItem(), buffer1.readVarInt());
                    })
            );
        }

        @Override
        public void write(NetByteBuf byteBuf, ItemRandomizer transformer) {
            byteBuf.writeUUID(transformer.getId());
            byteBuf.writeText(transformer.getName());
            byteBuf.writeEnum(transformer.getOrder());
            byteBuf.writeEnum(transformer.getTarget());
            byteBuf.writeEnum(transformer.getSource());
            byteBuf.writeList(transformer.getChances(), (buf, chance) -> {
                buf.writeItem(chance.content());
                buf.writeVarInt(chance.chance());
            });
        }
    }


}
