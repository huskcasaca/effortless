package dev.ftb.mods.ftbteams.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.ftb.mods.ftbteams.api.TeamManager;
import net.minecraft.nbt.CompoundTag;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class TeamManagerEvent {
	public static final Event<Consumer<TeamManagerEvent>> CREATED = EventFactory.createConsumerLoop(TeamManagerEvent.class);
	public static final Event<Consumer<TeamManagerEvent>> LOADED = EventFactory.createConsumerLoop(TeamManagerEvent.class);
	public static final Event<Consumer<TeamManagerEvent>> SAVED = EventFactory.createConsumerLoop(TeamManagerEvent.class);
	public static final Event<Consumer<TeamManagerEvent>> DESTROYED = EventFactory.createConsumerLoop(TeamManagerEvent.class);

	private final TeamManager manager;

	public TeamManagerEvent(TeamManager t) {
		manager = t;
	}

	public TeamManager getManager() {
		return manager;
	}

	public CompoundTag getExtraData() {
		return manager.getExtraData();
	}
}