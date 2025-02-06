package xaero.pac.common.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.server.ServerData;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class LazyPacketsConfirmationPacket {

	public static class Codec implements BiConsumer<LazyPacketsConfirmationPacket, FriendlyByteBuf>, Function<FriendlyByteBuf, LazyPacketsConfirmationPacket> {

		@Override
		public LazyPacketsConfirmationPacket apply(FriendlyByteBuf input) {
			try {
				if(input.readableBytes() > 2048)
					return null;
				input.readNbt(NbtAccounter.unlimitedHeap());
				return new LazyPacketsConfirmationPacket();
			} catch(Throwable t) {
				return null;
			}
		}

		@Override
		public void accept(LazyPacketsConfirmationPacket t, FriendlyByteBuf u) {
			CompoundTag tag = new CompoundTag();
			u.writeNbt(tag);
		}

	}

	public static class ServerHandler implements BiConsumer<LazyPacketsConfirmationPacket,ServerPlayer> {

		@Override
		public void accept(LazyPacketsConfirmationPacket t, ServerPlayer player) {
			ServerData.from(player.getServer()).getServerTickHandler().getLazyPacketSender().onConfirmation(player);
		}

	}

	public static class ClientHandler implements Consumer<LazyPacketsConfirmationPacket> {

		@Override
		public void accept(LazyPacketsConfirmationPacket t) {
			OpenPartiesAndClaims.INSTANCE.getPacketHandler().sendToServer(t);
		}

	}

}
