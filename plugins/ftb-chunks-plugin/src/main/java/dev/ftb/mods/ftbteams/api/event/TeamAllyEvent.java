package dev.ftb.mods.ftbteams.api.event;

import com.mojang.authlib.GameProfile;
import dev.ftb.mods.ftbteams.api.Team;

import java.util.Collection;
import java.util.List;

/**
 * Fired when one or more players are added or removed as allies for a team.
 */
public class TeamAllyEvent extends TeamEvent {
    private final List<GameProfile> players;
    private final boolean adding;

    public TeamAllyEvent(Team team, List<GameProfile> players, boolean adding) {
        super(team);
        this.players = players;
        this.adding = adding;
    }

    /**
     * Get the players that are affected (either being added or removed)
     *
     * @return the affected players
     */
    public Collection<GameProfile> getPlayers() {
        return players;
    }

    /**
     * Are the players being added as allies, or removed?
     *
     * @return true if the players are being added, false otherwise
     */
    public boolean isAdding() {
        return adding;
    }
}
