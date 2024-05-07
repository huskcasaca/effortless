package dev.huskuraft.effortless.building.structure.builder.standard;

import java.util.stream.Stream;

import dev.huskuraft.effortless.api.core.BlockInteraction;
import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.api.core.Player;
import dev.huskuraft.effortless.building.Context;
import dev.huskuraft.effortless.building.structure.builder.AbstractBlockStructure;

public class Disable extends AbstractBlockStructure {

    protected BlockInteraction trace(Player player, Context context, int index) {
        return null;
    }

    protected Stream<BlockPosition> collect(Context context, int index) {
        return Stream.empty();
    }

    @Override
    public int traceSize(Context context) {
        return 1;
    }

}
