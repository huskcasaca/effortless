package dev.huskuraft.effortless.networking.packets.player;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.NetByteBuf;
import dev.huskuraft.effortless.api.networking.NetByteBufSerializer;
import dev.huskuraft.effortless.api.networking.Packet;
import dev.huskuraft.effortless.building.history.HistoryResult;
import dev.huskuraft.effortless.building.operation.ItemSummaryType;
import dev.huskuraft.effortless.networking.packets.AllPacketListener;
import dev.huskuraft.effortless.networking.serializer.ContextSerializer;

public record PlayerHistoryResultPacket(
        HistoryResult historyResult
) implements Packet<AllPacketListener> {


    @Override
    public void handle(AllPacketListener packetListener, Player sender) {
        packetListener.handle(this, sender);
    }

    public static class Serializer implements NetByteBufSerializer<PlayerHistoryResultPacket> {

        @Override
        public PlayerHistoryResultPacket read(NetByteBuf byteBuf) {
            return new PlayerHistoryResultPacket(
                    new HistoryResult(
                            byteBuf.readEnum(HistoryResult.Type.class),
                            byteBuf.read(new ContextSerializer()),
                            byteBuf.readMap((buffer1) -> buffer1.readEnum(ItemSummaryType.class), (buffer1) -> buffer1.readList(NetByteBuf::readItemStack)))
            );

        }

        @Override
        public void write(NetByteBuf byteBuf, PlayerHistoryResultPacket packet) {
            byteBuf.writeEnum(packet.historyResult().type());
            byteBuf.write(packet.historyResult().context(), new ContextSerializer());
            byteBuf.writeMap(packet.historyResult().itemSummary(), NetByteBuf::writeEnum, ((buffer1, itemStacks) -> buffer1.writeList(itemStacks, NetByteBuf::writeItemStack)));
        }

    }

}
