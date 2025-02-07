package dev.ftb.mods.ftbteams.api.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents the client's knowledge of some player on the server, and their team relationships.
 *
 * @param id the player's unique UUID
 * @param name the player's name
 * @param online is the player currently online?
 * @param teamId the player's team ID (same as their UUID if they are not in a party)
 * @param profile the player's game profile (their UUID and name, for convenience)
 * @param extraData any extra data relating to the player
 */
public record KnownClientPlayer(UUID id, String name, boolean online, UUID teamId, GameProfile profile, CompoundTag extraData) {

	/**
	 * Is the player in their own team (i.e. not in a party)?
	 *
	 * @return true if the player is in their own personal team right now
	 */
	public boolean isInternalTeam() {
		return teamId.equals(id);
	}

	/**
	 * Check if the player is online and not in a party.
	 *
	 * @return true if the player is online and not in a party
	 */
	public boolean isOnlineAndNotInParty() {
		return online && isInternalTeam();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		KnownClientPlayer that = (KnownClientPlayer) o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
