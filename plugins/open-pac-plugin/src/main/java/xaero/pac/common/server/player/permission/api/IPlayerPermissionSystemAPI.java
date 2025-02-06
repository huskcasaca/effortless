package xaero.pac.common.server.player.permission.api;

import java.util.Optional;
import java.util.OptionalInt;

import javax.annotation.Nonnull;

import net.minecraft.server.level.ServerPlayer;

/**
 * The interface to be overridden by addons that wish to implement additional permission systems to be used
 * by Open Parties and Claims.
 * <p>
 * Player permission system implementations must be registered in {@link IPlayerPermissionSystemRegisterAPI}.
 */
public interface IPlayerPermissionSystemAPI {

    /**
     * Gets the value of an integer permission for a specified player.
     *
     * @param player the player, not null
     * @param node   the node of the permission from {@link UsedPermissionNodes}, not null
     * @return the OptionalInt for the int value of the permission, not null
     */
    @Nonnull
    OptionalInt getIntPermission(@Nonnull ServerPlayer player, @Nonnull IPermissionNodeAPI<Integer> node);

    /**
     * Gets the value of a boolean permission for a specified player.
     *
     * @param player the player, not null
     * @param node   the node of the permission from {@link UsedPermissionNodes}, not null
     * @return the boolean value of the permission, false if it doesn't exist
     */
    boolean getPermission(@Nonnull ServerPlayer player, @Nonnull IPermissionNodeAPI<Boolean> node);

    /**
     * Gets the value of a permission with a specific value type.
     * <p>
     * This isn't used by the mod as of typing this, but could be in the future.
     *
     * @param player the player, not null
     * @param node   the node of the permission from {@link UsedPermissionNodes}, not null
     * @param <T>    the permission value type
     * @return the Optional for the value of the permission, not null
     */
    @Nonnull
    <T> Optional<T> getPermissionTyped(@Nonnull ServerPlayer player, @Nonnull IPermissionNodeAPI<T> node);

}
