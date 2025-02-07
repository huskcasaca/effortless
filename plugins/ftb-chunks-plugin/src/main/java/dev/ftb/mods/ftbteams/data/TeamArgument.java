package dev.ftb.mods.ftbteams.data;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class TeamArgument implements ArgumentType<TeamArgumentProvider> {
	public static final SimpleCommandExceptionType ALREADY_IN_PARTY = new SimpleCommandExceptionType(Component.translatable("ftbteams.already_in_party"));
	public static final DynamicCommandExceptionType PLAYER_IN_PARTY = new DynamicCommandExceptionType(object -> Component.translatable("ftbteams.player_already_in_party", object));
	public static final SimpleCommandExceptionType NOT_IN_PARTY = new SimpleCommandExceptionType(Component.translatable("ftbteams.not_in_party"));
	public static final DynamicCommandExceptionType TEAM_NOT_FOUND = new DynamicCommandExceptionType(object -> Component.translatable("ftbteams.team_not_found", object));
	public static final DynamicCommandExceptionType CANT_EDIT = new DynamicCommandExceptionType(object -> Component.translatable("ftbteams.cant_edit", object));
	public static final Dynamic2CommandExceptionType NOT_MEMBER = new Dynamic2CommandExceptionType((a, b) -> Component.translatable("ftbteams.not_member", a, b));
	public static final Dynamic2CommandExceptionType NOT_OFFICER = new Dynamic2CommandExceptionType((a, b) -> Component.translatable("ftbteams.not_officer", a, b));
	public static final DynamicCommandExceptionType NOT_INVITED = new DynamicCommandExceptionType(object -> Component.translatable("ftbteams.not_invited", object));
	public static final SimpleCommandExceptionType OWNER_CANT_LEAVE = new SimpleCommandExceptionType(Component.translatable("ftbteams.owner_cant_leave"));
	public static final SimpleCommandExceptionType CANT_KICK_OWNER = new SimpleCommandExceptionType(Component.translatable("ftbteams.cant_kick_owner"));
	public static final SimpleCommandExceptionType API_OVERRIDE = new SimpleCommandExceptionType(Component.translatable("ftbteams.party_api_only"));
	public static final SimpleCommandExceptionType NAME_TOO_SHORT = new SimpleCommandExceptionType(Component.translatable("ftbteams.name_too_short"));
	public static final SimpleCommandExceptionType NO_PERMISSION = new SimpleCommandExceptionType(Component.translatable("ftbteams.server_permissions_prevent"));

	private final TeamType type;

    public static TeamArgument create() {
		return new TeamArgument(null);
	}

	public static TeamArgument create(TeamType type) {
		return new TeamArgument(type);
	}

	public static Team get(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
		return context.getArgument(name, TeamArgumentProvider.class).getTeam(context.getSource());
	}

	private TeamArgument(@Nullable TeamType type) {
        this.type = type;
    }

	private static class SelectorProvider implements TeamArgumentProvider {
		private final EntitySelector selector;

		private SelectorProvider(EntitySelector s) {
			selector = s;
		}

		@Override
		public Team getTeam(CommandSourceStack source) throws CommandSyntaxException {
			ServerPlayer player = selector.findSinglePlayer(source);
			return FTBTeamsAPI.api().getManager().getTeamForPlayer(player)
					.orElseThrow(() -> TEAM_NOT_FOUND.create(player.getUUID()));
		}
	}

	private static class IDProvider implements TeamArgumentProvider {
		private final String id;

		private IDProvider(String s) {
			id = s;
		}

		private CommandSyntaxException error() {
			return TeamArgument.TEAM_NOT_FOUND.create(id);
		}

		@Override
		public Team getTeam(CommandSourceStack source) throws CommandSyntaxException {
			Optional<Team> t = FTBTeamsAPI.api().getManager().getTeamByName(id);
			if (t.isPresent()) {
				return t.get();
			}

			return source.getServer().getProfileCache().get(id)
							.map(GameProfile::getId)
							.map(FTBTeamsAPI.api().getManager()::getTeamForPlayerID)
							.orElseThrow()
							.orElseThrow(this::error);
		}
	}

	@Override
	public TeamArgumentProvider parse(StringReader reader) throws CommandSyntaxException {
		if (reader.canRead() && reader.peek() == '@') {
			EntitySelector selector = new EntitySelectorParser(reader, true).parse();

			if (selector.includesEntities()) {
				throw EntityArgument.ERROR_ONLY_PLAYERS_ALLOWED.create();
			} else {
				return new SelectorProvider(selector);
			}
		}

		int i = reader.getCursor();

		while (reader.canRead() && reader.peek() != ' ') {
			reader.skip();
		}

		return new IDProvider(reader.getString().substring(i, reader.getCursor()));
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContext, SuggestionsBuilder builder) {
		if (commandContext.getSource() instanceof SharedSuggestionProvider) {
			Stream<String> list = getTeams(commandContext).stream()
					.filter(t -> type == null || type.matches(t))
					.map(Team::getShortName)
					.sorted();
			return SharedSuggestionProvider.suggest(list, builder);
		}

		return Suggestions.empty();
	}

	private Collection<Team> getTeams(CommandContext<?> context) {
		FTBTeamsAPI.API api = FTBTeamsAPI.api();
		if (context.getSource() instanceof CommandSourceStack && api.isManagerLoaded()) {
			return api.getManager().getTeams();
		} else if (api.isClientManagerLoaded()) {
			return api.getClientManager().getTeams();
		} else {
			return List.of();
		}
	}

	public static class Info implements ArgumentTypeInfo<TeamArgument, Info.Template> {
		@Override
		public void serializeToNetwork(Template template, FriendlyByteBuf buf) {
			buf.writeNullable(template.teamType, FriendlyByteBuf::writeEnum);
		}

		@Override
		public Template deserializeFromNetwork(FriendlyByteBuf buf) {
			return new Template(buf.readNullable(b -> b.readEnum(TeamType.class)));
		}

		@Override
		public void serializeToJson(Template template, JsonObject jsonObject) {
			if (template.teamType != null) {
				jsonObject.addProperty("type", template.teamType.name());
			}
		}

		@Override
		public Template unpack(TeamArgument argumentType) {
			return new Template(argumentType.type);
		}

		public final class Template implements ArgumentTypeInfo.Template<TeamArgument> {
			private final TeamType teamType;

			public Template(TeamType teamType) {
				this.teamType = teamType;
			}

			@Override
			public TeamArgument instantiate(CommandBuildContext commandBuildContext) {
				return TeamArgument.create(teamType);
			}

			@Override
			public ArgumentTypeInfo<TeamArgument, ?> type() {
				return Info.this;
			}
		}
	}
}
