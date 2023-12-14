package dev.huskuraft.effortless.building;

import dev.huskuraft.effortless.text.Text;

public enum PositionType {
    ABSOLUTE,
    RELATIVE,
    RELATIVE_ONCE;

    public Text getDisplayName() {
        return switch (this) {
            case ABSOLUTE -> Text.translate("effortless.position.absolute");
            case RELATIVE -> Text.translate("effortless.position.relative");
            case RELATIVE_ONCE -> Text.translate("effortless.position.relative_once");
        };
    }
}