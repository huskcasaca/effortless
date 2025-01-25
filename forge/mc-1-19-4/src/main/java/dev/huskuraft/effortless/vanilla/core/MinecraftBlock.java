package dev.huskuraft.effortless.vanilla.core;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import dev.huskuraft.effortless.api.core.Block;
import dev.huskuraft.effortless.api.core.BlockEntity;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.Direction;
import dev.huskuraft.effortless.api.core.FluidState;
import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.World;
import dev.huskuraft.effortless.api.core.fluid.BucketCollectable;
import dev.huskuraft.effortless.api.core.fluid.Fluid;
import dev.huskuraft.effortless.api.core.fluid.LiquidPlaceable;
import dev.huskuraft.effortless.api.sound.Sound;
import dev.huskuraft.effortless.vanilla.sound.MinecraftSound;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;

public record MinecraftBlock(
        net.minecraft.world.level.block.Block refs
) implements Block {

    @Override
    public BlockState getDefaultBlockState() {
        return MinecraftBlockState.ofNullable(refs.defaultBlockState());
    }

    @Override
    public BlockState getBlockState(Player player, BlockInteraction interaction) {
        BlockItem blockItem = (BlockItem) refs.asItem();
        Direction opposite = interaction.direction().getOpposite();
        BlockPosition placementPosition = interaction.getBlockPosition().offset(opposite.getStepX(), opposite.getStepY(), opposite.getStepZ());
        return MinecraftBlockState.ofNullable(blockItem.getPlacementState(new BlockPlaceContext(
                player.reference(),
                MinecraftConvertor.toPlatformInteractionHand(interaction.getHand()),
                player.getItemStack(interaction.getHand()).reference(),
                MinecraftConvertor.toPlatformBlockInteraction(interaction.withBlockPosition(placementPosition))
        )));
    }

    @Override
    public BucketCollectable getBucketCollectable() {
        if (refs instanceof net.minecraft.world.level.block.BucketPickup bucketPickup) {
            return new BucketCollectable() {

                @Override
                public ItemStack pickupBlock(World world, Player player, BlockPosition blockPosition, BlockState blockState) {
                    return new MinecraftItemStack(bucketPickup.pickupBlock(world.reference(), MinecraftConvertor.toPlatformBlockPosition(blockPosition), blockState.reference()));
                }

                @Override
                public Optional<Sound> getPickupSound() {
                    return bucketPickup.getPickupSound().map(MinecraftSound::new);
                }
            };
        }
        return null;
    }

    @Override
    public LiquidPlaceable getLiquidPlaceable() {
        if (refs instanceof LiquidBlockContainer liquidPlace) {
            return new LiquidPlaceable() {
                @Override
                public boolean canPlaceLiquid(World world, Player player, BlockPosition blockPosition, BlockState blockState, Fluid fluid) {
                    return liquidPlace.canPlaceLiquid(world.reference(), MinecraftConvertor.toPlatformBlockPosition(blockPosition), blockState.reference(), fluid.reference());
                }

                @Override
                public boolean placeLiquid(World world, BlockPosition blockPosition, BlockState blockState, FluidState fluidState) {
                    return liquidPlace.placeLiquid(world.reference(), MinecraftConvertor.toPlatformBlockPosition(blockPosition), blockState.reference(), fluidState.reference());
                }
            };
        }
        return null;
    }

    @Override
    public void destroy(World world, Player player, BlockPosition blockPosition, BlockState blockState) {
        refs.destroy(world.reference(), MinecraftConvertor.toPlatformBlockPosition(blockPosition), blockState.reference());
    }

    @Override
    public void destroyStart(World world, Player player, BlockPosition blockPosition, BlockState blockState, BlockEntity blockEntity, ItemStack itemStack) {
        refs.playerWillDestroy(world.reference(), MinecraftConvertor.toPlatformBlockPosition(blockPosition), blockState.reference(), player.reference());
    }

    @Override
    public void destroyEnd(World world, Player player, BlockPosition blockPosition, BlockState blockState, BlockEntity blockEntity, ItemStack itemStack) {
        refs.playerDestroy(world.reference(), player.reference(), MinecraftConvertor.toPlatformBlockPosition(blockPosition), blockState.reference(), blockEntity == null ? null : blockEntity.reference(), itemStack.reference());
    }

    @Override
    public void place(World world, Player player, BlockPosition blockPosition, BlockState blockState, ItemStack itemStack) {
        refs.setPlacedBy(world.reference(), MinecraftConvertor.toPlatformBlockPosition(blockPosition), blockState.reference(), player.reference(), itemStack.reference());
    }

    @Override
    public Item asItem() {
        return MinecraftItem.ofNullable(refs.asItem());
    }

    @Override
    public BlockEntity getEntity(BlockPosition blockPosition, BlockState blockState) {
        if (refs instanceof EntityBlock entityBlock) {
            return MinecraftBlockEntity.ofNullable(entityBlock.newBlockEntity(MinecraftConvertor.toPlatformBlockPosition(blockPosition), blockState.reference()));
        }
        return null;
    }

    @Override
    public List<ItemStack> getDrops(World world, Player player, BlockPosition blockPosition, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        return net.minecraft.world.level.block.Block.getDrops(blockState.reference(), world.reference(), MinecraftConvertor.toPlatformBlockPosition(blockPosition), blockEntity == null ? null : blockEntity.reference(), player.reference(), itemStack.reference()).stream().map(MinecraftItemStack::ofNullable).toList();
    }

}
