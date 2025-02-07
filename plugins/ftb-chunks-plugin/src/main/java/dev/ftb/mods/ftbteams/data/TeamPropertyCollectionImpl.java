package dev.ftb.mods.ftbteams.data;

import dev.ftb.mods.ftbteams.api.event.TeamCollectPropertiesEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import dev.ftb.mods.ftbteams.api.property.TeamProperty;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyType;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyValue;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.function.BiConsumer;

public class TeamPropertyCollectionImpl implements TeamPropertyCollection {
	private final PropertyMap map = new PropertyMap();

	public static final StreamCodec<RegistryFriendlyByteBuf, TeamPropertyCollection> STREAM_CODEC = StreamCodec.of(
            (buffer, props) -> {
				buffer.writeVarInt(props.size());
				props.forEach((prop, value) -> {
					TeamPropertyType.write(buffer, prop);
					prop.writeValue(buffer, value.getValue());
				});
            },
            buffer -> {
				TeamPropertyCollectionImpl props = new TeamPropertyCollectionImpl();
				int nProperties = buffer.readVarInt();
				for (int i = 0; i < nProperties; i++) {
					TeamProperty<?> tp = TeamPropertyType.read(buffer);
					props.map.putNetworkProperty(tp, buffer);
				}
				return props;
			}
    );

	public void collectProperties() {
		map.clear();
		TeamEvent.COLLECT_PROPERTIES.invoker().accept(new TeamCollectPropertiesEvent(map::putDefaultProperty));
	}

	@Override
	public <T> void forEach(BiConsumer<TeamProperty<T>, TeamPropertyValue<T>> consumer) {
		map.forEachProperty(consumer);
	}

	@Override
	public TeamPropertyCollectionImpl copy() {
		TeamPropertyCollectionImpl p = new TeamPropertyCollectionImpl();

		map.forEachProperty((key, value) -> p.map.putProperty(key, value.copy()));

		return p;
	}

	@Override
	public void updateFrom(TeamPropertyCollection otherProperties) {
		otherProperties.forEach((key, value) -> set(key, value.getValue()));
	}

	@Override
	public <T> T get(TeamProperty<T> key) {
		TeamPropertyValue<T> v = map.getProperty(key);
		return v == null ? key.getDefaultValue() : v.getValue();
	}

	@Override
	public <T> void set(TeamProperty<T> key, T value) {
		if (map.hasProperty(key)) {
			map.getProperty(key).setValue(value);
		} else {
			map.putProperty(key, new TeamPropertyValue<>(key, value));
		}
	}

	@Override
	public int size() {
		return map.size();
	}

	public void write(RegistryFriendlyByteBuf buffer) {
		TeamPropertyCollectionImpl.STREAM_CODEC.encode(buffer, this);
	}

	public void writeSyncableOnly(RegistryFriendlyByteBuf buffer, List<TeamProperty<?>> syncableProps) {
		// this is used when sync'ing team data for a different team
		// player A only needs to know limited info (display name, color...) about team B if A isn't a member of B
		PropertyMap subMap = new PropertyMap();
		syncableProps.forEach(prop -> {
			if (map.hasProperty(prop)) {
				subMap.backingMap.put(prop, map.backingMap.get(prop));
			}
		});

		buffer.writeVarInt(subMap.size());
		subMap.forEachProperty((key, value) -> {
			TeamPropertyType.write(buffer, key);
			key.writeValue(buffer, value.getValue());
		});
	}

	public void read(CompoundTag tag) {
		tag.getAllKeys().forEach(key -> map.findProperty(key).ifPresent(prop -> map.putNBTProperty(prop, tag.get(key))));
	}

	public CompoundTag write(CompoundTag tag) {
		map.forEachProperty((key, value) -> tag.put(key.getId().toString(), key.toNBT(value.getValue())));

		return tag;
	}

	private static class PropertyMap {
		final Map<Object, Object> backingMap = new LinkedHashMap<>();
		final Map<ResourceLocation, TeamProperty<?>> byId = new HashMap<>();

		void clear() {
			backingMap.clear();
			byId.clear();
		}

		boolean hasProperty(TeamProperty<?> prop) {
			return backingMap.containsKey(prop);
		}

		int size() {
			return backingMap.size();
		}

		<T> void putProperty(TeamProperty<T> prop, TeamPropertyValue<T> value) {
			backingMap.put(prop, value);
			byId.put(prop.getId(), prop);
		}

		void putDefaultProperty(TeamProperty<?> prop) {
			backingMap.put(prop, prop.createDefaultValue());
			byId.put(prop.getId(), prop);
		}

		void putNetworkProperty(TeamProperty<?> prop, RegistryFriendlyByteBuf buffer) {
			backingMap.put(prop, prop.createValueFromNetwork(buffer));
			byId.put(prop.getId(), prop);
		}

		void putNBTProperty(TeamProperty<?> prop, Tag tag) {
			backingMap.put(prop, prop.createValueFromNBT(tag));
			byId.put(prop.getId(), prop);
		}

		<T> TeamPropertyValue<T> getProperty(TeamProperty<T> property) {
			//noinspection unchecked
			return (TeamPropertyValue<T>) backingMap.get(property);
		}

		<T> void forEachProperty(BiConsumer<TeamProperty<T>, TeamPropertyValue<T>> consumer) {
			//noinspection unchecked
			backingMap.forEach((k, v) -> consumer.accept((TeamProperty<T>) k, (TeamPropertyValue<T>) v));
		}

		Optional<TeamProperty<?>> findProperty(String key) {
			try {
				return Optional.ofNullable(byId.get(ResourceLocation.tryParse(key)));
			} catch (ResourceLocationException e) {
				return Optional.empty();
			}
		}
	}
}
