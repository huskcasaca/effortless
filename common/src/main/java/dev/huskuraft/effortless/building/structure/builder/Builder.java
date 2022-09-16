package dev.huskuraft.effortless.building.structure.builder;

import dev.huskuraft.effortless.building.Context;

public interface Builder extends Traceable, BlockPosCollector {

    int totalClicks(Context context);

}
