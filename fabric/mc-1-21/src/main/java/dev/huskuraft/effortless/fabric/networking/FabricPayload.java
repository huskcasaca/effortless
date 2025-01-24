package dev.huskuraft.effortless.fabric.networking;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record FabricPayload(ResourceLocation channelId, ByteBuf byteBuf) implements CustomPacketPayload {
    @Override
    public Type<FabricPayload> type() {
        return new Type<>(channelId.reference());
    }
}
