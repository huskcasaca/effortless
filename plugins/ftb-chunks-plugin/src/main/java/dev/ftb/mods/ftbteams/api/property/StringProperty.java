package dev.ftb.mods.ftbteams.api.property;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class StringProperty extends TeamProperty<String> {
	private final Pattern pattern;

	public StringProperty(ResourceLocation id, Supplier<String> def, @Nullable Pattern pattern) {
		super(id, def);
		this.pattern = pattern;
	}

	public StringProperty(ResourceLocation id, Supplier<String> def) {
		this(id, def, null);
	}

	public StringProperty(ResourceLocation id, String def, @Nullable Pattern pattern) {
		this(id, () -> def, pattern);
	}

	public StringProperty(ResourceLocation id, String def) {
		this(id, () -> def);
	}

	static StringProperty fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
		String def = buf.readUtf(Short.MAX_VALUE);
		int flags = buf.readVarInt();
		String patVal = buf.readUtf(Short.MAX_VALUE);

		//noinspection MagicConstant
		return new StringProperty(id, def, patVal.isEmpty() ? null : Pattern.compile(patVal, flags));
	}

	@Override
	public TeamPropertyType<String> getType() {
		return TeamPropertyType.STRING;
	}

	@Override
	public Optional<String> fromString(String string) {
		if (pattern == null || pattern.matcher(string).matches()) {
			return Optional.of(string);
		}

		return Optional.empty();
	}

	@Override
	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeUtf(getDefaultValue(), Short.MAX_VALUE);
		buf.writeVarInt(pattern == null ? 0 : pattern.flags());
		buf.writeUtf(pattern == null ? "" : pattern.pattern(), Short.MAX_VALUE);
	}

	@Override
	public void config(ConfigGroup config, TeamPropertyValue<String> value) {
		config.addString(id.getPath(), value.value, value.consumer, getDefaultValue(), pattern);
	}
}