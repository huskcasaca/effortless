package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.*;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;

public class MinecraftBlockData extends BlockData {

    private final BlockState blockState;

    MinecraftBlockData(BlockState blockState) {
        this.blockState = blockState;
    }

    public static BlockState mirrorTopBottom(BlockState blockState) {
        // stairs
        if (blockState.getBlock() instanceof StairBlock) {
            return switch (blockState.getValue(StairBlock.HALF)) {
                case TOP -> blockState.setValue(StairBlock.HALF, Half.BOTTOM);
                case BOTTOM -> blockState.setValue(StairBlock.HALF, Half.TOP);
            };
        }

        // slabs
        if (blockState.getBlock() instanceof SlabBlock) {
            return switch (blockState.getValue(SlabBlock.TYPE)) {
                case TOP -> blockState.setValue(SlabBlock.TYPE, SlabType.BOTTOM);
                case BOTTOM -> blockState.setValue(SlabBlock.TYPE, SlabType.TOP);
                case DOUBLE -> blockState;
            };
        }

        // FIXME: 16/10/23
//        // buttons, endrod, observer, piston
        if (blockState.getBlock() instanceof DirectionalBlock) {
            switch (blockState.getValue(DirectionalBlock.FACING)) {
                case DOWN -> {
                    return blockState.setValue(DirectionalBlock.FACING, Direction.UP);
                }
                case UP -> {
                    return blockState.setValue(DirectionalBlock.FACING, Direction.DOWN);
                }
            }
        }

        // dispenser, dropper
        if (blockState.getBlock() instanceof DispenserBlock) {
            switch (blockState.getValue(DispenserBlock.FACING)) {
                case DOWN -> {
                    return blockState.setValue(DispenserBlock.FACING, Direction.UP);
                }
                case UP -> {
                    return blockState.setValue(DispenserBlock.FACING, Direction.DOWN);
                }
            }
        }
        return blockState;
    }

    public static BlockState mirrorLeftRight(BlockState blockState) {
        return blockState.mirror(Mirror.LEFT_RIGHT);
    }

    public static BlockState mirrorFrontBack(BlockState blockState) {
        return blockState.mirror(Mirror.FRONT_BACK);
    }

    public BlockState getRef() {
        return blockState;
    }

    @Override
    public BlockData rotate(Revolve revolve) {
        return new MinecraftBlockData(blockState.rotate(MinecraftAdapter.adapt(revolve)));
    }

    @Override
    public boolean isAir() {
        return blockState.isAir();
    }

    @Override
    public Item getItem() {
        return new MinecraftItem(blockState.getBlock().asItem());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftBlockData fabricBlockState && blockState.equals(fabricBlockState.getRef());
    }

    @Override
    public int hashCode() {
        return blockState.hashCode();
    }

    @Override
    public BlockData mirror(Axis axis) {
        return switch (axis) {
            case Y -> new MinecraftBlockData(mirrorTopBottom(blockState));
            case X -> new MinecraftBlockData(mirrorFrontBack(blockState));
            case Z -> new MinecraftBlockData(mirrorLeftRight(blockState));
        };
    }

    @Override
    public boolean isReplaceable(Player player, BlockInteraction interaction) {
        return getRef().canBeReplaced(new net.minecraft.world.item.context.BlockPlaceContext(
                ((MinecraftPlayer) player).getRef(),
                MinecraftAdapter.adapt(interaction.getHand()),
                ((MinecraftItemStack) player.getItemStack(interaction.getHand())).getRef(),
                MinecraftAdapter.adapt(interaction)
        ));
    }

    @Override
    public boolean isReplaceable() {
        return getRef().canBeReplaced();
    }

    @Override
    public boolean isDestroyable() {
        return !getRef().is(BlockTags.FEATURES_CANNOT_REPLACE);
    }
}
