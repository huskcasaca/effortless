package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.BuildType;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.operation.block.EntityState;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.replace.ReplaceMode;

public class ContextSerializer implements NetByteBufSerializer<Context> {

    @Override
    public Context read(NetByteBuf byteBuf) {
        return new Context(
                byteBuf.readUUID(),
                byteBuf.readEnum(BuildState.class),
                byteBuf.readEnum(BuildType.class),
                new Context.BuildInteractions(
                        byteBuf.readList(buffer1 -> buffer1.readNullable(new BlockInteractionSerializer()))
                ),
                new Context.StructureParams(
                        byteBuf.read(new BuildStructureSerializer()),
                        byteBuf.readEnum(ReplaceMode.class)),
                new Context.PatternParams(
                        byteBuf.readNullable(new PatternSerializer()),
                        byteBuf.readNullable(new EntityStateSerializer()),
                        byteBuf.readNullable(new EntityStateSerializer()),
                        byteBuf.readBoolean(), byteBuf.readLong()),
                new Context.CustomParams(
                        byteBuf.read(new GeneralConfigSerializer())
                )
        );
    }

    @Override
    public void write(NetByteBuf byteBuf, Context context) {
        byteBuf.writeUUID(context.id());
        byteBuf.writeEnum(context.buildState());
        byteBuf.writeEnum(context.buildType());
        byteBuf.writeList(context.buildInteractions().results(), (buffer1, target) -> buffer1.writeNullable(target, new BlockInteractionSerializer()));

        byteBuf.write(context.structureParams().buildStructure(), new BuildStructureSerializer());
        byteBuf.writeEnum(context.structureParams().replaceMode());

        byteBuf.writeNullable(context.patternParams().pattern(), new PatternSerializer());
        byteBuf.writeNullable(context.patternParams().activeState(), new EntityStateSerializer());
        byteBuf.writeNullable(context.patternParams().interactState(), new EntityStateSerializer());

        byteBuf.writeBoolean(context.patternParams().limitedProducer());
        byteBuf.writeLong(context.patternParams().seed());

        byteBuf.write(context.customParams().generalConfig(), new GeneralConfigSerializer());
    }

    public static class PatternSerializer implements NetByteBufSerializer<Pattern> {
        @Override
        public Pattern read(NetByteBuf byteBuf) {
            return new Pattern(
                    byteBuf.readUUID(),
                    byteBuf.readBoolean(),
                    byteBuf.readList(new TransformerSerializer())
            );
        }

        @Override
        public void write(NetByteBuf byteBuf, Pattern pattern) {
            byteBuf.writeUUID(pattern.id());
            byteBuf.writeBoolean(pattern.enabled());
            byteBuf.writeList(pattern.transformers(), new TransformerSerializer());
        }
    }

    public static class EntityStateSerializer implements NetByteBufSerializer<EntityState> {
        @Override
        public EntityState read(NetByteBuf byteBuf) {
            return new EntityState(
                    byteBuf.read(new Vector3dSerializer()),
                    byteBuf.readFloat(),
                    byteBuf.readFloat()
            );
        }

        @Override
        public void write(NetByteBuf byteBuf, EntityState entityState) {
            byteBuf.write(entityState.position(), new Vector3dSerializer());
            byteBuf.writeFloat(entityState.rotationX());
            byteBuf.writeFloat(entityState.rotationY());
        }
    }

}
