package dev.huskuraft.effortless.api.core;

import java.util.List;

import javax.annotation.Nullable;

import dev.huskuraft.effortless.api.core.fluid.BucketCollectable;
import dev.huskuraft.effortless.api.core.fluid.LiquidPlaceable;
import dev.huskuraft.effortless.api.platform.PlatformReference;

public interface Block extends PlatformReference {

    BlockState getDefaultBlockState();

    BlockState getBlockState(Player player, BlockInteraction interaction);

    BucketCollectable getBucketCollectable();

    LiquidPlaceable getLiquidPlaceable();

    void destroy(World world, Player player, BlockPosition blockPosition, BlockState blockState);

    void destroyStart(World world, Player player, BlockPosition blockPosition, BlockState blockState, BlockEntity blockEntity, ItemStack itemStack);

    void destroyEnd(World world, Player player, BlockPosition blockPosition, BlockState blockState, BlockEntity blockEntity, ItemStack itemStack);

    void place(World world, Player player, BlockPosition blockPosition, BlockState blockState, ItemStack itemStack);

    Item asItem();

    BlockEntity getEntity(BlockPosition blockPosition, BlockState blockState);

    List<ItemStack> getDrops(World world, Player player, BlockPosition blockPosition, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack);

}
