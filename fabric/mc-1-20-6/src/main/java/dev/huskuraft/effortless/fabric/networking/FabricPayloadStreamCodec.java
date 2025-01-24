package dev.huskuraft.effortless.fabric.networking;

import dev.huskuraft.effortless.api.core.ResourceLocation;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record FabricPayloadStreamCodec(
        ResourceLocation channelId
) implements StreamCodec<RegistryFriendlyByteBuf, FabricPayload> {
    @Override
    public FabricPayload decode(RegistryFriendlyByteBuf byteBuf) {
        return new FabricPayload(channelId, byteBuf.readBytes(byteBuf.readableBytes()));
    }

    @Override
    public void encode(RegistryFriendlyByteBuf byteBuf, FabricPayload payload) {
        byteBuf.writeBytes(payload.byteBuf().readBytes(payload.byteBuf().readableBytes()));
    }
}
