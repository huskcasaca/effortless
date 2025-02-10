package dev.huskuraft.effortless.networking.serializer;

import dev.huskuraft.universal.api.core.GameMode;
import dev.huskuraft.universal.api.networking.NetByteBuf;
import dev.huskuraft.universal.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.building.BuildState;
import dev.huskuraft.effortless.building.BuildType;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.InventorySnapshot;
import dev.huskuraft.effortless.building.clipboard.Clipboard;
import dev.huskuraft.effortless.building.config.BuilderConfig;
import dev.huskuraft.effortless.building.operation.block.Extras;
import dev.huskuraft.effortless.building.pattern.Pattern;
import dev.huskuraft.effortless.building.replace.Replace;
import dev.huskuraft.effortless.building.replace.ReplaceStrategy;

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
                byteBuf.read(new ClipboardSerializer()),
                byteBuf.read(new PatternSerializer()),
                byteBuf.read(new ReplaceSerializer()),
                new Context.Configs(
                        byteBuf.read(new ConstraintConfigSerializer()),
                        byteBuf.read(new BuilderConfigSerializer())
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
        byteBuf.write(context.clipboard(), new ClipboardSerializer());
        byteBuf.write(context.pattern(), new PatternSerializer());
        byteBuf.write(context.replace(), new ReplaceSerializer());

        byteBuf.write(context.configs().constraintConfig(), new ConstraintConfigSerializer());
        byteBuf.write(context.configs().builderConfig(), new BuilderConfigSerializer());
        byteBuf.writeNullable(context.extras(), new ExtrasSerializer());


    }

    public static class ClipboardSerializer implements NetByteBufSerializer<Clipboard> {
        @Override
        public Clipboard read(NetByteBuf byteBuf) {
            return new Clipboard(
                    byteBuf.readBoolean(),
                    byteBuf.read(new SnapshotSerializer())
            );
        }

        @Override
        public void write(NetByteBuf byteBuf, Clipboard clipboard) {
            byteBuf.writeBoolean(clipboard.enabled());
            byteBuf.write(clipboard.snapshot(), new SnapshotSerializer());
        }
    }

    public static class PatternSerializer implements NetByteBufSerializer<Pattern> {
        @Override
        public Pattern read(NetByteBuf byteBuf) {
            return new Pattern(
                    byteBuf.readBoolean(),
                    byteBuf.readList(new TransformerSerializer())
            );
        }

        @Override
        public void write(NetByteBuf byteBuf, Pattern pattern) {
            byteBuf.writeBoolean(pattern.enabled());
            byteBuf.writeList(pattern.transformers(), new TransformerSerializer());
        }
    }

    public static class EntityStateSerializer implements NetByteBufSerializer<Extras> {
        @Override
        public Extras read(NetByteBuf byteBuf) {
            return new Extras(
                    byteBuf.read(new Vector3dSerializer()),
                    byteBuf.readFloat(),
                    byteBuf.readFloat()
            );
        }

        @Override
        public void write(NetByteBuf byteBuf, Extras extras) {
            byteBuf.write(extras.position(), new Vector3dSerializer());
            byteBuf.writeFloat(extras.rotationX());
            byteBuf.writeFloat(extras.rotationY());
        }
    }

    public static class InventorySnapshotSerializer implements NetByteBufSerializer<InventorySnapshot> {

        @Override
        public InventorySnapshot read(NetByteBuf byteBuf) {
            return new InventorySnapshot(
                    byteBuf.readList(NetByteBuf::readItemStack),
                    byteBuf.readVarInt(),
                    byteBuf.readVarInt(),
                    byteBuf.readVarInt(),
                    byteBuf.readVarInt(),
                    byteBuf.readVarInt());
        }

        @Override
        public void write(NetByteBuf byteBuf, InventorySnapshot inventorySnapshot) {
            byteBuf.writeList(inventorySnapshot.items(), NetByteBuf::writeItemStack);
            byteBuf.writeVarInt(inventorySnapshot.selected());
            byteBuf.writeVarInt(inventorySnapshot.bagSize());
            byteBuf.writeVarInt(inventorySnapshot.armorSize());
            byteBuf.writeVarInt(inventorySnapshot.offhandSize());
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
            byteBuf.write(extras.extras(), new EntityStateSerializer());
            byteBuf.writeEnum(extras.gameMode());
            byteBuf.writeLong(extras.seed());
            byteBuf.write(extras.inventorySnapshot(), new InventorySnapshotSerializer());
        }
    }

    public static class ReplaceSerializer implements NetByteBufSerializer<Replace> {
        @Override
        public Replace read(NetByteBuf byteBuf) {
            return new Replace(
                    byteBuf.readEnum(ReplaceStrategy.class),
                    byteBuf.readList(NetByteBuf::readItemStack),
                    byteBuf.readBoolean()
            );
        }

        @Override
        public void write(NetByteBuf byteBuf, Replace replace) {
            byteBuf.writeEnum(replace.replaceStrategy());
            byteBuf.writeList(replace.replaceList(), NetByteBuf::writeItemStack);
            byteBuf.writeBoolean(replace.isQuick());
        }

    }

    public static class BuilderConfigSerializer implements NetByteBufSerializer<BuilderConfig> {
        @Override
        public BuilderConfig read(NetByteBuf byteBuf) {
            return new BuilderConfig(
                    byteBuf.readVarInt(),
                    byteBuf.readBoolean()
            );
        }

        @Override
        public void write(NetByteBuf byteBuf, BuilderConfig builderConfig) {
            byteBuf.writeVarInt(builderConfig.reservedToolDurability());
            byteBuf.writeBoolean(builderConfig.passiveMode());
        }

    }
}
