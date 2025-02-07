package dev.ftb.mods.ftbteams.api.property;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.Supplier;

public class PrivacyProperty extends TeamProperty<PrivacyMode> {
	public PrivacyProperty(ResourceLocation id, Supplier<PrivacyMode> def) {
		super(id, def);
	}

	public PrivacyProperty(ResourceLocation id, PrivacyMode def) {
		this(id, () -> def);
	}

	static PrivacyProperty fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
		return new PrivacyProperty(id, buf.readEnum(PrivacyMode.class));
	}

	@Override
	public TeamPropertyType<PrivacyMode> getType() {
		return TeamPropertyType.PRIVACY_MODE;
	}

	@Override
	public Optional<PrivacyMode> fromString(String string) {
		return Optional.ofNullable(PrivacyMode.NAME_MAP.getNullable(string));
	}

	@Override
	public String toString(PrivacyMode value) {
		return value.getSerializedName();
	}

	@Override
	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeEnum(getDefaultValue());
	}

	@Override
	public void config(ConfigGroup config, TeamPropertyValue<PrivacyMode> value) {
		config.addEnum(id.getPath(), value.value, value.consumer, PrivacyMode.NAME_MAP);
	}
}