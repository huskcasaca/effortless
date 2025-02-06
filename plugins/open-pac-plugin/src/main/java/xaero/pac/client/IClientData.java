package xaero.pac.client;

import xaero.pac.client.claims.IClientClaimsManager;
import xaero.pac.client.claims.sync.ClientClaimsSyncHandler;
import xaero.pac.client.controls.XPACKeyBindings;
import xaero.pac.client.parties.party.api.IClientPartyStorageAPI;
import xaero.pac.client.player.config.PlayerConfigClientSynchronizer;
import xaero.pac.client.player.config.api.IPlayerConfigClientStorageManagerAPI;

import javax.annotation.Nonnull;

public interface IClientData
<
	PCSM extends IPlayerConfigClientStorageManagerAPI,
	CPS extends IClientPartyStorageAPI,
	CM extends IClientClaimsManager<?, ?, ?>
> extends IClientDataAPI {
	//internal api

	@Nonnull
	@Override
	public PCSM getPlayerConfigStorageManager();

	@Nonnull
	@Override
	public CPS getClientPartyStorage();

	@Nonnull
	@Override
	public CM getClaimsManager();

	@Nonnull
	@Override
	public XPACKeyBindings getKeyBindings();

	public ClientTickHandler getClientTickHandler();
	public ClientWorldLoadHandler getClientWorldLoadHandler();
	public PlayerConfigClientSynchronizer getPlayerConfigClientSynchronizer();
	public void reset();
	public ClientClaimsSyncHandler getClientClaimsSyncHandler();

}
