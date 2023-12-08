package dev.huskuraft.effortless.events.player;

import dev.huskuraft.effortless.core.InteractionHand;
import dev.huskuraft.effortless.core.InteractionType;
import dev.huskuraft.effortless.core.Player;
import dev.huskuraft.effortless.events.api.EventResult;

public interface ClientPlayerInteract {
    EventResult onClientPlayerInteract(Player player, InteractionType type, InteractionHand hand);
}
