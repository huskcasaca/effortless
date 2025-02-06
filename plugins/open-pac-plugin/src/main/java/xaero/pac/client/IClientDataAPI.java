package xaero.pac.client;

import net.minecraft.client.gui.screens.Screen;
import xaero.pac.client.claims.api.IClientClaimsManagerAPI;
import xaero.pac.client.controls.api.OPACKeyBindingsAPI;
import xaero.pac.client.parties.party.api.IClientPartyStorageAPI;
import xaero.pac.client.player.config.api.IPlayerConfigClientStorageManagerAPI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IClientDataAPI {

	@Nonnull
	public IPlayerConfigClientStorageManagerAPI getPlayerConfigStorageManager();

	@Nonnull
	public IClientPartyStorageAPI getClientPartyStorage();

	@Nonnull
	public IClientClaimsManagerAPI getClaimsManager();

	@Nonnull
	public OPACKeyBindingsAPI getKeyBindings();

	public void openMainMenuScreen(@Nullable Screen escape, @Nullable Screen parent);

}
