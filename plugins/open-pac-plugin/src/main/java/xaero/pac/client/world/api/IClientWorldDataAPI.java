package xaero.pac.client.world.api;

/**
 * API for data attached to a client world
 */
public interface IClientWorldDataAPI {

    /**
     * Checks whether the handshake has been received from the server after entering this world/dimension indicating that
     * this mod is installed on the server side.
     *
     * @return true if the mod is installed on the server side and the handshake has been received, otherwise false
     */
    boolean serverHasMod();

    /**
     * If {@link #serverHasMod()} is true, then this checks if the server has the claims feature enabled.
     *
     * @return true if the server has the claims feature enabled, otherwise false
     */
    boolean serverHasClaimsEnabled();

    /**
     * If {@link #serverHasMod()} is true, then this checks if the server has the parties feature enabled.
     *
     * @return true if the server has the parties feature enabled, otherwise false
     */
    boolean serverHasPartiesEnabled();

}
