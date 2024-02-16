package dev.huskuraft.effortless.api.input;

public enum Keys implements Key {
    KEY_ESCAPE(KeyCodes.KEY_ESCAPE),
    KEY_ENTER(KeyCodes.KEY_ENTER),
    KEY_LEFT_CONTROL(KeyCodes.KEY_LEFT_CONTROL),
    ;

    private final KeyBinding keyBinding;

    Keys(KeyCodes key) {
        this.keyBinding = KeyBinding.of(key.value());
    }

    @Override
    public KeyBinding getBinding() {
        return keyBinding;
    }
}
