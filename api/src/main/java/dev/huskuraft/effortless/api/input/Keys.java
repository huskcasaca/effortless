package dev.huskuraft.effortless.api.input;

import java.util.Locale;

import dev.huskuraft.effortless.api.lang.Lang;

public enum Keys implements Key {
    KEY_ESCAPE(KeyCodes.KEY_ESCAPE),
    KEY_ENTER(KeyCodes.KEY_ENTER),
    KEY_LEFT_SHIFT(KeyCodes.KEY_LEFT_SHIFT),
    KEY_LEFT_CONTROL(KeyCodes.KEY_LEFT_CONTROL),
    KEY_RIGHT_SHIFT(KeyCodes.KEY_RIGHT_SHIFT),
    KEY_RIGHT_CONTROL(KeyCodes.KEY_RIGHT_CONTROL),
    ;

    private final KeyBinding keyBinding;

    Keys(KeyCodes key) {
        this.keyBinding = new KeyBinding() {
            @Override
            public String getName() {
                return Lang.asKeyDesc(getName().toLowerCase(Locale.ROOT).replace("key_", ""));
            }

            @Override
            public String getCategory() {
                return null;
            }

            @Override
            public int getDefaultKey() {
                return key.value();
            }

            @Override
            public boolean consumeClick() {
                return false;
            }

            @Override
            public boolean isDown() {
                return isKeyDown();
            }

            @Override
            public int getBoundCode() {
                return key.value();
            }

            @Override
            public Object refs() {
                return this;
            }
        };
    }

    @Override
    public KeyBinding getBinding() {
        return keyBinding;
    }

}
