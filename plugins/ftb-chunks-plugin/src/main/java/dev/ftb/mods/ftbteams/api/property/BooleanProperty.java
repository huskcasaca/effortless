package dev.ftb.mods.ftbteams.api.property;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.Supplier;

public class BooleanProperty extends TeamProperty<Boolean> {
	private static final Optional<Boolean> TRUE = Optional.of(Boolean.TRUE);
	private static final Optional<Boolean> FALSE = Optional.of(Boolean.FALSE);

	public BooleanProperty(ResourceLocation id, Supplier<Boolean> def) {
		super(id, def);
	}

	public BooleanProperty(ResourceLocation id, Boolean def) {
		this(id, () -> def);
	}

	static BooleanProperty fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
		boolean val = buf.readBoolean();
		return new BooleanProperty(id, () -> val);
	}

	@Override
	public TeamPropertyType<Boolean> getType() {
		return TeamPropertyType.BOOLEAN;
	}

	@Override
	public Optional<Boolean> fromString(String string) {
		if (string.equals("true")) {
			return TRUE;
		} else if (string.equals("false")) {
			return FALSE;
		}

		return Optional.empty();
	}

	@Override
	public void write(RegistryFriendlyByteBuf buf) {
		buf.writeBoolean(getDefaultValue());
	}

	@Override
	public void config(ConfigGroup config, TeamPropertyValue<Boolean> value) {
		config.addBool(id.getPath(), value.value, value.consumer, getDefaultValue());
	}

	@Override
	public Tag toNBT(Boolean value) {
		return ByteTag.valueOf(value);
	}

	@Override
	public Optional<Boolean> fromNBT(Tag tag) {
		if (tag instanceof NumericTag) {
			if (((NumericTag) tag).getAsByte() == 1) {
				return TRUE;
			}

			return FALSE;
		}

		return Optional.empty();
	}
}