package dev.huskuraft.effortless.networking.packets.player;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.api.networking.Packet;
import dev.huskuraft.effortless.building.operation.ItemSummary;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.building.operation.OperationTooltip;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;
import dev.huskuraft.effortless.networking.serializer.ContextSerializer;

public record PlayerOperationTooltipPacket(
        OperationTooltip operationTooltip
) implements Packet<AllPacketListener> {


    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements NetByteBufSerializer<PlayerOperationTooltipPacket> {

        @Override
        public PlayerOperationTooltipPacket read(NetByteBuf byteBuf) {
            return new PlayerOperationTooltipPacket(
                    new OperationTooltip(
                            byteBuf.readEnum(OperationTooltip.Type.class),
                            byteBuf.read(new ContextSerializer()),
                            byteBuf.readMap((buffer1) -> buffer1.readEnum(ItemSummary.class), (buffer1) -> buffer1.readList((NetByteBuf::readItemStack)))
                    ));

        }

        @Override
        public void write(NetByteBuf byteBuf, PlayerOperationTooltipPacket packet) {
            byteBuf.writeEnum(packet.operationTooltip().type());
            byteBuf.write(packet.operationTooltip().context(), new ContextSerializer());
            byteBuf.writeMap(packet.operationTooltip().itemSummary(), NetByteBuf::writeEnum, ((buffer1, blockStateMap) -> buffer1.writeList(blockStateMap, NetByteBuf::writeItemStack)));
        }

    }

    public static PlayerOperationTooltipPacket build(OperationResult operationResult) {
        return new PlayerOperationTooltipPacket(
                operationResult.getTooltip().withType(OperationTooltip.Type.BUILD)
        );
    }

    public static PlayerOperationTooltipPacket undo(OperationResult operationResult) {
        return new PlayerOperationTooltipPacket(
                operationResult.getTooltip().withType(OperationTooltip.Type.UNDO_SUCCESS)
        );
    }

    public static PlayerOperationTooltipPacket redo(OperationResult operationResult) {
        return new PlayerOperationTooltipPacket(
                operationResult.getTooltip().withType(OperationTooltip.Type.REDO_SUCCESS)
        );
    }

    public static PlayerOperationTooltipPacket nothingToUndo() {
        return new PlayerOperationTooltipPacket(
                OperationTooltip.empty(
                        OperationTooltip.Type.NOTHING_TO_UNDO
                )
        );
    }

    public static PlayerOperationTooltipPacket nothingToRedo() {
        return new PlayerOperationTooltipPacket(
                OperationTooltip.empty(
                        OperationTooltip.Type.NOTHING_TO_REDO
                )
        );
    }

}
