package dev.huskuraft.effortless.session.config;

import java.util.List;

import dev.huskuraft.effortless.api.core.ResourceLocation;

public record ConstraintConfig(
        Boolean useCommands,
        Boolean allowUseMod,
        Boolean allowBreakBlocks,
        Boolean allowPlaceBlocks,
        Boolean allowInteractBlocks,
        Boolean useCorrectTools,
        Integer maxReachDistance,
        Integer maxBlockBreakVolume,
        Integer maxBlockPlaceVolume,
        List<ResourceLocation> whitelistedItems,
        List<ResourceLocation> blacklistedItems
) {

    public boolean allowCopyPasteBlocks() {
        return true;
    }

    public static final boolean USE_COMMANDS_DEFAULT = false;
    public static final boolean ALLOW_USE_MOD_DEFAULT = true;
    public static final boolean ALLOW_BREAK_BLOCKS_DEFAULT = true;
    public static final boolean ALLOW_PLACE_BLOCKS_DEFAULT = true;
    public static final boolean ALLOW_INTERACT_BLOCKS_DEFAULT = true;
    public static final boolean USE_CORRECT_TOOLS_DEFAULT = true;

    public static final int MAX_REACH_DISTANCE_DEFAULT = 128;
    public static final int MAX_REACH_DISTANCE_RANGE_START = 0;
    public static final int MAX_REACH_DISTANCE_RANGE_END = Short.MAX_VALUE;

    public static final int MAX_BOX_VOLUME_PER_BREAK_DEFAULT = 1000 * 1000;
    public static final int MAX_BOX_VOLUME_PER_BREAK_RANGE_START = 0;
    public static final int MAX_BOX_VOLUME_PER_BREAK_RANGE_END = Integer.MAX_VALUE;

    public static final int MAX_BOX_VOLUME_PER_PLACE_DEFAULT = 1000 * 1000;
    public static final int MAX_BOX_VOLUME_PER_PLACE_RANGE_START = 0;
    public static final int MAX_BOX_VOLUME_PER_PLACE_RANGE_END = Integer.MAX_VALUE;

    public static final List<ResourceLocation> WHITELISTED_ITEMS_DEFAULT = List.of();
    public static final List<ResourceLocation> BLACKLISTED_ITEMS_DEFAULT = List.of();

    public static final ConstraintConfig DEFAULT = new ConstraintConfig(
            USE_COMMANDS_DEFAULT,
            ALLOW_USE_MOD_DEFAULT,
            ALLOW_BREAK_BLOCKS_DEFAULT,
            ALLOW_PLACE_BLOCKS_DEFAULT,
            ALLOW_INTERACT_BLOCKS_DEFAULT,
            USE_CORRECT_TOOLS_DEFAULT,
            MAX_REACH_DISTANCE_DEFAULT,
            MAX_BOX_VOLUME_PER_BREAK_DEFAULT,
            MAX_BOX_VOLUME_PER_PLACE_DEFAULT,
            WHITELISTED_ITEMS_DEFAULT,
            BLACKLISTED_ITEMS_DEFAULT
    );

    public static final ConstraintConfig EMPTY = new ConstraintConfig(
            false,
            false,
            false,
            false,
            false,
            false,
            0,
            0,
            0,
            List.of(),
            List.of()
    );

    public static final ConstraintConfig NULL = new ConstraintConfig(
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
            null
    );
}
