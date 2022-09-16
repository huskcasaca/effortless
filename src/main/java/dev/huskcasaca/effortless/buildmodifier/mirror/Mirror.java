package dev.huskcasaca.effortless.buildmodifier.mirror;

import dev.huskcasaca.effortless.buildmodifier.Modifier;
import dev.huskcasaca.effortless.buildmodifier.ModifierSettingsManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class Mirror implements Modifier {

    public static List<BlockPos> findCoordinates(Player player, BlockPos startPos) {
        List<BlockPos> coordinates = new ArrayList<>();

        //find mirrorsettings for the player
        MirrorSettings m = ModifierSettingsManager.getModifierSettings(player).getMirrorSettings();
        if (!isEnabled(m, startPos)) return coordinates;

        if (m.mirrorX) coordinateMirrorX(m, startPos, coordinates);
        if (m.mirrorY) coordinateMirrorY(m, startPos, coordinates);
        if (m.mirrorZ) coordinateMirrorZ(m, startPos, coordinates);

        return coordinates;
    }

    private static void coordinateMirrorX(MirrorSettings m, BlockPos oldBlockPos, List<BlockPos> coordinates) {
        //find mirror position
        double x = m.position.x + (m.position.x - oldBlockPos.getX() - 0.5);
        BlockPos newBlockPos = new BlockPos(x, oldBlockPos.getY(), oldBlockPos.getZ());
        coordinates.add(newBlockPos);

        if (m.mirrorY) coordinateMirrorY(m, newBlockPos, coordinates);
        if (m.mirrorZ) coordinateMirrorZ(m, newBlockPos, coordinates);
    }

    private static void coordinateMirrorY(MirrorSettings m, BlockPos oldBlockPos, List<BlockPos> coordinates) {
        //find mirror position
        double y = m.position.y + (m.position.y - oldBlockPos.getY() - 0.5);
        BlockPos newBlockPos = new BlockPos(oldBlockPos.getX(), y, oldBlockPos.getZ());
        coordinates.add(newBlockPos);

        if (m.mirrorZ) coordinateMirrorZ(m, newBlockPos, coordinates);
    }

    private static void coordinateMirrorZ(MirrorSettings m, BlockPos oldBlockPos, List<BlockPos> coordinates) {
        //find mirror position
        double z = m.position.z + (m.position.z - oldBlockPos.getZ() - 0.5);
        BlockPos newBlockPos = new BlockPos(oldBlockPos.getX(), oldBlockPos.getY(), z);
        coordinates.add(newBlockPos);
    }

    public static List<BlockState> findBlockStates(Player player, BlockPos startPos, BlockState blockState, ItemStack itemStack, List<ItemStack> itemStacks) {
        List<BlockState> blockStates = new ArrayList<>();

        //find mirrorsettings for the player
        MirrorSettings m = ModifierSettingsManager.getModifierSettings(player).getMirrorSettings();
        if (!isEnabled(m, startPos)) return blockStates;

        //Randomizer bag synergy
//		AbstractRandomizerBagItem randomizerBagItem = null;
        Container bagInventory = null;
//		if (!itemStack.isEmpty() && itemStack.getItem() instanceof AbstractRandomizerBagItem) {
//			randomizerBagItem = (AbstractRandomizerBagItem) itemStack.getItem() ;
//			bagInventory = randomizerBagItem.getBagInventory(itemStack);
//		}

        if (m.mirrorX)
            blockStateMirrorX(player, m, startPos, blockState, bagInventory, itemStack, InteractionHand.MAIN_HAND, blockStates, itemStacks);
        if (m.mirrorY)
            blockStateMirrorY(player, m, startPos, blockState, bagInventory, itemStack, InteractionHand.MAIN_HAND, blockStates, itemStacks);
        if (m.mirrorZ)
            blockStateMirrorZ(player, m, startPos, blockState, bagInventory, itemStack, InteractionHand.MAIN_HAND, blockStates, itemStacks);

        return blockStates;
    }

    private static void blockStateMirrorX(Player player, MirrorSettings m, BlockPos oldBlockPos, BlockState oldBlockState,
                                          Container bagInventory, ItemStack itemStack, InteractionHand hand, List<BlockState> blockStates, List<ItemStack> itemStacks) {
        //find mirror position
        double x = m.position.x + (m.position.x - oldBlockPos.getX() - 0.5);
        BlockPos newBlockPos = new BlockPos(x, oldBlockPos.getY(), oldBlockPos.getZ());

        //Randomizer bag synergy
//		if (bagInventory != null) {
//			itemStack = ((AbstractRandomizerBagItem)itemStack.getItem()).pickRandomStack(bagInventory);
//			oldBlockState = BuildModifiers.getBlockStateFromItem(itemStack, player, oldBlockPos, Direction.UP, new Vec3(0, 0, 0), hand);
//		}

        //Find blockstate
        BlockState newBlockState = oldBlockState == null ? null : oldBlockState.mirror(net.minecraft.world.level.block.Mirror.FRONT_BACK);

        //Store blockstate and itemstack
        blockStates.add(newBlockState);
        itemStacks.add(itemStack);

        if (m.mirrorY)
            blockStateMirrorY(player, m, newBlockPos, newBlockState, bagInventory, itemStack, hand, blockStates, itemStacks);
        if (m.mirrorZ)
            blockStateMirrorZ(player, m, newBlockPos, newBlockState, bagInventory, itemStack, hand, blockStates, itemStacks);
    }

    private static void blockStateMirrorY(Player player, MirrorSettings m, BlockPos oldBlockPos, BlockState oldBlockState,
                                          Container bagInventory, ItemStack itemStack, InteractionHand hand, List<BlockState> blockStates, List<ItemStack> itemStacks) {
        //find mirror position
        double y = m.position.y + (m.position.y - oldBlockPos.getY() - 0.5);
        BlockPos newBlockPos = new BlockPos(oldBlockPos.getX(), y, oldBlockPos.getZ());

        //Randomizer bag synergy
//		if (bagInventory != null) {
//			itemStack = ((AbstractRandomizerBagItem)itemStack.getItem()).pickRandomStack(bagInventory);
//			oldBlockState = BuildModifiers.getBlockStateFromItem(itemStack, player, oldBlockPos, Direction.UP, new Vec3(0, 0, 0), hand);
//		}

        //Find blockstate
        BlockState newBlockState = oldBlockState == null ? null : getVerticalMirror(oldBlockState);

        //Store blockstate and itemstack
        blockStates.add(newBlockState);
        itemStacks.add(itemStack);

        if (m.mirrorZ)
            blockStateMirrorZ(player, m, newBlockPos, newBlockState, bagInventory, itemStack, hand, blockStates, itemStacks);
    }

    private static void blockStateMirrorZ(Player player, MirrorSettings m, BlockPos oldBlockPos, BlockState oldBlockState,
                                          Container bagInventory, ItemStack itemStack, InteractionHand hand, List<BlockState> blockStates, List<ItemStack> itemStacks) {
        //find mirror position
        double z = m.position.z + (m.position.z - oldBlockPos.getZ() - 0.5);
        BlockPos newBlockPos = new BlockPos(oldBlockPos.getX(), oldBlockPos.getY(), z);

        //Randomizer bag synergy
//		if (bagInventory != null) {
//			itemStack = ((AbstractRandomizerBagItem)itemStack.getItem()).pickRandomStack(bagInventory);
//			oldBlockState = BuildModifiers.getBlockStateFromItem(itemStack, player, oldBlockPos, Direction.UP, new Vec3(0, 0, 0), hand);
//		}

        //Find blockstate
        BlockState newBlockState = oldBlockState == null ? null : oldBlockState.mirror(net.minecraft.world.level.block.Mirror.LEFT_RIGHT);

        //Store blockstate and itemstack
        blockStates.add(newBlockState);
        itemStacks.add(itemStack);
    }

    public static boolean isEnabled(MirrorSettings m, BlockPos startPos) {
        if (m == null || !m.enabled || (!m.mirrorX && !m.mirrorY && !m.mirrorZ)) return false;

        //within mirror distance
        return !(startPos.getX() + 0.5 < m.position.x - m.radius) && !(startPos.getX() + 0.5 > m.position.x + m.radius) &&
                !(startPos.getY() + 0.5 < m.position.y - m.radius) && !(startPos.getY() + 0.5 > m.position.y + m.radius) &&
                !(startPos.getZ() + 0.5 < m.position.z - m.radius) && !(startPos.getZ() + 0.5 > m.position.z + m.radius);
    }

    private static BlockState getVerticalMirror(BlockState blockState) {
        //Stairs
        if (blockState.getBlock() instanceof StairBlock) {
            if (blockState.getValue(StairBlock.HALF) == Half.BOTTOM) {
                return blockState.setValue(StairBlock.HALF, Half.TOP);
            } else {
                return blockState.setValue(StairBlock.HALF, Half.BOTTOM);
            }
        }

        //Slabs
        if (blockState.getBlock() instanceof SlabBlock) {
            if (blockState.getValue(SlabBlock.TYPE) == SlabType.DOUBLE) {
                return blockState;
            } else if (blockState.getValue(SlabBlock.TYPE) == SlabType.BOTTOM) {
                return blockState.setValue(SlabBlock.TYPE, SlabType.TOP);
            } else {
                return blockState.setValue(SlabBlock.TYPE, SlabType.BOTTOM);
            }
        }

        //Buttons, endrod, observer, piston
        if (blockState.getBlock() instanceof DirectionalBlock) {
            if (blockState.getValue(DirectionalBlock.FACING) == Direction.DOWN) {
                return blockState.setValue(DirectionalBlock.FACING, Direction.UP);
            } else if (blockState.getValue(DirectionalBlock.FACING) == Direction.UP) {
                return blockState.setValue(DirectionalBlock.FACING, Direction.DOWN);
            }
        }

        //Dispenser, dropper
        if (blockState.getBlock() instanceof DispenserBlock) {
            if (blockState.getValue(DispenserBlock.FACING) == Direction.DOWN) {
                return blockState.setValue(DispenserBlock.FACING, Direction.UP);
            } else if (blockState.getValue(DispenserBlock.FACING) == Direction.UP) {
                return blockState.setValue(DispenserBlock.FACING, Direction.DOWN);
            }
        }

        return blockState;
    }

    public static class MirrorSettings {
        public boolean enabled = false;
        public Vec3 position = new Vec3(0.5, 64.5, 0.5);
        public boolean mirrorX = true, mirrorY = false, mirrorZ = false;
        public int radius = 10;
        public boolean drawLines = true, drawPlanes = true;

        public MirrorSettings() {
        }

        public MirrorSettings(boolean mirrorEnabled, Vec3 position, boolean mirrorX, boolean mirrorY, boolean mirrorZ, int radius, boolean drawLines, boolean drawPlanes) {
            this.enabled = mirrorEnabled;
            this.position = position;
            this.mirrorX = mirrorX;
            this.mirrorY = mirrorY;
            this.mirrorZ = mirrorZ;
            this.radius = radius;
            this.drawLines = drawLines;
            this.drawPlanes = drawPlanes;
        }

        public int getReach() {
            return radius * 2; //Change ModifierSettings#setReachUpgrade too
        }
    }
}
