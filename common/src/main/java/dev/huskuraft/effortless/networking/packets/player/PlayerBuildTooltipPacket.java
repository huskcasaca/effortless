package dev.huskuraft.effortless.networking.packets.player;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.api.networking.Packet;
import dev.huskuraft.effortless.building.operation.BlockEntitySummary;
import dev.huskuraft.effortless.building.operation.BlockStateSummary;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.operation.OperationTooltip;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;
import dev.huskuraft.effortless.networking.serializer.ContextSerializer;

public record PlayerBuildTooltipPacket(
        OperationTooltip operationTooltip
) implements Packet<AllPacketListener> {


    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements NetByteBufSerializer<PlayerBuildTooltipPacket> {

        @Override
        public PlayerBuildTooltipPacket read(NetByteBuf byteBuf) {
            return new PlayerBuildTooltipPacket(
                    new OperationTooltip(
                            byteBuf.readEnum(OperationTooltip.Type.class),
                            byteBuf.read(new ContextSerializer()),
                            byteBuf.readMap((buffer1) -> buffer1.readEnum(BlockStateSummary.class), (buffer1) -> buffer1.readMap((NetByteBuf::readBlockState), NetByteBuf::readVarInt)),
                            byteBuf.readMap((buffer1) -> buffer1.readEnum(BlockEntitySummary.class), (buffer1) -> buffer1.readList((NetByteBuf::readItemStack)))
                    ));

        }

        @Override
        public void write(NetByteBuf byteBuf, PlayerBuildTooltipPacket packet) {
            byteBuf.writeEnum(packet.operationTooltip().type());
            byteBuf.write(packet.operationTooltip().context(), new ContextSerializer());
            byteBuf.writeMap(packet.operationTooltip().blockStateSummary(), NetByteBuf::writeEnum, ((buffer1, blockStateMap) -> buffer1.writeMap(blockStateMap, NetByteBuf::writeBlockState, NetByteBuf::writeVarInt)));
            byteBuf.writeMap(packet.operationTooltip().blockEntitySummary(), NetByteBuf::writeEnum, ((buffer1, blockStateMap) -> buffer1.writeList(blockStateMap, NetByteBuf::writeItemStack)));
        }

    }

    public static PlayerBuildTooltipPacket build(OperationResult operationResult) {
        return new PlayerBuildTooltipPacket(
                operationResult.getTooltip().withType(OperationTooltip.Type.BUILD)
        );
    }

    public static PlayerBuildTooltipPacket undo(OperationResult operationResult) {
        return new PlayerBuildTooltipPacket(
                operationResult.getTooltip().withType(OperationTooltip.Type.UNDO_SUCCESS)
        );
    }

    public static PlayerBuildTooltipPacket redo(OperationResult operationResult) {
        return new PlayerBuildTooltipPacket(
                operationResult.getTooltip().withType(OperationTooltip.Type.REDO_SUCCESS)
        );
    }

    public static PlayerBuildTooltipPacket nothingToUndo() {
        return new PlayerBuildTooltipPacket(
                OperationTooltip.empty(
                        OperationTooltip.Type.NOTHING_TO_UNDO
                )
        );
    }

    public static PlayerBuildTooltipPacket nothingToRedo() {
        return new PlayerBuildTooltipPacket(
                OperationTooltip.empty(
                        OperationTooltip.Type.NOTHING_TO_REDO
                )
        );
    }

}
