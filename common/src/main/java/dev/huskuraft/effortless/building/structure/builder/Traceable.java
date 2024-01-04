package dev.huskuraft.effortless.building.structure.builder;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;

public interface Traceable {

    BlockInteraction trace(Player player, Context context);

}
