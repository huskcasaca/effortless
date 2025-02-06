package xaero.pac.common.server.claims.protection;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class ExceptionElementType<T> {

	private static final List<ExceptionElementType<?>> TYPES = new ArrayList<>();
	public static final ExceptionElementType<Block> BLOCK = new ExceptionElementType<>(Registries.BLOCK, Block.class);
	public static final ExceptionElementType<EntityType<?>> ENTITY_TYPE = new ExceptionElementType<>(Registries.ENTITY_TYPE, EntityType.class);
	public static final ExceptionElementType<Item> ITEM = new ExceptionElementType<>(Registries.ITEM, Item.class);
	private final ResourceKey<Registry<T>> registryResourceKey;
	private Iterable<T> iterable;
	private Iterable<TagKey<T>> tagIterable;
	private Class<?> type;

	public ExceptionElementType(ResourceKey<Registry<T>> registryResourceKey, Class<?> type) {
		this.registryResourceKey = registryResourceKey;
		this.type = type;
		TYPES.add(this);
	}

	public Registry<T> getRegistry(MinecraftServer server){
		return server.registryAccess().lookup(registryResourceKey).orElseThrow();
	}

	public Iterable<T> getIterable() {
		return iterable;
	}

	public Iterable<TagKey<T>> getTagIterable() {
		return tagIterable;
	}

	public static void updateAllIterables(MinecraftServer server){
		TYPES.forEach(type -> type.updateIterables(server));
	}

	public Class<?> getType() {
		return type;
	}

	public static void clearAllIterables(){
		TYPES.forEach(type -> {
			type.iterable = null;
			type.tagIterable = null;
		});
	}

	private void updateIterables(MinecraftServer server){
		Registry<T> registry = getRegistry(server);
		iterable = registry.stream().toList();
		tagIterable = registry.listTagIds().toList();
	}

}
