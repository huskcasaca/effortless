package xaero.pac.common.server.task;

import xaero.pac.common.claims.player.IPlayerChunkClaim;
import xaero.pac.common.claims.player.IPlayerClaimPosList;
import xaero.pac.common.claims.player.IPlayerDimensionClaims;
import xaero.pac.common.parties.party.IPartyPlayerInfo;
import xaero.pac.common.parties.party.ally.IPartyAlly;
import xaero.pac.common.parties.party.member.IPartyMember;
import xaero.pac.common.server.IServerData;
import xaero.pac.common.server.claims.IServerClaimsManager;
import xaero.pac.common.server.claims.IServerDimensionClaimsManager;
import xaero.pac.common.server.claims.IServerRegionClaims;
import xaero.pac.common.server.claims.player.IServerPlayerClaimInfo;
import xaero.pac.common.server.parties.party.IServerParty;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public final class ServerSpreadoutQueuedTaskHandler<T extends IServerSpreadoutQueuedTask<T>> extends ServerSpreadoutTaskHandler<T, T> {
	//task and task holder are the same in this case

	private final Deque<T> taskQueue;

	private ServerSpreadoutQueuedTaskHandler(Deque<T> tasks, Function<T, T> holderToTask, int perTickLimit, int perTickPerTaskLimit) {
		super(holderToTask, perTickLimit, perTickPerTaskLimit);
		this.taskQueue = tasks;
	}

	public boolean addTask(T task, IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> serverData){
		if(taskQueue.add(task)) {
			task.onQueued(serverData);
			return true;
		}
		return false;
	}

	@Override
	protected Iterator<T> getTaskHolderIterator(IServerData<?, ?> serverData) {
		return taskQueue.iterator();
	}

	@Override
	protected void handleTasksToAdd(List<T> tasksToAdd) {
		taskQueue.addAll(tasksToAdd);
	}

	public static final class Builder<T extends IServerSpreadoutQueuedTask<T>> extends ServerSpreadoutTaskHandler.Builder<T, T, Builder<T>> {

		private Builder(){}

		@Override
		public Builder<T> setDefault() {
			super.setDefault();
			return self;
		}

		@Override
		public Builder<T> setHolderToTask(Function<T, T> holderToTask) {
			if(holderToTask != null)
				throw new RuntimeException(new IllegalAccessException());
			return super.setHolderToTask(holderToTask);
		}

		@Override
		public ServerSpreadoutQueuedTaskHandler<T> build() {
			holderToTask = Function.<T>identity();
			return (ServerSpreadoutQueuedTaskHandler<T>) super.build();
		}

		@Override
		protected ServerSpreadoutTaskHandler<T, T> buildInternally() {
			return new ServerSpreadoutQueuedTaskHandler<>(new LinkedList<>(), holderToTask, perTickLimit, perTickPerTaskLimit);
		}

		public static <T extends IServerSpreadoutQueuedTask<T>> Builder<T> begin(){
			return new Builder<T>().setDefault();
		}

	}

}
