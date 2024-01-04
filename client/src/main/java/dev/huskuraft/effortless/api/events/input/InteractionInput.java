package dev.huskuraft.effortless.api.events.input;

import dev.huskuraft.effortless.api.core.InteractionHand;
import dev.huskuraft.effortless.api.core.InteractionType;
import dev.huskuraft.effortless.events.api.EventResult;

public interface InteractionInput {
    EventResult onInteractionInput(InteractionType type, InteractionHand hand);
}
