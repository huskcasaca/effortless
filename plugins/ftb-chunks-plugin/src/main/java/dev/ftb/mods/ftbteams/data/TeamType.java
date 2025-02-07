package dev.ftb.mods.ftbteams.data;

import dev.ftb.mods.ftbteams.api.Team;
import net.minecraft.ChatFormatting;
import net.minecraft.util.StringRepresentable;

import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public enum TeamType implements StringRepresentable {
	PLAYER("player", PlayerTeam::new, Team::isPlayerTeam, ChatFormatting.GRAY),
	PARTY("party", PartyTeam::new, Team::isPartyTeam, ChatFormatting.AQUA),
	SERVER("server", ServerTeam::new, Team::isServerTeam, ChatFormatting.LIGHT_PURPLE);

	private final String name;
	private final BiFunction<TeamManagerImpl, UUID, AbstractTeam> factory;
	private final Predicate<Team> matcher;
	private final ChatFormatting color;

	TeamType(String name, BiFunction<TeamManagerImpl, UUID, AbstractTeam> factory, Predicate<Team> matcher, ChatFormatting color) {
		this.name = name;
		this.factory = factory;
		this.matcher = matcher;
		this.color = color;
	}

	public AbstractTeam createTeam(TeamManagerImpl manager, UUID id) {
		return factory.apply(manager, id);
	}

	@Override
	public String getSerializedName() {
		return name;
	}

	public ChatFormatting getColor() {
		return color;
	}

	public boolean matches(Team team) {
		return matcher.test(team);
	}
}
