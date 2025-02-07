package xaero.pac.client.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.gui.screens.Screen;
import xaero.pac.client.IClientDataAPI;
import xaero.pac.client.claims.api.IClientClaimsManagerAPI;
import xaero.pac.client.controls.api.OPACKeyBindingsAPI;
import xaero.pac.client.parties.party.api.IClientPartyStorageAPI;
import xaero.pac.client.player.config.api.IPlayerConfigClientStorageManagerAPI;
import xaero.pac.common.capability.api.ICapabilityHelperAPI;

/**
 * This is the main client API access point. You can get the instance with {@link #get()}.
 * <p>
 * For functionality that requires registering handlers/listeners, it is often required or recommended to
 * do so during the OPACClientAddonRegister.EVENT on Fabric or the OPACClientAddonRegisterEvent on Forge.
 * <p>
 * Make sure to check whether the server side has the mod installed with
 * <p>
 * {@code
 * ClientWorldMainCapabilityAPI capability = api.getCapabilityHelper().getCapability(Minecraft.getInstance().level, ClientWorldCapabilityTypes.MAIN_CAP);
 * IClientWorldDataAPI worldData = capability.getClientWorldData();
 * boolean serverHasMod = worldData.serverHasMod();
 * }
 * <p>
 * for each client world instance ({@code Minecraft.getInstance().level}).
 */
public final class OpenPACClientAPI {

    private IClientDataAPI getClientData() {
        throw new RuntimeException("stub!");
    }

    /**
     * Gets the API for the client-side config data updated by the server.
     *
     * @return instance of the client-side player config API, not null
     */
    @Nonnull
    public IPlayerConfigClientStorageManagerAPI getPlayerConfigClientStorageManager() {
        return getClientData().getPlayerConfigStorageManager();
    }

    /**
     * Gets the API for the client-side party data updated by the server.
     *
     * @return instance of the client-side party API, not null
     */
    @Nonnull
    public IClientPartyStorageAPI getClientPartyStorage() {
        return getClientData().getClientPartyStorage();
    }

    /**
     * Gets the API for the client-side claims manager updated by the server.
     *
     * @return instance of the client-side claims manager API, not null
     */
    @Nonnull
    public IClientClaimsManagerAPI getClaimsManager() {
        return getClientData().getClaimsManager();
    }

    /**
     * Gets the API for the mod's keybindings.
     *
     * @return instance of the mod's keybindings API, not null
     */
    @Nonnull
    public OPACKeyBindingsAPI getKeyBindings() {
        return getClientData().getKeyBindings();
    }

    /**
     * Opens the main menu screen of the Parties and Claims mod.
     *
     * @param escape the screen to switch to when the escape key is hit, can be null
     * @param parent the screen to switch to when the screen is exited normally, can be null
     */
    public void openMainMenuScreen(@Nullable Screen escape, @Nullable Screen parent) {
        getClientData().openMainMenuScreen(escape, parent);
    }

    /**
     * @return instance of the capability API, not null
     */
    @Nonnull
    public ICapabilityHelperAPI getCapabilityHelper() {
        throw new RuntimeException("stub!");
    }

    /**
     * Gets the client-side Open Parties and Claims API instance.
     *
     * @return instance of the client-side API, not null
     */
    @Nonnull
    public static OpenPACClientAPI get() {
        throw new RuntimeException("stub!");
    }

}
