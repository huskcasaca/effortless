package dev.ftb.mods.ftbchunks.data;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@FunctionalInterface
public interface Protection {
    Protection EDIT_BLOCK = (player, pos, hand, chunk, entity) -> {
        throw new RuntimeException("stub!");
    };

    Protection INTERACT_BLOCK = (player, pos, hand, chunk, entity) -> {
        throw new RuntimeException("stub!");
    };

    Protection RIGHT_CLICK_ITEM = (player, pos, hand, chunk, entity) -> {
        throw new RuntimeException("stub!");
    };

    Protection EDIT_FLUID = (player, pos, hand, chunk, entity) -> {
        throw new RuntimeException("stub!");
    };

    Protection INTERACT_ENTITY = (player, pos, hand, chunk, entity) -> {
        throw new RuntimeException("stub!");
    };

    Protection ATTACK_NONLIVING_ENTITY = (player, pos, hand, chunk, entity) -> {
        throw new RuntimeException("stub!");
    };

    // for use on Fabric
    Protection EDIT_AND_INTERACT_BLOCK = (player, pos, hand, chunk, entity) -> {
        throw new RuntimeException("stub!");
    };

    ProtectionOverride override(ServerPlayer player, BlockPos pos, InteractionHand hand, @Nullable ClaimedChunk chunk, @Nullable Entity entity);
}
