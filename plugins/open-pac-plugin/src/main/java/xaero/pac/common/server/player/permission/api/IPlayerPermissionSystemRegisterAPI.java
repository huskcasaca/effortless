package xaero.pac.common.server.player.permission.api;

import javax.annotation.Nonnull;

/**
 * The API for registering player permission system implementations.
 * <p>
 * Player permission system implementations must be registered during the
 * xaero.pac.common.event.api.OPACServerAddonRegister.EVENT on Fabric or OPACServerAddonRegisterEvent on Forge.
 */
public interface IPlayerPermissionSystemRegisterAPI {

    /**
     * Registers a player permission system implementation to be available to OPAC
     * under a specified name.
     * <p>
     * The actual permission system used by the mod is configured in the main server config file
     * with the "permissionSystem" option.
     *
     * @param name   the name to register the permission system under, not null
     * @param system the permission system implementation, not null
     */
    void register(@Nonnull String name, @Nonnull IPlayerPermissionSystemAPI system);

}
