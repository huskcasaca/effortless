package dev.huskuraft.effortless.building.replace;

import java.util.List;

import dev.huskuraft.effortless.Effortless;
import dev.huskuraft.effortless.api.core.BlockItem;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.ItemStack;
import dev.huskuraft.effortless.building.Context;

public record Replace(
        ReplaceMode replaceMode,
        List<ItemStack> replaceList,
        boolean isQuick
) {

    public static final Replace DISABLED = new Replace(ReplaceMode.DISABLED, List.of(), true);
    public static final Replace BLOCKS_AND_AIR = new Replace(ReplaceMode.BLOCKS_AND_AIR, List.of(), true);
    public static final Replace BLOCKS_ONLY = new Replace(ReplaceMode.BLOCKS_ONLY, List.of(), true);
    public static final Replace OFFHAND_ONLY = new Replace(ReplaceMode.OFFHAND_ONLY, List.of(), true);

    public Replace(ReplaceMode replaceMode, List<ItemStack> replaceList) {
        this(replaceMode, replaceList, true);
    }

    public Replace(ReplaceMode replaceMode) {
        this(replaceMode, List.of(), true);
    }

    public Replace withReplaceMode(ReplaceMode replaceMode) {
        return new Replace(replaceMode, replaceList(), isQuick());
    }

    public Replace next() {
        return withReplaceMode(replaceMode().next());
    }

    public boolean shouldIgnore(BlockState blockState) {
        return switch (replaceMode) {
            case DISABLED -> true;
            case BLOCKS_AND_AIR -> blockState.isAir();
            case BLOCKS_ONLY, OFFHAND_ONLY -> false;
        };
    }

    public boolean shouldReplace(Context context, BlockState blockState) {
        if (blockState == null) {
            Effortless.LOGGER.warn("Replacing null block state");
            return true;
        }
        return switch (replaceMode) {
            case DISABLED -> false;
            case BLOCKS_AND_AIR -> true;
            case BLOCKS_ONLY -> blockState.getItem() instanceof BlockItem && !blockState.isAir();
            case OFFHAND_ONLY -> {
                if (context.extras().inventorySnapshot().offhandItems().isEmpty()) {
                    yield blockState.isAir();
                }
                yield context.extras().inventorySnapshot().offhandItems().stream().map(ItemStack::getItem).toList().contains(blockState.getItem());
            }
//            case CUSTOM_LIST_ONLY -> replaceList.stream().map(ItemStack::getItem).toList().contains(blockState.getItem());
        };
    }

}
