package dev.huskuraft.effortless.api.core;

import java.util.stream.Collectors;

import dev.huskuraft.effortless.api.sound.SoundSet;

public interface BlockState extends StateHolder {

    BlockState mirror(Axis axis);

    BlockState rotate(Revolve revolve);

    boolean isAir();

    Item getItem();

    boolean isReplaceable(Player player, BlockInteraction interaction);

    boolean isReplaceable();

    boolean isDestroyable();

    SoundSet getSoundSet();

    default String getString() {
        return getItem().getId().getString() + getPropertiesString();
    }

    default String getPropertiesString() {
        return "[" + getProperties().stream().map(PropertyHolder::getAsString).collect(Collectors.joining(",")) + "]";
    }

    Block getBlock();

    InteractionResult use(Player player, BlockInteraction blockInteraction);

}
