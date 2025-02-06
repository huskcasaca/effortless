package xaero.pac.common.server.lazypacket;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class LazyPacket<P extends LazyPacket<P>> {

	private final FriendlyByteBuf data;
	private boolean prepared;

	public LazyPacket() {
		data = new FriendlyByteBuf(Unpooled.buffer());
	}

	protected abstract Function<FriendlyByteBuf, P> getDecoder();

	public int getPreparedSize() {
		if(!prepared)
			throw new IllegalStateException("Lazy packet has not been prepared!");
		return data.writerIndex();
	}

	public int prepare() {
		if(prepared)
			return data.writerIndex();
		data.clear();
		writeOnPrepare(data);
		prepared = true;
		return data.writerIndex();
	}

	protected abstract void writeOnPrepare(FriendlyByteBuf dest);

	public static class Encoder<P extends LazyPacket<P>> implements BiConsumer<P, FriendlyByteBuf> {

		@Override
		public void accept(P t, FriendlyByteBuf u) {
			LazyPacket<P> lazyPacket = t;
			if(!lazyPacket.prepared)
				throw new IllegalStateException("Lazy packet has not been prepared!");
			u.writeBytes(lazyPacket.data, 0, lazyPacket.data.writerIndex());
		}

	}

	public static abstract class Handler<P extends LazyPacket<P>> implements Consumer<P> {

		protected abstract void handle(P t);

		@Override
		public void accept(P t) {
			LazyPacket<P> lazyPacket = t;
			if(lazyPacket.prepared) {
				//was directly passed in singleplayer without encoding
				//a lot of references in packets become unusable after preparation, so we need to always decode prepared data
				lazyPacket.data.readerIndex(0);//may have been read before (when reusing packet objects)
				P decoded = lazyPacket.getDecoder().apply(lazyPacket.data);
				handle(decoded);
				return;
			}
			handle(t);
		}

	}

}
