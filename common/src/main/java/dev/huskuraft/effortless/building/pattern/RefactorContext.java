package dev.huskuraft.effortless.building.pattern;

import java.util.Objects;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockState;
import dev.huskuraft.effortless.api.core.Item;
import dev.huskuraft.effortless.api.core.Player;

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
