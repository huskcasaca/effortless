package dev.huskuraft.effortless;

import dev.huskuraft.effortless.api.input.Key;
import dev.huskuraft.effortless.api.input.KeyBinding;
import dev.huskuraft.effortless.api.input.KeyCodes;

enum EffortlessKeys implements Key {

    SETTINGS("settings", KeyCodes.KEY_UNKNOWN),
    BUILD_MODE_SETTINGS("build_mode_settings", KeyCodes.KEY_UNKNOWN),
    PATTERN_SETTINGS("pattern_settings", KeyCodes.KEY_UNKNOWN),

    BUILD_MODE_RADIAL("build_mode_radial", KeyCodes.KEY_LEFT_ALT),
    PATTERN_RADIAL("pattern_radial", KeyCodes.KEY_LEFT_SUPER),

    UNDO("undo", KeyCodes.KEY_LEFT_BRACKET),
    REDO("redo", KeyCodes.KEY_RIGHT_BRACKET),
    //    CYCLE_REPLACE_MODE("cycle_replace", KeyCodes.KEY_UNKNOWN),
    TOGGLE_REPLACE("toggle_replace", KeyCodes.KEY_UNKNOWN),

//    TOGGLE_QUICK_REPLACE("toggle_quick_replace", KeyCodes.KEY_UNKNOWN),
//	  TOGGLE_ALT_PLACE("toggle_alt_place", KeyCodes.KEY_UNKNOWN),
    ;

    private final KeyCodes key;
    private final KeyBinding keyBinding;

    EffortlessKeys(String description, KeyCodes defaultKey) {
        this.key = defaultKey;
        this.keyBinding = EffortlessClient.getInstance().getPlatform().newKeyBinding("key.effortless.%s.desc".formatted(description), "key.effortless.category", key);
    }

    @Override
    public KeyBinding getBinding() {
        return keyBinding;
    }
}
