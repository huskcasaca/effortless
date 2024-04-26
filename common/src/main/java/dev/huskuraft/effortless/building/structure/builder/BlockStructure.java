package dev.huskuraft.effortless.building.structure.builder;

import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;

public interface BlockStructure {

    int totalClicks(Context context);

    BlockInteraction trace(Player player, Context context);

    Stream<BlockPosition> collect(Context context);

}
