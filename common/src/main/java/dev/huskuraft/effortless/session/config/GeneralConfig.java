package dev.huskuraft.effortless.session.config;

import java.util.List;

import dev.huskuraft.effortless.api.core.ResourceLocation;

public record GeneralConfig(
        Boolean useCommands,
        Boolean allowUseMod,
        Boolean allowBreakBlocks,
        Boolean allowPlaceBlocks,
        Integer maxReachDistance,
        Integer maxDistancePerAxis,
        Integer maxBreakBoxVolume,
        Integer maxPlaceBoxVolume,
        List<ResourceLocation> whitelistedItems,
        List<ResourceLocation> blacklistedItems
) {

    public static final boolean USE_COMMANDS_DEFAULT = false;
    public static final boolean ALLOW_USE_MOD_DEFAULT = true;
    public static final boolean ALLOW_BREAK_BLOCKS_DEFAULT = true;
    public static final boolean ALLOW_PLACE_BLOCKS_DEFAULT = true;

    public static final int MAX_REACH_DISTANCE_DEFAULT = 32;
    public static final int MAX_REACH_DISTANCE_RANGE_START = 1;
    public static final int MAX_REACH_DISTANCE_RANGE_END = 1024;

    public static final int MAX_DISTANCE_PER_AXIS_DEFAULT = 1024;
    public static final int MAX_DISTANCE_PER_AXIS_RANGE_START = 1;
    public static final int MAX_DISTANCE_PER_AXIS_RANGE_END = Short.MAX_VALUE;

    public static final int MAX_BREAK_BOX_VOLUME_DEFAULT = 1024;
    public static final int MAX_BREAK_BOX_VOLUME_RANGE_START = 1;
    public static final int MAX_BREAK_BOX_VOLUME_RANGE_END = Short.MAX_VALUE;

    public static final int MAX_PLACE_BOX_VOLUME_DEFAULT = 1024;
    public static final int MAX_PLACE_BOX_VOLUME_RANGE_START = 1;
    public static final int MAX_PLACE_BOX_VOLUME_RANGE_END = Short.MAX_VALUE;

    public static final List<ResourceLocation> WHITELISTED_ITEMS_DEFAULT = List.of();
    public static final List<ResourceLocation> BLACKLISTED_ITEMS_DEFAULT = List.of();

    public static final GeneralConfig DEFAULT = new GeneralConfig(
            USE_COMMANDS_DEFAULT,
            ALLOW_USE_MOD_DEFAULT,
            ALLOW_BREAK_BLOCKS_DEFAULT,
            ALLOW_PLACE_BLOCKS_DEFAULT,
            MAX_REACH_DISTANCE_DEFAULT,
            MAX_DISTANCE_PER_AXIS_DEFAULT,
            MAX_BREAK_BOX_VOLUME_DEFAULT,
            MAX_PLACE_BOX_VOLUME_DEFAULT,
            WHITELISTED_ITEMS_DEFAULT,
            BLACKLISTED_ITEMS_DEFAULT
    );

    public static final GeneralConfig EMPTY = new GeneralConfig(
            false,
            false,
            false,
            false,
            0,
            0,
            0,
            0,
            List.of(),
            List.of()
    );

    public static final GeneralConfig NULL = new GeneralConfig(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    );
}
