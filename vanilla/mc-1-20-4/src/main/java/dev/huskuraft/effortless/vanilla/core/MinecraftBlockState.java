package dev.huskuraft.effortless.vanilla.core;

import java.util.Map;
import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.core.Axis;
import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.InteractionResult;
import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.api.core.Property;
import dev.huskuraft.effortless.api.core.PropertyValue;
import dev.huskuraft.effortless.api.core.Revolve;
import dev.huskuraft.effortless.api.sound.SoundSet;
import dev.huskuraft.effortless.vanilla.sound.MinecraftSoundSet;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;

public record MinecraftBlockState(net.minecraft.world.level.block.state.BlockState reference) implements BlockState {

    public static BlockState ofNullable(net.minecraft.world.level.block.state.BlockState value) {
        return value == null ? null : new MinecraftBlockState(value);
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
    public net.minecraft.world.level.block.state.BlockState referenceValue() {
        return reference;
    }

    @Override
    public BlockState rotate(Revolve revolve) {
        return ofNullable(reference.rotate(switch (revolve) {
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
        return MinecraftItem.ofNullable(reference.getBlock().asItem());
    }

    @Override
    public BlockState mirror(Axis axis) {
        return switch (axis) {
            case Y -> ofNullable(mirrorTopBottom(reference));
            case X -> ofNullable(mirrorFrontBack(reference));
            case Z -> ofNullable(mirrorLeftRight(reference));
        };
    }

    @Override
    public boolean isReplaceable(Player player, BlockInteraction interaction) {
        ItemStack itemStack = player.getItemStack(interaction.getHand());
        return reference.canBeReplaced(new net.minecraft.world.item.context.BlockPlaceContext(
                player.reference(),
                MinecraftConvertor.toPlatformInteractionHand(interaction.getHand()),
                itemStack.reference(),
                MinecraftConvertor.toPlatformBlockInteraction(interaction)
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

    @Override
    public SoundSet getSoundSet() {
        return new MinecraftSoundSet(reference.getSoundType());
    }

    @Override
    public Map<Property, PropertyValue> getPropertiesMap() {
        return reference.getValues().entrySet().stream().collect(Collectors.toMap(entry -> new MinecraftProperty(entry.getKey()), entry -> new MinecraftPropertyValue(entry.getValue())));
    }

    @Override
    public InteractionResult use(Player player, BlockInteraction blockInteraction) {
        return MinecraftConvertor.toPlatformInteractionResult(reference.use(player.getWorld().reference(), player.reference(), MinecraftConvertor.toPlatformInteractionHand(blockInteraction.getHand()), MinecraftConvertor.toPlatformBlockInteraction(blockInteraction)));
    }

}
