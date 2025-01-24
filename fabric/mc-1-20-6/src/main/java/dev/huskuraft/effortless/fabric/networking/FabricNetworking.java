package dev.huskuraft.effortless.fabric.networking;

import com.google.auto.service.AutoService;
import dev.huskuraft.effortless.api.core.ResourceLocation;
import dev.huskuraft.effortless.api.networking.ByteBufReceiver;
import dev.huskuraft.effortless.api.networking.ByteBufSender;
import dev.huskuraft.effortless.api.networking.Networking;
import dev.huskuraft.effortless.api.networking.Side;

@AutoService(Networking.class)
public class FabricNetworking implements Networking {

    public static ByteBufSender register(ResourceLocation channelId, Side side, ByteBufReceiver receiver) {
        var fabricSide = FabricSide.of(side);
        var type = new FabricPayload.Type<FabricPayload>(channelId.reference());
        var codec = new FabricPayloadStreamCodec(channelId);
        fabricSide.registerPayload(type, codec);
        fabricSide.registerReceiver(type, (payload, player) -> receiver.receiveBuffer(payload.byteBuf(), player));
        return (byteBuf, player) -> fabricSide.send(new FabricPayload(channelId, byteBuf), player);
    }

}
