package dev.huskuraft.effortless;

import dev.huskuraft.effortless.input.Key;
import dev.huskuraft.effortless.input.KeyBinding;
import dev.huskuraft.effortless.input.KeyCodes;
import dev.huskuraft.effortless.text.Text;

enum ActualKeys implements Key {

    SETTINGS("settings", KeyCodes.KEY_UNKNOWN),
    BUILD_MODE_SETTINGS("build_mode_settings", KeyCodes.KEY_UNKNOWN),
    PATTERN_SETTINGS("pattern_settings", KeyCodes.KEY_UNKNOWN),

    BUILD_MODE_RADIAL("build_mode_radial", KeyCodes.KEY_LALT),
    PATTERN_RADIAL("pattern_radial", KeyCodes.KEY_LWIN),

    UNDO("undo", KeyCodes.KEY_LBRACKET),
    REDO("redo", KeyCodes.KEY_RBRACKET),
    //    CYCLE_REPLACE_MODE("cycle_replace", InputConstants.UNKNOWN.getValue()),
    TOGGLE_REPLACE("toggle_replace", KeyCodes.KEY_UNKNOWN),

//    TOGGLE_QUICK_REPLACE("toggle_quick_replace", InputConstants.UNKNOWN.getValue()),
//	  TOGGLE_ALT_PLACE("toggle_alt_place", InputConstants.UNKNOWN.getValue()),
    ;

    private final String description;
    private final KeyCodes key;
    private final boolean modifiable;
    private KeyBinding keyBinding;

    ActualKeys(String description, KeyCodes defaultKey) {
        this.description = "key.effortless.%s.desc".formatted(description);
        this.key = defaultKey;
        this.modifiable = !description.isEmpty();
    }

    public String getName() {
        return description;
    }

    @Override
    public String getCategory() {
        return "key.effortless.category";
    }

    @Override
    public boolean consumeClick() {
        return keyBinding.consumeClick();
    }

    @Override
    public void bindKeyMapping(KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
    }

    @Override
    public KeyCodes getDefaultKey() {
        return key;
    }

    @Override
    public boolean isDown() {
        return keyBinding.isDown();
    }

    @Override
    public boolean isKeyDown() {
        return keyBinding.isKeyDown();
    }

    @Override
    public String getBoundKey() {
        return keyBinding.getBoundKey();
    }

    @Override
    public int getBoundCode() {
        return keyBinding.getBoundCode();
    }

}
