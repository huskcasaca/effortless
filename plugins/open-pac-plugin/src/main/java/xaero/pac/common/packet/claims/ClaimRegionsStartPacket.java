package xaero.pac.common.packet.claims;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.server.lazypacket.LazyPacket;
import xaero.pac.common.server.player.data.ServerPlayerData;
import xaero.pac.common.server.player.data.api.ServerPlayerDataAPI;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ClaimRegionsStartPacket extends LazyPacket<ClaimRegionsStartPacket> {

	public static final Encoder<ClaimRegionsStartPacket> ENCODER = new Encoder<>();
	public static final Decoder DECODER = new Decoder();

	public ClaimRegionsStartPacket() {
		super();
	}

	@Override
	protected void writeOnPrepare(FriendlyByteBuf u) {
		CompoundTag tag = new CompoundTag();
		u.writeNbt(tag);
	}

	@Override
	protected Function<FriendlyByteBuf, ClaimRegionsStartPacket> getDecoder() {
		return DECODER;
	}

	public static class Decoder implements Function<FriendlyByteBuf, ClaimRegionsStartPacket> {

		@Override
		public ClaimRegionsStartPacket apply(FriendlyByteBuf input) {
			try {
				if(input.readableBytes() > 2048)
					return null;
				input.readNbt(NbtAccounter.unlimitedHeap());
				return new ClaimRegionsStartPacket();
			} catch(Throwable t) {
				return null;
			}
		}

	}

	public static class ServerHandler implements BiConsumer<ClaimRegionsStartPacket,ServerPlayer> {

		@Override
		public void accept(ClaimRegionsStartPacket t, ServerPlayer serverPlayer) {
			ServerPlayerData mainCap = (ServerPlayerData) ServerPlayerDataAPI.from(serverPlayer);
			mainCap.getClaimsManagerPlayerClaimOwnerPropertiesSync().start(serverPlayer);
			mainCap.getClaimsManagerPlayerSubClaimPropertiesSync().start(serverPlayer);
			mainCap.getClaimsManagerPlayerStateSync().start(serverPlayer);
			mainCap.getClaimsManagerPlayerRegionSync().start(serverPlayer);
		}

	}

	public static class ClientHandler extends Handler<ClaimRegionsStartPacket> {

		@Override
		public void handle(ClaimRegionsStartPacket t) {
			t.prepare();
			OpenPartiesAndClaims.INSTANCE.getPacketHandler().sendToServer(t);
		}

	}

}
