package dev.huskuraft.effortless.session.config;

import java.util.List;

import dev.huskuraft.effortless.api.core.ResourceLocation;

public record GeneralConfig(
        Boolean useCommands,
        Boolean allowUseMod,
        Boolean allowBreakBlocks,
        Boolean allowPlaceBlocks,
        Integer maxReachDistance,
        Integer maxBoxVolumePerBreak,
        Integer maxBoxVolumePerPlace,
        Integer maxBoxSideLengthPerBreak,
        Integer maxBoxSideLengthPerPlace,
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

    public static final int MAX_BOX_SIDE_LENGTH_PER_BREAK_DEFAULT = 1024;
    public static final int MAX_BOX_SIDE_LENGTH_PER_BREAK_RANGE_START = 1;
    public static final int MAX_BOX_SIDE_LENGTH_PER_BREAK_RANGE_END = Short.MAX_VALUE;

    public static final int MAX_BOX_SIDE_LENGTH_PER_PLACE_DEFAULT = 1024;
    public static final int MAX_BOX_SIDE_LENGTH_PER_PLACE_RANGE_START = 1;
    public static final int MAX_BOX_SIDE_LENGTH_PER_PLACE_RANGE_END = Short.MAX_VALUE;

    public static final int MAX_BOX_VOLUME_PER_BREAK_DEFAULT = 1024;
    public static final int MAX_BOX_VOLUME_PER_BREAK_RANGE_START = 1;
    public static final int MAX_BOX_VOLUME_PER_BREAK_RANGE_END = Short.MAX_VALUE;

    public static final int MAX_BOX_VOLUME_PER_PLACE_DEFAULT = 1024;
    public static final int MAX_BOX_VOLUME_PER_PLACE_RANGE_START = 1;
    public static final int MAX_BOX_VOLUME_PER_PLACE_RANGE_END = Short.MAX_VALUE;

    public static final List<ResourceLocation> WHITELISTED_ITEMS_DEFAULT = List.of();
    public static final List<ResourceLocation> BLACKLISTED_ITEMS_DEFAULT = List.of();

    public static final GeneralConfig DEFAULT = new GeneralConfig(
            USE_COMMANDS_DEFAULT,
            ALLOW_USE_MOD_DEFAULT,
            ALLOW_BREAK_BLOCKS_DEFAULT,
            ALLOW_PLACE_BLOCKS_DEFAULT,
            MAX_REACH_DISTANCE_DEFAULT,
            MAX_BOX_VOLUME_PER_BREAK_DEFAULT,
            MAX_BOX_VOLUME_PER_PLACE_DEFAULT,
            MAX_BOX_SIDE_LENGTH_PER_BREAK_DEFAULT,
            MAX_BOX_SIDE_LENGTH_PER_PLACE_DEFAULT,
            WHITELISTED_ITEMS_DEFAULT,
            BLACKLISTED_ITEMS_DEFAULT);

    public static final GeneralConfig EMPTY = new GeneralConfig(
            false,
            false,
            false,
            false,
            0,
            0,
            0,
            0,
            0,
            List.of(),
            List.of());

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
            null,
            null);
}
