package xaero.pac.common.server.player.config.dynamic;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import xaero.pac.common.player.config.dynamic.PlayerConfigDynamicOptions;
import xaero.pac.common.server.claims.protection.group.ChunkProtectionExceptionGroup;

import java.util.Map;

public class PlayerConfigDynamicOptionsLoader {

	public void load(PlayerConfigDynamicOptions.Builder builder, Map<String, ChunkProtectionExceptionGroup<Block>> blockExceptionGroups, Map<String, ChunkProtectionExceptionGroup<EntityType<?>>> entityExceptionGroups, Map<String, ChunkProtectionExceptionGroup<Item>> itemExceptionGroups, Map<String, ChunkProtectionExceptionGroup<EntityType<?>>> entityBarrierGroups, Map<String, ChunkProtectionExceptionGroup<EntityType<?>>> blockProtectionExceptionEntityGroups, Map<String, ChunkProtectionExceptionGroup<EntityType<?>>> entityProtectionExceptionEntityGroups, Map<String, ChunkProtectionExceptionGroup<EntityType<?>>> droppedItemProtectionExceptionEntityGroups){
		PlayerConfigExceptionDynamicOptionsLoader exceptionDynamicOptionsLoader = new PlayerConfigExceptionDynamicOptionsLoader();
		entityBarrierGroups.values().forEach(group -> exceptionDynamicOptionsLoader.handleGroup(group, builder, "entity", "entities"));
		blockExceptionGroups.values().forEach(group -> exceptionDynamicOptionsLoader.handleGroup(group, builder, "block", "blocks"));
		entityExceptionGroups.values().forEach(group -> exceptionDynamicOptionsLoader.handleGroup(group, builder, "entity", "entities"));
		itemExceptionGroups.values().forEach(group -> exceptionDynamicOptionsLoader.handleGroup(group, builder, "item", "items"));
		blockProtectionExceptionEntityGroups.values().forEach(group -> exceptionDynamicOptionsLoader.handleGroup(group, builder, "entity", "entities"));
		entityProtectionExceptionEntityGroups.values().forEach(group -> exceptionDynamicOptionsLoader.handleGroup(group, builder, "entity", "entities"));
		droppedItemProtectionExceptionEntityGroups.values().forEach(group -> exceptionDynamicOptionsLoader.handleGroup(group, builder, "entity", "entities"));
	}

}
