package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.api.core.*;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;

public class MinecraftBlockState implements BlockState {

    private final net.minecraft.world.level.block.state.BlockState reference;

    MinecraftBlockState(net.minecraft.world.level.block.state.BlockState reference) {
        this.reference = reference;
    }

    @Override
    public net.minecraft.world.level.block.state.BlockState referenceValue() {
        return reference;
    }

    public static BlockState fromMinecraftBlockState(net.minecraft.world.level.block.state.BlockState value) {
        return new MinecraftBlockState(value);
    }

    public static net.minecraft.world.level.block.state.BlockState toMinecraftBlockState(BlockState value) {
        return value.reference();
    }

    public static net.minecraft.world.level.block.state.BlockState mirrorTopBottom(net.minecraft.world.level.block.state.BlockState value) {
        // stairs
        if (value.getBlock() instanceof StairBlock) {
            return switch (value.getValue(StairBlock.HALF)) {
                case TOP -> value.setValue(StairBlock.HALF, Half.BOTTOM);
                case BOTTOM -> value.setValue(StairBlock.HALF, Half.TOP);
            };
        }

        // slabs
        if (value.getBlock() instanceof SlabBlock) {
            return switch (value.getValue(SlabBlock.TYPE)) {
                case TOP -> value.setValue(SlabBlock.TYPE, SlabType.BOTTOM);
                case BOTTOM -> value.setValue(SlabBlock.TYPE, SlabType.TOP);
                case DOUBLE -> value;
            };
        }

        // FIXME: 16/10/23
//        // buttons, endrod, observer, piston
        if (value.getBlock() instanceof DirectionalBlock) {
            switch (value.getValue(DirectionalBlock.FACING)) {
                case DOWN -> {
                    return value.setValue(DirectionalBlock.FACING, Direction.UP);
                }
                case UP -> {
                    return value.setValue(DirectionalBlock.FACING, Direction.DOWN);
                }
            }
        }

        // dispenser, dropper
        if (value.getBlock() instanceof DispenserBlock) {
            switch (value.getValue(DispenserBlock.FACING)) {
                case DOWN -> {
                    return value.setValue(DispenserBlock.FACING, Direction.UP);
                }
                case UP -> {
                    return value.setValue(DispenserBlock.FACING, Direction.DOWN);
                }
            }
        }
        return value;
    }

    public static net.minecraft.world.level.block.state.BlockState mirrorLeftRight(net.minecraft.world.level.block.state.BlockState value) {
        return value.mirror(Mirror.LEFT_RIGHT);
    }

    public static net.minecraft.world.level.block.state.BlockState mirrorFrontBack(net.minecraft.world.level.block.state.BlockState value) {
        return value.mirror(Mirror.FRONT_BACK);
    }

    @Override
    public BlockState rotate(Revolve revolve) {
        return new MinecraftBlockState(reference.rotate(switch (revolve) {
            case NONE -> Rotation.NONE;
            case CLOCKWISE_90 -> Rotation.CLOCKWISE_90;
            case CLOCKWISE_180 -> Rotation.CLOCKWISE_180;
            case COUNTERCLOCKWISE_90 -> Rotation.COUNTERCLOCKWISE_90;
        }));
    }

    @Override
    public boolean isAir() {
        return reference.isAir();
    }

    @Override
    public Item getItem() {
        return MinecraftItem.fromMinecraft(reference.getBlock().asItem());
    }

    @Override
    public BlockState mirror(Axis axis) {
        return switch (axis) {
            case Y -> new MinecraftBlockState(mirrorTopBottom(reference));
            case X -> new MinecraftBlockState(mirrorFrontBack(reference));
            case Z -> new MinecraftBlockState(mirrorLeftRight(reference));
        };
    }

    @Override
    public boolean isReplaceable(Player player, BlockInteraction interaction) {
        return reference.canBeReplaced(new net.minecraft.world.item.context.BlockPlaceContext(
                MinecraftPlayer.toMinecraftPlayer(player),
                MinecraftPrimitives.toMinecraftInteractionHand(interaction.getHand()),
                MinecraftItemStack.toMinecraftItemStack(player.getItemStack(interaction.getHand())),
                MinecraftPrimitives.toMinecraftBlockInteraction(interaction)
        ));
    }

    @Override
    public boolean isReplaceable() {
        return false; // reference.canBeReplaced();
    }

    @Override
    public boolean isDestroyable() {
        return !reference.is(BlockTags.FEATURES_CANNOT_REPLACE);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MinecraftBlockState blockState && reference.equals(blockState.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

}
