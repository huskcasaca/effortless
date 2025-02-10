package dev.huskuraft.effortless.building.replace;

import java.util.List;

import dev.huskuraft.universal.api.core.ItemStack;
import dev.huskuraft.effortless.building.Option;

public record Replace(
        ReplaceStrategy replaceStrategy,
        List<ItemStack> replaceList,
        boolean isQuick
) implements Option  {

    public static final Replace DISABLED = new Replace(ReplaceStrategy.DISABLED, List.of(), false);

    public Replace withReplaceStrategy(ReplaceStrategy replaceStrategy) {
        return new Replace(replaceStrategy, replaceList(), isQuick());
    }

    public Replace withReplaceMode(ReplaceMode replaceMode) {
        return new Replace(replaceStrategy(), replaceList(), replaceMode == ReplaceMode.QUICK);
    }

    public Replace withQuick(boolean isQuick) {
        return new Replace(replaceStrategy(), replaceList(), isQuick);
    }

    public Replace next() {
        return withReplaceStrategy(replaceStrategy().next());
    }

    @Override
    public String getName() {
        return replaceStrategy().getName();
    }

    @Override
    public String getCategory() {
        return isQuick ? "quick_replace" : "replace";
    }
}
