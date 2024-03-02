package dev.huskuraft.effortless.session;

import java.util.List;

import dev.huskuraft.effortless.api.core.ResourceLocation;

public record BuilderConfig(
        Boolean allowUseMod,
        Boolean allowBreakBlocks,
        Boolean allowPlaceBlocks,
        Integer maxReachDistance,
        Integer maxDistancePerAxis,
        Integer maxBreakBlocks,
        Integer maxPlaceBlocks,
        List<ResourceLocation> whitelistedItems,
        List<ResourceLocation> blacklistedItems
) {

    public static final boolean ALLOW_USE_MOD_DEFAULT = true;
    public static final boolean ALLOW_BREAK_BLOCKS_DEFAULT = true;
    public static final boolean ALLOW_PLACE_BLOCKS_DEFAULT = true;

    public static final int MAX_REACH_DISTANCE_DEFAULT = 32;
    public static final int MAX_REACH_DISTANCE_RANGE_START = 0;
    public static final int MAX_REACH_DISTANCE_RANGE_END = 1024 * 4;

    public static final int MAX_DISTANCE_PER_AXIS_DEFAULT = 32;
    public static final int MAX_DISTANCE_PER_AXIS_RANGE_START = 0;
    public static final int MAX_DISTANCE_PER_AXIS_RANGE_END = 1024;

    public static final int MAX_BREAK_BLOCKS_DEFAULT = 1024;
    public static final int MAX_BREAK_BLOCKS_RANGE_START = 0;
    public static final int MAX_BREAK_BLOCKS_RANGE_END = Short.MAX_VALUE;

    public static final int MAX_PLACE_BLOCKS_DEFAULT = 1024;
    public static final int MAX_PLACE_BLOCKS_RANGE_START = 0;
    public static final int MAX_PLACE_BLOCKS_RANGE_END = Short.MAX_VALUE;

    public static final List<ResourceLocation> WHITELISTED_ITEMS_DEFAULT = List.of();
    public static final List<ResourceLocation> BLACKLISTED_ITEMS_DEFAULT = List.of();

    public static final BuilderConfig DEFAULT = new BuilderConfig(
            BuilderConfig.ALLOW_USE_MOD_DEFAULT,
            BuilderConfig.ALLOW_BREAK_BLOCKS_DEFAULT,
            BuilderConfig.ALLOW_PLACE_BLOCKS_DEFAULT,
            BuilderConfig.MAX_REACH_DISTANCE_DEFAULT,
            BuilderConfig.MAX_DISTANCE_PER_AXIS_DEFAULT,
            BuilderConfig.MAX_BREAK_BLOCKS_DEFAULT,
            BuilderConfig.MAX_PLACE_BLOCKS_DEFAULT,
            BuilderConfig.WHITELISTED_ITEMS_DEFAULT,
            BuilderConfig.BLACKLISTED_ITEMS_DEFAULT
    );
}
