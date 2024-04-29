package dev.huskuraft.effortless.networking.packets.player;

import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.networking.Buffer;
import dev.huskuraft.effortless.api.networking.BufferSerializer;
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

    public static class Serializer implements BufferSerializer<PlayerHistoryResultPacket> {

        @Override
        public PlayerHistoryResultPacket read(Buffer buffer) {
            return new PlayerHistoryResultPacket(
                    new HistoryResult(
                            buffer.readEnum(HistoryResult.Type.class),
                            buffer.read(new ContextSerializer()),
                            buffer.readMap((buffer1) -> buffer1.readEnum(ItemSummaryType.class), (buffer1) -> buffer1.readList(Buffer::readItemStack)))
            );

        }

        @Override
        public void write(Buffer buffer, PlayerHistoryResultPacket packet) {
            buffer.writeEnum(packet.historyResult().type());
            buffer.write(packet.historyResult().context(), new ContextSerializer());
            buffer.writeMap(packet.historyResult().itemSummary(), Buffer::writeEnum, ((buffer1, itemStacks) -> buffer1.writeList(itemStacks, Buffer::writeItemStack)));
        }

    }

}
