package dev.ftb.mods.ftbteams;

import com.mojang.brigadier.CommandDispatcher;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.event.TeamCollectPropertiesEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import dev.ftb.mods.ftbteams.api.event.TeamManagerEvent;
import dev.ftb.mods.ftbteams.api.property.TeamProperties;
import dev.ftb.mods.ftbteams.client.FTBTeamsClient;
import dev.ftb.mods.ftbteams.data.FTBTeamsCommands;
import dev.ftb.mods.ftbteams.data.TeamManagerImpl;
import dev.ftb.mods.ftbteams.net.FTBTeamsNet;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FTBTeams {
	public static final Logger LOGGER = LogManager.getLogger(FTBTeamsAPI.MOD_NAME);

	public FTBTeams() {
		FTBTeamsAPI._init(FTBTeamsAPIImpl.INSTANCE);

		LifecycleEvent.SERVER_BEFORE_START.register(this::serverAboutToStart);
		CommandRegistrationEvent.EVENT.register(this::registerCommands);
		LifecycleEvent.SERVER_STOPPED.register(this::serverStopped);
		LifecycleEvent.SERVER_LEVEL_SAVE.register(this::worldSaved);
		TeamEvent.COLLECT_PROPERTIES.register(this::teamConfig);
		PlayerEvent.PLAYER_JOIN.register(this::playerLoggedIn);
		PlayerEvent.PLAYER_QUIT.register(this::playerLoggedOut);

		EnvExecutor.runInEnv(Env.CLIENT, () -> FTBTeamsClient::init);

		FTBTeamsNet.register();
	}

	private void serverAboutToStart(MinecraftServer server) {
		TeamManagerImpl.INSTANCE = new TeamManagerImpl(server);
		TeamManagerEvent.CREATED.invoker().accept(new TeamManagerEvent(TeamManagerImpl.INSTANCE));
		TeamManagerImpl.INSTANCE.load();
	}

	private void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection selection) {
		new FTBTeamsCommands().register(dispatcher);
	}

	private void serverStopped(MinecraftServer server) {
		TeamManagerEvent.DESTROYED.invoker().accept(new TeamManagerEvent(TeamManagerImpl.INSTANCE));
		TeamManagerImpl.INSTANCE = null;
	}

	private void worldSaved(ServerLevel level) {
		if (TeamManagerImpl.INSTANCE != null) {
			TeamManagerImpl.INSTANCE.saveNow();
		}
	}

	private void teamConfig(TeamCollectPropertiesEvent event) {
		event.add(TeamProperties.DISPLAY_NAME);
		event.add(TeamProperties.DESCRIPTION);
		event.add(TeamProperties.COLOR);
		event.add(TeamProperties.FREE_TO_JOIN);
		event.add(TeamProperties.MAX_MSG_HISTORY_SIZE);
	}

	private void playerLoggedIn(ServerPlayer player) {
		if (TeamManagerImpl.INSTANCE != null) {
			TeamManagerImpl.INSTANCE.playerLoggedIn(player, player.getUUID(), player.getScoreboardName());
		}
	}

	private void playerLoggedOut(ServerPlayer player) {
		if (TeamManagerImpl.INSTANCE != null) {
			TeamManagerImpl.INSTANCE.playerLoggedOut(player);
		}
	}
}
