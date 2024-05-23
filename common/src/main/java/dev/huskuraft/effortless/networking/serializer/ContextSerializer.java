package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.effortless.api.core.GameMode;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.BuildType;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.InventorySnapshot;
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
                new Context.Interactions(
                        byteBuf.readList(buffer1 -> buffer1.readNullable(new BlockInteractionSerializer()))
                ),
                byteBuf.read(new StructureSerializer()),
                byteBuf.read(new PatternSerializer()),
                byteBuf.readEnum(ReplaceMode.class),
                new Context.Configs(
                        byteBuf.read(new ConstraintConfigSerializer())
                ),
                byteBuf.readNullable(new ExtrasSerializer())
        );
    }

    @Override
    public void write(NetByteBuf byteBuf, Context context) {
        byteBuf.writeUUID(context.id());
        byteBuf.writeEnum(context.buildState());
        byteBuf.writeEnum(context.buildType());
        byteBuf.writeList(context.interactions().results(), (buffer1, target) -> buffer1.writeNullable(target, new BlockInteractionSerializer()));

        byteBuf.write(context.structure(), new StructureSerializer());
        byteBuf.write(context.pattern(), new PatternSerializer());
        byteBuf.writeEnum(context.replaceMode());

        byteBuf.write(context.configs().constraintConfig(), new ConstraintConfigSerializer());
        byteBuf.writeNullable(context.extras(), new ExtrasSerializer());


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

    public static class InventorySnapshotSerializer implements NetByteBufSerializer<InventorySnapshot> {

        @Override
        public InventorySnapshot read(NetByteBuf byteBuf) {
            return new InventorySnapshot(
                    byteBuf.readList(NetByteBuf::readItemStack),
                    byteBuf.readList(NetByteBuf::readItemStack),
                    byteBuf.readList(NetByteBuf::readItemStack),
                    byteBuf.readVarInt(),
                    byteBuf.readVarInt()
            );
        }

        @Override
        public void write(NetByteBuf byteBuf, InventorySnapshot inventorySnapshot) {
            byteBuf.writeList(inventorySnapshot.items(), NetByteBuf::writeItemStack);
            byteBuf.writeList(inventorySnapshot.armorItems(), NetByteBuf::writeItemStack);
            byteBuf.writeList(inventorySnapshot.offhandItems(), NetByteBuf::writeItemStack);
            byteBuf.writeVarInt(inventorySnapshot.selected());
            byteBuf.writeVarInt(inventorySnapshot.hotbarSize());
        }
    }


    public static class ExtrasSerializer implements NetByteBufSerializer<Context.Extras> {

        @Override
        public Context.Extras read(NetByteBuf byteBuf) {
            return new Context.Extras(
                    byteBuf.readResourceLocation(),
                    byteBuf.read(new EntityStateSerializer()),
                    byteBuf.readEnum(GameMode.class),
                    byteBuf.readLong(),
                    byteBuf.read(new InventorySnapshotSerializer()));
        }

        @Override
        public void write(NetByteBuf byteBuf, Context.Extras extras) {
            byteBuf.writeResourceLocation(extras.dimensionId());
            byteBuf.write(extras.entityState(), new EntityStateSerializer());
            byteBuf.writeEnum(extras.gameMode());
            byteBuf.writeLong(extras.seed());
            byteBuf.write(extras.inventorySnapshot(), new InventorySnapshotSerializer());
        }
    }
}
