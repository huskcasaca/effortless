package xaero.pac.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface IPacketHandler {

	public <P> void register(int index, Class<P> type,
							 BiConsumer<P, FriendlyByteBuf> encoder,
							 Function<FriendlyByteBuf, P> decoder,
							 BiConsumer<P, ServerPlayer> serverHandler,
							 Consumer<P> clientHandler);

	public <T> void sendToServer(T packet);

	public <T> void sendToPlayer(ServerPlayer player, T packet);

}
