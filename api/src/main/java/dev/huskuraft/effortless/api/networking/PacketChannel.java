package dev.huskuraft.effortless.api.networking;

import dev.huskuraft.effortless.api.core.ResourceLocation;

public interface PacketChannel extends PacketSender, PacketReceiver {

    int getCompatibilityVersion();

    ResourceLocation getChannelId();


}
