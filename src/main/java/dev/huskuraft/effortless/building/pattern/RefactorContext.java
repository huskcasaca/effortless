package dev.huskuraft.effortless.building.pattern;

import dev.huskuraft.universal.api.core.BlockEntity;
import dev.huskuraft.universal.api.core.BlockInteraction;
import dev.huskuraft.universal.api.core.BlockItem;
import dev.huskuraft.universal.api.core.BlockState;
import dev.huskuraft.universal.api.core.Item;
import dev.huskuraft.universal.api.core.Player;

public class RefactorContext {

    private final Item item;

    public RefactorContext(Item item) {
        this.item = item;
    }

    public static RefactorContext of(Item item) {
        return new RefactorContext(item);
    }

    public BlockState refactor(Player player, BlockInteraction blockInteraction) {
        if (item == null) {
            return null;
        }
        if (item instanceof BlockItem blockItem) {
            return blockItem.getPlacementState(player, blockInteraction);
        }
        return null;
    }

    public BlockEntity refactor(BlockEntity blockEntity, BlockInteraction blockInteraction) {
        return blockEntity;
    }

}
