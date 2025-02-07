package dev.ftb.mods.ftbteams.api.property;

import dev.ftb.mods.ftblibrary.config.NameMap;
import net.minecraft.util.StringRepresentable;

public enum PrivacyMode implements StringRepresentable {
	ALLIES("allies"),
	PRIVATE("private"),
	PUBLIC("public");

	public static final PrivacyMode[] VALUES = values();
	public static final NameMap<PrivacyMode> NAME_MAP = NameMap.of(ALLIES, VALUES).baseNameKey("ftbteams.privacy_mode").create();

	public final String name;

	PrivacyMode(String name) {
		this.name = name;
	}

	@Override
	public String getSerializedName() {
		return name;
	}
}