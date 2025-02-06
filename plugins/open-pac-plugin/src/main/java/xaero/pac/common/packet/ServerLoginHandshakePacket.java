package xaero.pac.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.server.player.data.ServerPlayerData;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ServerLoginHandshakePacket {

	public ServerLoginHandshakePacket() {
		super();
	}

	public static class Codec implements BiConsumer<ServerLoginHandshakePacket, FriendlyByteBuf>, Function<FriendlyByteBuf, ServerLoginHandshakePacket> {

		@Override
		public ServerLoginHandshakePacket apply(FriendlyByteBuf input) {
			return new ServerLoginHandshakePacket();
		}

		@Override
		public void accept(ServerLoginHandshakePacket t, FriendlyByteBuf u) {
		}

	}

	public static class ClientHandler implements Consumer<ServerLoginHandshakePacket> {

		@Override
		public void accept(ServerLoginHandshakePacket t) {
			OpenPartiesAndClaims.INSTANCE.getClientDataInternal().reset();
			OpenPartiesAndClaims.INSTANCE.getPacketHandler().sendToServer(t);
		}

	}

	public static class ServerHandler implements BiConsumer<ServerLoginHandshakePacket, ServerPlayer> {

		@Override
		public void accept(ServerLoginHandshakePacket t, ServerPlayer player) {
			((ServerPlayerData)ServerPlayerData.from(player)).setHasMod(true);
		}

	}

}
