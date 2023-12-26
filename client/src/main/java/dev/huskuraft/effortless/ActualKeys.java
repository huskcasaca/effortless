package dev.huskuraft.effortless;

import dev.huskuraft.effortless.input.Key;
import dev.huskuraft.effortless.input.KeyBinding;
import dev.huskuraft.effortless.input.KeyCodes;

enum ActualKeys implements Key {

    SETTINGS("settings", KeyCodes.KEY_UNKNOWN),
    BUILD_MODE_SETTINGS("build_mode_settings", KeyCodes.KEY_UNKNOWN),
    PATTERN_SETTINGS("pattern_settings", KeyCodes.KEY_UNKNOWN),

    BUILD_MODE_RADIAL("build_mode_radial", KeyCodes.KEY_LEFT_ALT),
    PATTERN_RADIAL("pattern_radial", KeyCodes.KEY_LEFT_SUPER),

    UNDO("undo", KeyCodes.KEY_LEFT_BRACKET),
    REDO("redo", KeyCodes.KEY_RIGHT_BRACKET),
    //    CYCLE_REPLACE_MODE("cycle_replace", InputConstants.UNKNOWN.getValue()),
    TOGGLE_REPLACE("toggle_replace", KeyCodes.KEY_UNKNOWN),

//    TOGGLE_QUICK_REPLACE("toggle_quick_replace", InputConstants.UNKNOWN.getValue()),
//	  TOGGLE_ALT_PLACE("toggle_alt_place", InputConstants.UNKNOWN.getValue()),
    ;

    private final String description;
    private final KeyCodes key;
    private final boolean modifiable;
    private final KeyBinding keyBinding;

    ActualKeys(String description, KeyCodes defaultKey) {
        this.description = "key.effortless.%s.desc".formatted(description);
        this.key = defaultKey;
        this.modifiable = !description.isEmpty();
        this.keyBinding = EffortlessClient.getInstance().getPlatform().newKeyBinding(description, "key.effortless.category", key);
    }

    @Override
    public KeyBinding getBinding() {
        return keyBinding;
    }
}
