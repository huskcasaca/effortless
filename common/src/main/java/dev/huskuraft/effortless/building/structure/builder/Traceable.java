package dev.huskuraft.effortless.building.structure.builder;

import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.core.BlockInteraction;
import dev.huskuraft.effortless.core.Player;

public interface Traceable {

    BlockInteraction trace(Player player, Context context);

}
