package dev.huskuraft.effortless.building.replace;

import java.util.List;

import dev.huskuraft.effortless.api.core.ItemStack;

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

}
