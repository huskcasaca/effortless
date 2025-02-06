package xaero.pac.common.server;

import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.claims.api.IServerClaimsManagerAPI;
import xaero.pac.common.server.claims.protection.api.IChunkProtectionAPI;
import xaero.pac.common.server.parties.party.api.IPartyManagerAPI;
import xaero.pac.common.server.player.config.api.IPlayerConfigManagerAPI;
import xaero.pac.common.server.player.localization.api.IAdaptiveLocalizerAPI;

public interface IServerDataAPI {

    IPartyManagerAPI getPartyManager();

    IServerClaimsManagerAPI getServerClaimsManager();

    IPlayerConfigManagerAPI getPlayerConfigs();

    OpenPACServerAPI getAPI();

    IAdaptiveLocalizerAPI getAdaptiveLocalizer();

    IChunkProtectionAPI getChunkProtection();

}
