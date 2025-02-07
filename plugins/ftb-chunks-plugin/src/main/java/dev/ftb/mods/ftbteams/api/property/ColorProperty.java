package dev.ftb.mods.ftbteams.api.property;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.Supplier;

public class ColorProperty extends TeamProperty<Color4I> {
	public ColorProperty(ResourceLocation id, Supplier<Color4I> def) {
		super(id, def);
	}

	public ColorProperty(ResourceLocation id, Color4I def) {
		this(id, () -> def);
	}

	static ColorProperty fromNetwork(ResourceLocation id, FriendlyByteBuf def) {
		return new ColorProperty(id, Color4I.rgb(def.readVarInt()));
	}

	@Override
	public TeamPropertyType<Color4I> getType() {
		return TeamPropertyType.COLOR;
	}

	@Override
	public Optional<Color4I> fromString(String string) {
		Color4I c = Color4I.fromString(string);
		return c.isEmpty() ? Optional.empty() : Optional.of(c);
	}

	@Override
	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeVarInt(getDefaultValue().rgb());
	}

	@Override
	public String toString(Color4I value) {
		return value.toString();
	}

	@Override
	public void config(ConfigGroup config, TeamPropertyValue<Color4I> value) {
		config.addColor(id.getPath(), value.value, value.consumer, getDefaultValue());
	}
}