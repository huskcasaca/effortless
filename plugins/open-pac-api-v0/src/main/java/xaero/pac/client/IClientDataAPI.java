package xaero.pac.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.gui.screens.Screen;
import xaero.pac.client.claims.api.IClientClaimsManagerAPI;
import xaero.pac.client.controls.api.OPACKeyBindingsAPI;
import xaero.pac.client.parties.party.api.IClientPartyStorageAPI;
import xaero.pac.client.player.config.api.IPlayerConfigClientStorageManagerAPI;

public interface IClientDataAPI {

    @Nonnull
    IPlayerConfigClientStorageManagerAPI getPlayerConfigStorageManager();

    @Nonnull
    IClientPartyStorageAPI getClientPartyStorage();

    @Nonnull
    IClientClaimsManagerAPI getClaimsManager();

    @Nonnull
    OPACKeyBindingsAPI getKeyBindings();

    void openMainMenuScreen(@Nullable Screen escape, @Nullable Screen parent);

}
