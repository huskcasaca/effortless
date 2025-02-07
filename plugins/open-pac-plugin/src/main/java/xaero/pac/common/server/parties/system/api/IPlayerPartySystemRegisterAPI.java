package xaero.pac.common.server.parties.system.api;

/**
 * The API for registering party system implementations.
 * <p>
 * Party system implementations must be registered during the
 * xaero.pac.common.event.api.OPACServerAddonRegister.EVENT on Fabric or OPACServerAddonRegisterEvent on Forge.
 */
public interface IPlayerPartySystemRegisterAPI {

    /**
     * Registers a party system implementation to be available to OPAC
     * under a specified name.
     * <p>
     * The primary party system used by the mod is configured in the main server config file
     * with the "primaryPartySystem" option.
     *
     * @param name   the name to register the party system under, not null
     * @param system the party system implementation, not null
     */
    void register(String name, IPlayerPartySystemAPI<?> system);

}
