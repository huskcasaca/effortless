package dev.ftb.mods.ftbchunks.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

@FunctionalInterface
public interface Protection {
    /**
     * Get the protection policy for a specific action
     *
     * @param player player doing the action
     * @param pos    blockpos at which the action occurs
     * @param hand   the hand the player is using
     * @param chunk  the claimed chunk in which the action is occurring, or null if not in a claimed chunk
     * @param entity the entity being acted on, if any
     * @return the protection policy
     */
    ProtectionPolicy getProtectionPolicy(ServerPlayer player, BlockPos pos, InteractionHand hand, @Nullable ClaimedChunk chunk, @Nullable Entity entity);

    Protection EDIT_BLOCK = (player, pos, hand, chunk, entity) -> {
        throw new RuntimeException("stub!");
    };

    Protection INTERACT_BLOCK = (player, pos, hand, chunk, entity) -> {
        throw new RuntimeException("stub!");
    };

    Protection RIGHT_CLICK_ITEM = (player, pos, hand, chunk, entity) -> {
        throw new RuntimeException("stub!");
    };

    static boolean isFood(ItemStack stack) {
        throw new RuntimeException("stub!");
    }

    static boolean isBeneficialPotion(ItemStack stack) {
        throw new RuntimeException("stub!");
    }

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
}
