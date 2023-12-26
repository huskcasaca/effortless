package dev.huskuraft.effortless.events.player;

import dev.huskuraft.effortless.core.InteractionHand;
import dev.huskuraft.effortless.core.InteractionType;
import dev.huskuraft.effortless.events.api.EventResult;

public interface InteractionInput {
    EventResult onInteractionInput(InteractionType type, InteractionHand hand);
}
