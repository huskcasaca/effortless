package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.BlockData;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.core.ItemStack;

public class RefactorContext {

    private final ItemStack itemStack;

    public RefactorContext(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static RefactorContext of(ItemStack itemStack) {
        return new RefactorContext(itemStack);
    }

    public BlockData refactor(Player player, BlockInteraction blockInteraction) {
        return itemStack.getBlockData(player, blockInteraction);
    }

}
