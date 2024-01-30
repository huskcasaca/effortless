package dev.huskuraft.effortless.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class InteractionInputEvents {

    public static final Event<Attack> ATTACK = EventFactory.createArrayBacked(Attack.class, callbacks -> (player, hand) -> {
        for (var callback : callbacks) {
            if (callback.onAttack(player, hand)) {
                return true;
            }
        }
        return false;
    });

    public static final Event<UseItem> USE_ITEM = EventFactory.createArrayBacked(UseItem.class, callbacks -> (player, hand) -> {
        for (var callback : callbacks) {
            if (callback.onUseItem(player, hand)) {
                return true;
            }
        }
        return false;
    });

    public static final Event<PickBlock> PICK_BLOCK = EventFactory.createArrayBacked(PickBlock.class, callbacks -> (player, hand) -> {
        for (var callback : callbacks) {
            if (callback.onPickBlock(player, hand)) {
                return true;
            }
        }
        return false;
    });

    @FunctionalInterface
    public interface Attack {
        boolean onAttack(Player player, InteractionHand hand);
    }

    @FunctionalInterface
    public interface UseItem {
        boolean onUseItem(Player player, InteractionHand hand);
    }

    @FunctionalInterface
    public interface PickBlock {
        boolean onPickBlock(Player player, InteractionHand hand);
    }

}