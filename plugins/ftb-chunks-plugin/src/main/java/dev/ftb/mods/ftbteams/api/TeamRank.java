package dev.ftb.mods.ftbteams.api;

import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

import java.util.Optional;

public enum TeamRank implements StringRepresentable {
	ENEMY("enemy", -100),
	NONE("none", 0),
	ALLY("ally", 50, Icons.FRIENDS),
	INVITED("invited", 75),
	MEMBER("member", 100, Icons.ACCEPT_GRAY),
	OFFICER("officer", 500, Icons.SHIELD),
	OWNER("owner", 1000, Icons.DIAMOND),
	;

	public static final NameMap<TeamRank> NAME_MAP = NameMap.of(NONE, values()).create();

	private final String name;
	private final int power;
	private final Icon icon;

	TeamRank(String name, int power, Icon icon) {
		this.name = name;
		this.power = power;
		this.icon = icon;
	}

	TeamRank(String name, int power) {
		this(name, power, null);
	}

	@Override
	public String getSerializedName() {
		return name;
	}

	public int getPower() {
		return power;
	}

	public boolean isAtLeast(TeamRank rank) {
		return rank.power >= 0 ?
				power >= rank.power :
				power <= rank.power;
	}

	public boolean isEnemyOrWorse() {
		return isAtLeast(ENEMY);
	}

	public boolean isNoneOrBetter() {
		return isAtLeast(NONE);
	}

	public boolean isAllyOrBetter() {
		return isAtLeast(ALLY);
	}

	public boolean isInvitedOrBetter() {
		return isAtLeast(INVITED);
	}

	public boolean isMemberOrBetter() {
		return isAtLeast(MEMBER);
	}

	public boolean isOfficerOrBetter() {
		return isAtLeast(OFFICER);
	}

	public boolean isOwner() {
		return isAtLeast(OWNER);
	}

	public Optional<Icon> getIcon() {
		return Optional.ofNullable(icon);
	}

	public Component getDisplayName() {
		return Component.translatable("ftbteams.ranks." + name);
	}
}
