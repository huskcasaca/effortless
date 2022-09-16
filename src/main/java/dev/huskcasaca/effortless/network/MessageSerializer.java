package dev.huskcasaca.effortless.network;

import net.minecraft.network.FriendlyByteBuf;

public interface MessageSerializer<M extends Message> {

    void encode(M message, FriendlyByteBuf buf);

    M decode(FriendlyByteBuf buf);


}
