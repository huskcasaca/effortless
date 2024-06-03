package dev.huskuraft.effortless.building.replace;

import java.util.List;

import dev.huskuraft.effortless.api.core.ItemStack;

public record Replace(
        ReplaceStrategy replaceStrategy,
        List<ItemStack> replaceList,
        boolean isQuick
) {

    public static final Replace DISABLED = new Replace(ReplaceStrategy.DISABLED, List.of(), true);
    public static final Replace BLOCKS_AND_AIR = new Replace(ReplaceStrategy.BLOCKS_AND_AIR, List.of(), true);
    public static final Replace BLOCKS_ONLY = new Replace(ReplaceStrategy.BLOCKS_ONLY, List.of(), true);
    public static final Replace OFFHAND_ONLY = new Replace(ReplaceStrategy.OFFHAND_ONLY, List.of(), true);

    public Replace(ReplaceStrategy replaceStrategy, List<ItemStack> replaceList) {
        this(replaceStrategy, replaceList, true);
    }

    public Replace(ReplaceStrategy replaceStrategy) {
        this(replaceStrategy, List.of(), true);
    }

    public Replace withReplaceStrategy(ReplaceStrategy replaceStrategy) {
        return new Replace(replaceStrategy, replaceList(), isQuick());
    }

    public Replace next() {
        return withReplaceStrategy(replaceStrategy().next());
    }

}
