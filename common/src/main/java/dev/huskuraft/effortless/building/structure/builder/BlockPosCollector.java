package dev.huskuraft.effortless.building.structure.builder;

import dev.huskuraft.effortless.api.core.BlockPosition;
import dev.huskuraft.effortless.building.Context;

import java.util.stream.Stream;

public interface BlockPosCollector {

    Stream<BlockPosition> collect(Context context);

}
