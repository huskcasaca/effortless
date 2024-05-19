package dev.huskuraft.effortless.building;

import dev.huskuraft.effortless.api.text.Text;

public enum BuildState {
    IDLE,
    BREAK_BLOCK,
    PLACE_BLOCK,
    INTERACT_BLOCK;

    public boolean isIdle() {
        return this == IDLE;
    }

    public Text getDisplayName() {
        return Text.translate("effortless.state.%s".formatted(
                switch (this) {
                    case IDLE -> "idle";
                    case BREAK_BLOCK -> "breaking_block";
                    case PLACE_BLOCK -> "placing_block";
                    case INTERACT_BLOCK -> "interacting_block";
                }
        ));
    }

}
