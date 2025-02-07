package dev.ftb.mods.ftbteams.api.property;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;

import java.util.regex.Pattern;

/**
 * These are the standard team properties which are registered for every team. Other mods may add additional properties;
 * see {@link dev.ftb.mods.ftbteams.api.event.TeamCollectPropertiesEvent}.
 */
public class TeamProperties {
    public static final StringProperty DISPLAY_NAME = new StringProperty(FTBTeamsAPI.rl("display_name"), "", Pattern.compile(".{3,}"));
    public static final StringProperty DESCRIPTION = new StringProperty(FTBTeamsAPI.rl("description"), "");
    public static final ColorProperty COLOR = new ColorProperty(FTBTeamsAPI.rl("color"), Color4I.WHITE);
    public static final BooleanProperty FREE_TO_JOIN = new BooleanProperty(FTBTeamsAPI.rl("free_to_join"), false);
    public static final IntProperty MAX_MSG_HISTORY_SIZE = new IntProperty(FTBTeamsAPI.rl("max_msg_history_size"), 1000);
}
