package dev.huskuraft.effortless.building.replace;

import java.util.List;

import dev.huskuraft.effortless.api.core.ItemStack;

public record Replace(
        ReplaceStrategy replaceStrategy,
        List<ItemStack> replaceList,
        boolean isQuick
) {

    public static final Replace DISABLED = new Replace(ReplaceStrategy.DISABLED, List.of(), false);

    public Replace withReplaceStrategy(ReplaceStrategy replaceStrategy) {
        return new Replace(replaceStrategy, replaceList(), isQuick());
    }

    public Replace withReplaceMode(ReplaceMode replaceMode) {
        return new Replace(replaceStrategy(), replaceList(), replaceMode == ReplaceMode.QUICK);
    }

    public Replace next() {
        return withReplaceStrategy(replaceStrategy().next());
    }

}
