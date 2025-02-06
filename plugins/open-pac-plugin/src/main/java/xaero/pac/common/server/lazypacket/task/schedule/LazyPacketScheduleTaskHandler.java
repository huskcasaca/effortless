package xaero.pac.common.server.lazypacket.task.schedule;

import net.minecraft.server.level.ServerPlayer;
import xaero.pac.common.server.task.ServerSpreadoutTaskHandler;
import xaero.pac.common.server.task.player.ServerPlayerSpreadoutTaskHandler;

import java.util.function.Function;

public final class LazyPacketScheduleTaskHandler extends ServerPlayerSpreadoutTaskHandler<LazyPacketScheduleTask> {

	private LazyPacketScheduleTaskHandler(Function<ServerPlayer, LazyPacketScheduleTask> holderToTask, int perTickLimit, int perTickPerTaskLimit) {
		super(holderToTask, perTickLimit, perTickPerTaskLimit);
	}

	public void onLazyPacketsDropped(ServerPlayer player){
		holderToTask.apply(player).onLazyPacketsDropped();
	}

	public static final class Builder extends ServerPlayerSpreadoutTaskHandler.Builder<LazyPacketScheduleTask, Builder> {

		@Override
		public LazyPacketScheduleTaskHandler build() {
			return (LazyPacketScheduleTaskHandler) super.build();
		}

		@Override
		protected ServerSpreadoutTaskHandler<LazyPacketScheduleTask, ServerPlayer> buildInternally() {
			return new LazyPacketScheduleTaskHandler(holderToTask, perTickLimit, perTickPerTaskLimit);
		}

		public static Builder begin(){
			return new Builder().setDefault();
		}
	}

}
