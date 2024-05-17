package dev.huskuraft.effortless.networking.packets.player;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.api.networking.Packet;
import dev.huskuraft.effortless.building.history.BuildTooltip;
import dev.huskuraft.effortless.building.operation.ItemSummaryType;
import dev.huskuraft.effortless.building.operation.OperationResult;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;
import dev.huskuraft.effortless.networking.serializer.ContextSerializer;

public record PlayerBuildTooltipPacket(
        BuildTooltip buildTooltip
) implements Packet<AllPacketListener> {


    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements NetByteBufSerializer<PlayerBuildTooltipPacket> {

        @Override
        public PlayerBuildTooltipPacket read(NetByteBuf byteBuf) {
            return new PlayerBuildTooltipPacket(
                    new BuildTooltip(
                            byteBuf.readEnum(BuildTooltip.Type.class),
                            byteBuf.read(new ContextSerializer()),
                            byteBuf.readMap((buffer1) -> buffer1.readEnum(ItemSummaryType.class), (buffer1) -> buffer1.readList(NetByteBuf::readItemStack)))
            );

        }

        @Override
        public void write(NetByteBuf byteBuf, PlayerBuildTooltipPacket packet) {
            byteBuf.writeEnum(packet.buildTooltip().type());
            byteBuf.write(packet.buildTooltip().context(), new ContextSerializer());
            byteBuf.writeMap(packet.buildTooltip().itemSummary(), NetByteBuf::writeEnum, ((buffer1, itemStacks) -> buffer1.writeList(itemStacks, NetByteBuf::writeItemStack)));
        }

    }

    public static PlayerBuildTooltipPacket buildSuccess(OperationResult operationResult) {
        return new PlayerBuildTooltipPacket(
                new BuildTooltip(
                        BuildTooltip.Type.BUILD_SUCCESS,
                        operationResult
                )
        );
    }

    public static PlayerBuildTooltipPacket undoSuccess(OperationResult operationResult) {
        return new PlayerBuildTooltipPacket(
                new BuildTooltip(
                        BuildTooltip.Type.UNDO_SUCCESS,
                        operationResult
                )
        );
    }

    public static PlayerBuildTooltipPacket redoSuccess(OperationResult operationResult) {
        return new PlayerBuildTooltipPacket(
                new BuildTooltip(
                        BuildTooltip.Type.REDO_SUCCESS,
                        operationResult
                )
        );
    }

    public static PlayerBuildTooltipPacket nothingToUndo() {
        return new PlayerBuildTooltipPacket(
                new BuildTooltip(
                        BuildTooltip.Type.NOTHING_TO_UNDO
                )
        );
    }

    public static PlayerBuildTooltipPacket nothingToRedo() {
        return new PlayerBuildTooltipPacket(
                new BuildTooltip(
                        BuildTooltip.Type.NOTHING_TO_REDO
                )
        );
    }

}
