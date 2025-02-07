package xaero.pac.client.parties.party.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * API for the party storage on the client side
 */
public interface IClientPartyStorageAPI {

    /**
     * Gets the local client player's party.
     *
     * @return the player's party, null if not in one
     */
    @Nullable
    IClientPartyAPI getParty();

    /**
     * Gets the name of the local client player's party.
     *
     * @return the name of the party, null if not in a party
     */
    @Nullable
    String getPartyName();

    /**
     * Gets the party ally info storage.
     *
     * @return ally info storage, not null
     */
    @Nonnull
    IClientPartyAllyInfoStorageAPI getAllyInfoStorage();

    /**
     * Gets the party/ally player dynamic info storage.
     * <p>
     * The dynamic info includes locations of some party members and allies.
     *
     * @return the party/ally player dynamic info storage, not null
     */
    @Nonnull
    IClientPartyMemberDynamicInfoSyncableStorageAPI getPartyMemberDynamicInfoSyncableStorage();

    /**
     * Checks whether the party data sync is still in progress.
     * <p>
     * Party data starts loading in the background when you join the server.
     *
     * @return true if party data is loading, otherwise false
     */
    boolean isLoading();

    /**
     * Gets the number of members in the local client player's party, meant for the UI.
     * <p>
     * This value is updated before individual member info starts syncing and doesn't necessarily represent the real
     * number of member info currently loaded. It should mainly be used for displaying the member count on the UI.
     *
     * @return the member count for the party meant for the UI
     */
    int getUIMemberCount();

    /**
     * Gets the number of ally parties for the local client player's party, meant for the UI.
     * <p>
     * This value is updated before individual ally info starts syncing and doesn't necessarily represent the real
     * number of ally info currently loaded. It should mainly be used for displaying the ally count on the UI.
     *
     * @return the ally count for the party meant for the UI
     */
    int getUIAllyCount();

    /**
     * Gets the number of active invitations for the local client player's party, meant for the UI.
     * <p>
     * This value is updated before individual invitation info starts syncing and doesn't necessarily represent the real
     * number of invites currently loaded. It should mainly be used for displaying the invite count on the UI.
     *
     * @return the invite count for the party meant for the UI
     */
    int getUIInviteCount();

    /**
     * Gets the maximum number of members allowed in a party.
     *
     * @return the maximum allowed number of members
     */
    int getMemberLimit();

    /**
     * Gets the maximum number of ally parties allowed for a party.
     *
     * @return the maximum allowed number of allies
     */
    int getAllyLimit();

    /**
     * Gets the maximum number of active invitations allowed for a party.
     *
     * @return the maximum allowed number of invites
     */
    int getInviteLimit();

}
