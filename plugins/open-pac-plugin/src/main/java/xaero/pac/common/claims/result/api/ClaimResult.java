package xaero.pac.common.claims.result.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import xaero.pac.common.claims.player.api.IPlayerChunkClaimAPI;

/**
 * A claim result for a single chunk
 */
public class ClaimResult<C extends IPlayerChunkClaimAPI> {

    private final C claimResult;
    private final Type resultType;

    /**
     * A constructor for internal usage
     *
     * @param claimResult the claim state where relevant
     * @param resultType  the result type
     */
    public ClaimResult(@Nullable C claimResult, @Nonnull Type resultType) {
        super();
        this.claimResult = claimResult;
        this.resultType = resultType;
    }

    /**
     * Gets the claim state associated with this result where relevant.
     *
     * @return the claim state for this result, can be null
     */
    @Nullable
    public C getClaimResult() {
        return claimResult;
    }

    /**
     * Gets the {@link ClaimResult.Type} of this result.
     *
     * @return the type of this result, not null
     */
    @Nonnull
    public Type getResultType() {
        return resultType;
    }

    /**
     * All types of claim action results
     */
    public enum Type {

        /**
         * A chunk was already forceloadable
         */
        ALREADY_FORCELOADABLE(null, false, false),

        /**
         * A chunk was already not forceloadable
         */
        ALREADY_UNFORCELOADED(null, false, false),

        /**
         * The claims feature is disabled
         */
        CLAIMS_ARE_DISABLED(null, false, true),

        /**
         * The area for a claim action was too big
         */
        TOO_MANY_CHUNKS(null, false, true),

        /**
         * The dimension is unclaimable
         */
        UNCLAIMABLE_DIMENSION(null, false, true),

        /**
         * The chunk isn't claimed by who is trying to (un)forceload it
         */
        NOT_CLAIMED_BY_USER_FORCELOAD(null, false, true),

        /**
         * The chunk isn't claimed by who is trying to unclaim it
         */
        NOT_CLAIMED_BY_USER(null, false, true),

        /**
         * The chunk is already claimed
         */
        ALREADY_CLAIMED(null, false, true),

        /**
         * The maximum number of forceloadable claims was reached
         */
        FORCELOAD_LIMIT_REACHED(null, false, true),

        /**
         * The maximum number of claims was reached
         */
        CLAIM_LIMIT_REACHED(null, false, true),

        /**
         * The chunk was beyond the maximum distance
         */
        TOO_FAR(null, false, true),

        /**
         * There is a claim replacement currently in progress in the background
         */
        REPLACEMENT_IN_PROGRESS(null, false, true),

        /**
         * The user doesn't have permission to make server claims
         * <p>
         * This result type is only used for server claim requests made by online players.
         * Permissions for server claims are not checked by the try methods in the server claims manager.
         */
        NO_SERVER_PERMISSION(null, false, true),

        /**
         * Successfully unforceloaded a chunk
         */
        SUCCESSFUL_UNFORCELOAD(null, true, false),

        /**
         * Successfully unclaimed a chunk
         */
        SUCCESSFUL_UNCLAIM(null, true, false),

        /**
         * Successfully forceloaded a chunk
         */
        SUCCESSFUL_FORCELOAD(null, true, false),

        /**
         * Successfully claimed a chunk
         */
        SUCCESSFUL_CLAIM(null, true, false);

        /**
         * A message describing the result
         */
        @Nonnull
        public final Component message;

        /**
         * Whether the result can be considered a success
         */
        public final boolean success;

        /**
         * Whether the result can be considered a failure
         */
        public final boolean fail;

        Type(Component message, boolean success, boolean fail) {
            this.message = message;
            this.success = success;
            this.fail = fail;
        }

    }

}
