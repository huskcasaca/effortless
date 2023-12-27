package dev.huskuraft.effortless.vanilla.adapters;

import dev.huskuraft.effortless.core.*;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;

public class MinecraftBlockData extends BlockData {

    private final BlockState reference;

    MinecraftBlockData(BlockState reference) {
        this.reference = reference;
    }

    public static BlockData fromMinecraftBlockData(BlockState value) {
        return new MinecraftBlockData(value);
    }

    public static BlockState toMinecraftBlockData(BlockData value) {
        return ((MinecraftBlockData) value).reference;
    }

    public static BlockState mirrorTopBottom(BlockState value) {
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

    public static BlockState mirrorLeftRight(BlockState value) {
        return value.mirror(Mirror.LEFT_RIGHT);
    }

    public static BlockState mirrorFrontBack(BlockState value) {
        return value.mirror(Mirror.FRONT_BACK);
    }

    @Override
    public BlockData rotate(Revolve revolve) {
        return new MinecraftBlockData(reference.rotate(switch (revolve) {
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
    public boolean equals(Object obj) {
        return obj instanceof MinecraftBlockData fabricBlockState && reference.equals(fabricBlockState.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

    @Override
    public BlockData mirror(Axis axis) {
        return switch (axis) {
            case Y -> new MinecraftBlockData(mirrorTopBottom(reference));
            case X -> new MinecraftBlockData(mirrorFrontBack(reference));
            case Z -> new MinecraftBlockData(mirrorLeftRight(reference));
        };
    }

    @Override
    public boolean isReplaceable(Player player, BlockInteraction interaction) {
        return reference.canBeReplaced(new net.minecraft.world.item.context.BlockPlaceContext(
                MinecraftPlayer.toMinecraftPlayer(player),
                MinecraftPlayer.toMinecraftInteractionHand(interaction.getHand()),
                MinecraftItemStack.toMinecraftItemStack(player.getItemStack(interaction.getHand())),
                MinecraftPlayer.toMinecraftBlockInteraction(interaction)
        ));
    }

    @Override
    public boolean isReplaceable() {
        return reference.canBeReplaced();
    }

    @Override
    public boolean isDestroyable() {
        return !reference.is(BlockTags.FEATURES_CANNOT_REPLACE);
    }
}
