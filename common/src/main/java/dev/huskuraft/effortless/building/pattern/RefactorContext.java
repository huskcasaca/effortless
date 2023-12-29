package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.BlockState;
import dev.huskuraft.effortless.core.Item;
import dev.huskuraft.effortless.core.Player;

import java.util.Objects;

public class RefactorContext {

    private final Item item;

    public RefactorContext(Item item) {
        this.item = item;
    }

    public static RefactorContext of(Item item) {
        return new RefactorContext(Objects.requireNonNull(item));
    }

    public BlockState refactor(Player player, BlockInteraction blockInteraction) {
        return item.getDefaultStack().getBlockState(player, blockInteraction);
    }

}
