package dev.huskuraft.effortless.building;

import dev.huskuraft.universal.api.text.ChatFormatting;
import dev.huskuraft.universal.api.text.Text;
import dev.huskuraft.effortless.building.structure.BuildMode;

public enum BuildState {
    IDLE,
    BREAK_BLOCK,
    PLACE_BLOCK,
    INTERACT_BLOCK,
    COPY_STRUCTURE,
    PASTE_STRUCTURE;

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
                    case COPY_STRUCTURE -> "copying_structure";
                    case PASTE_STRUCTURE -> "pasting_structure";
                }
        ));
    }

    public Text getDisplayName(BuildMode buildMode) {
        return Text.translate("effortless.state.%s.build_mode".formatted(
                switch (this) {
                    case IDLE -> "idle";
                    case BREAK_BLOCK -> "breaking_block";
                    case PLACE_BLOCK -> "placing_block";
                    case INTERACT_BLOCK -> "interacting_block";
                    case COPY_STRUCTURE -> "copying_structure";
                    case PASTE_STRUCTURE -> "pasting_structure";
                }
        ), buildMode.getDisplayName().withStyle(ChatFormatting.GOLD));
    }

}
