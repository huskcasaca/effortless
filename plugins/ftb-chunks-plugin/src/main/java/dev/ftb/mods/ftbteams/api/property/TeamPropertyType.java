package dev.ftb.mods.ftbteams.api.property;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamPropertyType<T> {
	private static final Map<String, TeamPropertyType<?>> MAP = new HashMap<>();

	public static final TeamPropertyType<Boolean> BOOLEAN = TeamPropertyType.register("boolean", BooleanProperty::fromNetwork);
	public static final TeamPropertyType<String> STRING = TeamPropertyType.register("string", StringProperty::fromNetwork);
	public static final TeamPropertyType<List<String>> STRING_LIST = TeamPropertyType.register("string_list", StringListProperty::fromNetwork);
	public static final TeamPropertyType<Integer> INT = TeamPropertyType.register("int", IntProperty::fromNetwork);
	public static final TeamPropertyType<Double> DOUBLE = TeamPropertyType.register("double", DoubleProperty::fromNetwork);
	public static final TeamPropertyType<Color4I> COLOR = TeamPropertyType.register("color", ColorProperty::fromNetwork);
	public static final TeamPropertyType<String> ENUM = TeamPropertyType.register("enum", EnumProperty::fromNetwork);
	public static final TeamPropertyType<PrivacyMode> PRIVACY_MODE = TeamPropertyType.register("privacy_mode", PrivacyProperty::fromNetwork);

	private final String id;
	private final FromNet<T> deserializer;

	private TeamPropertyType(String id, FromNet<T> deserializer) {
		this.id = id;
		this.deserializer = deserializer;
	}

	public static TeamProperty<?> read(RegistryFriendlyByteBuf buf) {
		return MAP.get(buf.readUtf(Short.MAX_VALUE)).deserializer.apply(buf.readResourceLocation(), buf);
	}

	public static void write(RegistryFriendlyByteBuf buf, TeamProperty<?> p) {
		buf.writeUtf(p.getType().id, Short.MAX_VALUE);
		buf.writeResourceLocation(p.id);
		p.write(buf);
	}

	private static <Y> TeamPropertyType<Y> register(String id, FromNet<Y> p) {
		TeamPropertyType<Y> t = new TeamPropertyType<>(id, p);
		MAP.put(id, t);
		return t;
	}

	public interface FromNet<Y> {
		TeamProperty<Y> apply(ResourceLocation id, RegistryFriendlyByteBuf buf);
	}
}
