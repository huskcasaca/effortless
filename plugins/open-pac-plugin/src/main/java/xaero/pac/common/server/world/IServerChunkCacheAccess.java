package xaero.pac.common.server.world;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;

public interface IServerChunkCacheAccess {

	public <T> void addRegionTicket(ServerChunkCache serverChunkCache, TicketType<T> type, ChunkPos pos, int distance, T value, boolean forceTicks);
	public <T> void removeRegionTicket(ServerChunkCache serverChunkCache, TicketType<T> type, ChunkPos pos, int distance, T value, boolean forceTicks);

}
