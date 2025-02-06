package xaero.pac.client;

import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.client.claims.ClientClaimsManager;
import xaero.pac.client.claims.sync.ClientClaimsSyncHandler;
import xaero.pac.client.controls.XPACKeyBindings;
import xaero.pac.client.parties.party.ClientPartyStorage;
import xaero.pac.client.patreon.Patreon;
import xaero.pac.client.player.config.PlayerConfigClientStorageManager;
import xaero.pac.client.player.config.PlayerConfigClientSynchronizer;
import xaero.pac.common.LoadCommon;

public class LoadClient extends LoadCommon {

	public LoadClient(OpenPartiesAndClaims modMain) {
		super(modMain);
		modMain.getModSupport().initClient();
	}

	public void loadClient() {
		XPACKeyBindings keyBindings = new XPACKeyBindings();
		keyBindings.register();
		ClientClaimsManager claimsManager = ClientClaimsManager.Builder.begin().build();
		ClientClaimsSyncHandler claimsSyncHandler = new ClientClaimsSyncHandler(claimsManager);
		modMain.setClientData(
				ClientData.Builder.begin()
				.setClientTickHandler(new ClientTickHandler())
				.setClientWorldLoadHandler(new ClientWorldLoadHandler())
				.setPlayerConfigClientSynchronizer(new PlayerConfigClientSynchronizer())
				.setKeyBindings(keyBindings)
				.setPlayerConfigStorageManager(PlayerConfigClientStorageManager.Builder.begin().build())
				.setClientPartyStorage(ClientPartyStorage.Builder.begin().build())
				.setClaimsManager(claimsManager)
				.setClientClaimsSyncHandler(claimsSyncHandler)
				.build()
				);

		Patreon.checkPatreon();
	}

}
