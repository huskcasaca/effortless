package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.*;
import dev.huskuraft.effortless.platform.Server;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.UUID;

class MinecraftWorld extends World {

    private final Level level;

    MinecraftWorld(Level level) {
        this.level = level;
    }

    public Level getRef() {
        return level;
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return MinecraftAdapter.adapt(getRef().getPlayerByUUID(uuid));
    }

    @Override
    public BlockData getBlockData(BlockPosition blockPosition) {
        return MinecraftAdapter.adapt(getRef().getBlockState(MinecraftAdapter.adapt(blockPosition)));
    }

    @Override
    public boolean isClient() {
        return getRef().isClientSide();
    }

}
