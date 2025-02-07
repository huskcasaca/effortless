package dev.ftb.mods.ftbteams.api.property;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Represents an individual property; the combination of a unique ID, and the default value for the property.
 *
 * @param <T> the type of value that the property holds
 */
public abstract class TeamProperty<T> {
	protected final ResourceLocation id;
	private final Supplier<T> defaultValue;

	protected TeamProperty(ResourceLocation _id, Supplier<T> def) {
		id = _id;
		defaultValue = def;
	}

	public ResourceLocation getId() {
		return id;
	}

	public T getDefaultValue() {
		return defaultValue.get();
	}

	public String getTranslationKey(String prefix) {
		return prefix + "." + id.getNamespace() + "." + id.getPath();
	}

	public abstract TeamPropertyType<T> getType();

	public abstract Optional<T> fromString(String string);

	public abstract void write(RegistryFriendlyByteBuf buf);

	public String toString(T value) {
		return value.toString();
	}

	public final int hashCode() {
		return id.hashCode();
	}

	public final String toString() {
		return id.toString();
	}

	public final boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof TeamProperty) {
			return id.equals(((TeamProperty<?>) o).id);
		}

		return false;
	}

	public void writeValue(RegistryFriendlyByteBuf buf, T value) {
		buf.writeUtf(toString(value), Short.MAX_VALUE);
	}

	public T readValue(RegistryFriendlyByteBuf buf) {
		return fromString(buf.readUtf(Short.MAX_VALUE)).orElse(getDefaultValue());
	}

	public void config(ConfigGroup config, TeamPropertyValue<T> value) {
	}

	public Tag toNBT(T value) {
		return StringTag.valueOf(toString(value));
	}

	public Optional<T> fromNBT(Tag tag) {
		return fromString(tag.getAsString());
	}

	public TeamPropertyValue<T> createDefaultValue() {
		return new TeamPropertyValue<>(this, getDefaultValue());
	}

	public TeamPropertyValue<T> createValueFromNetwork(RegistryFriendlyByteBuf buf) {
		return new TeamPropertyValue<>(this, readValue(buf));
	}

	public TeamPropertyValue<T> createValueFromNBT(Tag tag) {
		return new TeamPropertyValue<>(this, fromNBT(tag).orElse(getDefaultValue()));
	}
}