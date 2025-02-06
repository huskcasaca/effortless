package xaero.pac.common.server.task.player;

import net.minecraft.server.level.ServerPlayer;
import xaero.pac.common.server.IServerData;
import xaero.pac.common.server.player.data.ServerPlayerData;
import xaero.pac.common.server.task.ServerSpreadoutTaskHandler;

import java.util.Iterator;
import java.util.function.Function;

public class ServerPlayerSpreadoutTaskHandler<T extends ServerPlayerSpreadoutTask<T>> extends ServerSpreadoutTaskHandler<T, ServerPlayer> {

	protected ServerPlayerSpreadoutTaskHandler(Function<ServerPlayer, T> holderToTask, int perTickLimit, int perTickPerTaskLimit) {
		super(holderToTask, perTickLimit, perTickPerTaskLimit);
	}

	@Override
	protected Iterator<ServerPlayer> getTaskHolderIterator(IServerData<?, ?> serverData) {
		return serverData.getServer().getPlayerList().getPlayers().iterator();
	}

	@Override
	protected final boolean canDropTasks() {
		//can't be removing players
		return false;
	}

	public static abstract class Builder<T extends ServerPlayerSpreadoutTask<T>, B extends Builder<T, B>> extends ServerSpreadoutTaskHandler.Builder<T, ServerPlayer, B> {

		private Function<ServerPlayerData, T> playerTaskGetter;

		protected Builder(){
		}

		@Override
		public B setDefault() {
			super.setDefault();
			setPlayerTaskGetter(null);
			return self;
		}

		@Override
		public final B setHolderToTask(Function<ServerPlayer, T> holderToTask) {
			if(holderToTask != null)
				throw new RuntimeException(new IllegalAccessException());
			return super.setHolderToTask(holderToTask);
		}

		public B setPlayerTaskGetter(Function<ServerPlayerData, T> playerTaskGetter) {
			this.playerTaskGetter = playerTaskGetter;
			return self;
		}

		@Override
		public ServerPlayerSpreadoutTaskHandler<T> build() {
			if(playerTaskGetter == null)
				throw new IllegalStateException();
			holderToTask = p -> {
				ServerPlayerData data = (ServerPlayerData) ServerPlayerData.from(p);
				return playerTaskGetter.apply(data);
			};
			return (ServerPlayerSpreadoutTaskHandler<T>) super.build();
		}
	}

	public static final class FinalBuilder<T extends ServerPlayerSpreadoutTask<T>> extends Builder<T, FinalBuilder<T>> {

		@Override
		protected ServerSpreadoutTaskHandler<T, ServerPlayer> buildInternally() {
			return new ServerPlayerSpreadoutTaskHandler<>(holderToTask, perTickLimit, perTickPerTaskLimit);
		}

		public static <T extends ServerPlayerSpreadoutTask<T>> FinalBuilder<T> begin(){
			return new FinalBuilder<T>().setDefault();
		}

	}

}
